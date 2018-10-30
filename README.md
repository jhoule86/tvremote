# tvremote
universal TV remote
by jhoule

Mostly contains Junits for running commands against my 2011 LG Smart TV.
This is a fun tinkering tool for HTTP and UDP client code, and I actually use it to lower the volume or turn the TV off when it's been left on in the other room.

Unfortunately, due to firmware differences, it only works for the line of TVs supported (poorly) by the "LG TV Remote 2011" Android app ( https://play.google.com/store/apps/details?id=com.clipcomm.WiFiRemocon&hl=en_US)

In theory, this library works better than the Android app because it can negotiate before sending a command (instead of at app startup) and will not die a horrible death each time the app's session with the TV's internal server expires.
