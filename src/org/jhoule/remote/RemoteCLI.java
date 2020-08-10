package org.jhoule.remote;

import java.util.Scanner;

import org.jhoule.remote.lg2k11.LG2k11Remote;

public class RemoteCLI {

    static final String COMMAND_QUIT = "!q";

    private static Remote getRemote() {
        Remote rem = null;
        try {
            rem = new LG2k11Remote();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return rem;
    }

    public static void main(String[] args) {

        Remote rem = getRemote();

        if (!(rem instanceof Remote && rem.setup())) {
            System.err.println("Could not initiate remote");
            System.exit(-1);
        }

        System.out.println("Remote control started - use '" + COMMAND_QUIT + "' to quit gracefully");
        Scanner s = new Scanner(System.in);
        try {

            while (true) {
                if (!rem.isDeviceAvailable()) {
                    System.err.println("Remote states device is not available");
                    continue;
                }

                System.out.println("button name?");
                String name = s.nextLine();
                System.out.println();

                if (name.equalsIgnoreCase(COMMAND_QUIT)) {
                    System.exit(1);
                }

                rem.pressButton(name);
            }
        } finally {
            s.close();
            if (rem != null)
            {
                rem.shutDown();
            }
        }
    }

}
