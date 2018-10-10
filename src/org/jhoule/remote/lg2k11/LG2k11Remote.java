package org.jhoule.remote.lg2k11;

import org.jhoule.remote.Button;
import org.jhoule.remote.Remote;
import org.jhoule.remote.RemoteImpl;
import org.jhoule.remote.lg2k11.LG2k11Button.*;
import org.jhoule.ws.client.URLResource;
import org.jhoule.ws.client.http.HTTPClient;
import org.jhoule.ws.client.http.NativeHTTPClient;
import org.jhoule.ws.client.udp.UDPClient;
import org.jhoule.ws.client.udp.UDPResource;
import org.jhoule.ws.client.udp.UnicastUDPClient;

import java.io.ByteArrayOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.CRC32;

/**
 * Created by jhoule on 10/25/2014.
 */
public class LG2k11Remote extends RemoteImpl implements Remote
{

    public static int PORT_HTTP = 8080;
    public static int PORT_UDP = 7070;

    /**
     * Client used to talk to TV's "HDCP" web interface
     * which mostly provides auth details.
     *
     * TODO: implement HTTP server in order to listen to TV's mode change announcements!
     *
     */
    HTTPClient mHTTPClient;
    UDPClient mUDPSender;

    final String mTVAddress;
    final String mAuthKey;
    int mSessionID = -1;

    static {
        mButtons = new ButtonMap();
        mKeypad = new ButtonMap(10);

        // power
        mButtons.put(new LGPowerButton());

        // volume/mute
        mButtons.put(new LGMuteButton());
        mButtons.put(new LGVolUpButton());
        mButtons.put(new LGVolDownButton());

        // navigation
        mButtons.put(new LGGHomeButton());
        // TODO: up, down, left, right

        // color buttons
        mButtons.put(new LGGreenButton());
        // TODO: rest of color buttons

        // playback
        // TODO: playback buttons
        mButtons.put(new LGStopButton());

        // input
        // TODO: input buttons

        // keypad [0, 9] and dash/underscore.
        mKeypad.put(new LGKeypadButton(0));
        mKeypad.put(new LGKeypadButton(1));
        mKeypad.put(new LGKeypadButton(2));
        mKeypad.put(new LGKeypadButton(3));
        mKeypad.put(new LGKeypadButton(4));
        mKeypad.put(new LGKeypadButton(5));
        mKeypad.put(new LGKeypadButton(6));
        mKeypad.put(new LGKeypadButton(7));
        mKeypad.put(new LGKeypadButton(8));
        mKeypad.put(new LGKeypadButton(9));

        mKeypad.put(new LGDashButton());

        // add the keypad to the full collection of numbers
        mButtons.putAll(mKeypad);
    }

    public LG2k11Remote(String aTVAddress, String aAuthKey) {
        mTVAddress = aTVAddress;
        mAuthKey = aAuthKey;

        mHTTPClient = new NativeHTTPClient();
        mUDPSender = new UnicastUDPClient();

        try {

            if (mTVAddress == null || mTVAddress.length() < 1) {
                throw new Exception("adddress not specified");
            }

            final InetAddress addr = InetAddress.getByName(mTVAddress);
            if (!(addr instanceof Inet4Address && addr.getHostAddress().equals(mTVAddress))) {
                throw new Exception("address not resolvable to IP address");
            }

        } catch (Exception t) {
            throw new RuntimeException(mTVAddress + "is not a valid IPv4 address");
        }
    }

    boolean isTVAvailable() {
        int response;
        try {
            mHTTPClient.setResource(createResourceForVersionInfo());
            mHTTPClient.open();
            response = mHTTPClient.doRequest(HTTPClient.METHOD.GET, null);

            byte[] payload = mHTTPClient.getLastResponsePayload();
            if (payload != null) {
                System.err.println(new String());
            }

        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
            return false;
        } finally {
            mHTTPClient.close();
        }
        return (response == 200);
    }

    boolean confirmSession() {
        int response;
        try {
            mHTTPClient.setResource(createResourceForConfirmingAuth());
            mHTTPClient.open();
            response = mHTTPClient.doRequest(HTTPClient.METHOD.GET, null);

            System.err.println(new String(mHTTPClient.getLastResponsePayload()));

        } catch (Exception e) {
            return false;
        } finally {
            mHTTPClient.close();
        }
        return (response == 200);
    }

    int establishSession() {
        byte[] payload = ("<?xml version=\"1.0\" encoding=\"utf-8\"?><auth><type>AuthReq</type><value>" + mAuthKey + "</value></auth>").getBytes();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/atom+xml");
        int response = -1;

        // have to ask for code every time... because TV is dumb?
        try {
            mHTTPClient.setResource(createResourceForAuthDisplay());
            mHTTPClient.open();
            response = mHTTPClient.doRequest(HTTPClient.METHOD.POST, payload, headers);

        } catch (Exception e) {
            return -1;
        } finally {
            mHTTPClient.close();
        }

        if (response != 200) {
            throw new RuntimeException("HTTP call failed for displaying pairing key: " + mHTTPClient.getLastError());
        }

        try {
            mHTTPClient.setResource(createResourceForAuthKey());
            mHTTPClient.open();
            response = mHTTPClient.doRequest(HTTPClient.METHOD.POST, payload, headers);

        } catch (Exception e) {
            return -1;
        } finally {
            mHTTPClient.close();
        }

        if (response != 200) {
            throw new RuntimeException("HTTP call failed for auth: " + mHTTPClient.getLastError());
        }

        byte[] answerBytes = mHTTPClient.getLastResponsePayload();
        if (answerBytes == null || answerBytes.length < 1) {
            throw new RuntimeException("HTTP call failed for auth: no data");
        }

        String answer = new String(answerBytes);
        // <?xml version="1.0" encoding="utf-8"?><envelope><HDCPError>200</HDCPError><HDCPErrorDetail>OK</HDCPErrorDetail><session>114859659</session></envelope>

        final String sessionTag = "<session>";
        final String sessionEnd = "</session>";

        int start = answer.indexOf(sessionTag) + sessionTag.length();
        int end = answer.indexOf(sessionEnd, start);

        if (start < 0 || start > answer.length() - 1 || end < 0 || end > answer.length() - 1) {
            throw new RuntimeException("HTTP call failed for auth: \"session\" element is missing");
        }

        mSessionID = Integer.parseInt(answer.substring(start, end));

        // confirm that we got the code
        if (!confirmSession()) {
            mSessionID = -1;
            throw new RuntimeException("HTTP confirmation call failed!");
        }

        mUDPSender.setResource(createResourceForUDP());

        return mSessionID;
    }

    boolean hasSessionID() {
        return mSessionID > 0;
    }

    protected URLResource createResourceForVersionInfo() {
        final String resourcePath = "/hdcp/api/data?target=version_info";
        try {
            URL url = new URL(getHTTPURL().toString() + resourcePath);
            return new URLResource(url);
        } catch (Exception e) {
            return null;
        }
    }

    protected URLResource createResourceForAuthDisplay() {
        final String resourcePath = "/hdcp/api/auth";
        try {
            URL url = new URL(getHTTPURL().toString() + resourcePath);
            return new URLResource(url);
        } catch (Exception e) {
            return null;
        }
    }

    protected URLResource createResourceForAuthKey() {
        final String resourcePath = "/hdcp/api/auth";
        try {
            URL url = new URL(getHTTPURL().toString() + resourcePath);
            return new URLResource(url);
        } catch (Exception e) {
            return null;
        }
    }

    protected URLResource createResourceForConfirmingAuth() {
        final String resourcePath = "/hdcp/api/data?target=context_ui&session=" + mSessionID;
        try {
            URL url = new URL(getHTTPURL().toString() + resourcePath);
            return new URLResource(url);
        } catch (Exception e) {
            return null;
        }
    }

    protected URL getHTTPURL() {
        try {
            return new URL("http://" + mTVAddress + ":" + PORT_HTTP);
        } catch (Exception e) {
            return null;
        }
    }

    protected UDPResource createResourceForUDP() {
        try {
            return new UDPResource(mTVAddress, PORT_UDP);
        } catch (Exception e) {
            return null;
        }
    }

    public void pressButton(Button aButton) {
        if (aButton == null) {
            throw new NullPointerException("Button to press was null");
        }

        Object o = aButton.getVirtualValue();
        if (! (o instanceof Integer))
        {
            throw new IllegalArgumentException("This remote only accepts Integer Buttons");
        }

        Integer i = (Integer) o;

        pressButton(i);
    }

    protected void pressButton(Integer aButtonNumber) {
        final int cmd = 1;

        if (aButtonNumber == null)
        {
            throw new IllegalArgumentException("Unable to press NULL button");
        }

        byte[] argBytes = ByteBuffer.allocate(4).putInt(aButtonNumber).array();
        sendUDPCommand(cmd, argBytes);
    }

    protected static void reverseBA(byte[] aArray) {
        if (aArray == null) {
            return;
        }
        int i = 0;
        int j = aArray.length - 1;
        byte tmp;
        while (j > i) {
            tmp = aArray[j];
            aArray[j] = aArray[i];
            aArray[i] = tmp;
            j--;
            i++;
        }
    }

    protected byte[] getSessionIDAsBytes() {
        byte[] bytes = ByteBuffer.allocate(4).putInt(mSessionID).array();
        reverseBA(bytes);
        return bytes;
    }

    protected void sendUDPCommand(int aCommand, byte[] aDataArray) {
        byte[] toSend = null;

        byte[] checksum = ByteBuffer.allocate(4).putInt(0).array();
        Integer bigCMD = aCommand;
        byte[] cmd = ByteBuffer.allocate(2).putShort(bigCMD.shortValue()).array();
        reverseBA(cmd);

        byte[] sessionBytes = getSessionIDAsBytes();
        // already an LSB. don't reverse!

        byte[] data = aDataArray;
        reverseBA(data);
        byte[] lenData = ByteBuffer.allocate(4).putInt(data.length).array();
        reverseBA(lenData);

        /*
        Bytes	Content
        0-3	CRC32 checksum of the whole message (bytes 0-3, i.e. this bytes, are set to "0x00" for the CRC32 calculation)
        4-7	Session ID
        8-9	Command 1
        10-13	Length of following array
        14-21	data array (fist four bytes, i.e. 14-17 might be Command 2)

        ! !

        Command 1	Name	                Parameters
        -----------------------------------------------
        1	        key input	            1x 4 bytes
        2	        touch move	            2x 4 bytes
        3	        touch click	            1x 4 bytes
        5	        set favorite channel	2x 4 bytes
        7	        channel change	        2x 4 bytes
        9	        text input	            Command 2 = 1, String

         */

        try {
            ByteArrayOutputStream preHash = new ByteArrayOutputStream();
            ByteArrayOutputStream noHash = new ByteArrayOutputStream();


            noHash.write(sessionBytes);
            noHash.write(cmd);
            noHash.write(lenData);
            noHash.write(data);
            byte[] theRest = noHash.toByteArray();

            preHash.write(checksum);
            preHash.write(theRest);

            CRC32 crc = new CRC32();
            crc.update(preHash.toByteArray());
            Long val = crc.getValue();
            int crcInt = val.intValue();
            checksum = ByteBuffer.allocate(4).putInt(crcInt).array();
            reverseBA(checksum);

            // now create real request, with checksum filled in
            ByteArrayOutputStream postHash = new ByteArrayOutputStream();
            postHash.write(checksum);
            postHash.write(theRest);

            toSend = postHash.toByteArray();

            System.out.println("Sending command: " + bytesToHex(toSend));

            mUDPSender.open();
            mUDPSender.sendBytes(toSend);
            mUDPSender.close();

        } catch (Throwable t) {
            System.err.println(t.toString());
        }
    }

    protected static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            builder.append(hexArray[v >>> 4]).append(hexArray[v & 0x0F]);
            if (j < bytes.length - 1) {
                builder.append(':');
            }
        }
        return builder.toString();
    }

    public static void main(String [] args)
    {

        final String quit = "REMOTE_OFF";

        LG2k11Remote rem = new MyLGRemote();

        boolean on = rem.isTVAvailable();
        if (!on)
        {
            throw new RuntimeException("TV is not on");
        }

        Scanner s = new Scanner(System.in);

        boolean running = true;
        boolean haveSession = false;

        while (running) {
            if (!rem.isTVAvailable())
            {
                // invalidate session
                haveSession = false;

                System.out.println("TV is not on.");
                break;
            }

            if (! haveSession)
            {
                try {
                    rem.establishSession();
                } catch (Exception e) {
                    throw new RuntimeException("Unable to establish session", e);
                }
            }

            System.out.println("button name?");
            String name = s.nextLine();
            System.out.println();

            if (name.equalsIgnoreCase(quit))
            {
                running = false;
                break;
            }

            rem.pressButton(name);
        }
    }
}
