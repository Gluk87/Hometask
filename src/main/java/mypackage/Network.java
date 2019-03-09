package mypackage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

class Network {

    static boolean isNetworkActiv() {
        InetAddress host;
        try {
            host = InetAddress.getByName("randomuser.me");
        } catch (UnknownHostException e) {
            return false;
        }
        try {
            assert host != null;
            return host.isReachable(1000);
        } catch (IOException e) {
            return false;
        }
    }
}
