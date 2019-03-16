package mypackage;

import java.net.HttpURLConnection;
import java.net.URL;

class Network {

    static boolean isNetworkActive() {
        boolean result = false;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL("https://randomuser.me").openConnection();
            con.setRequestMethod("HEAD");
            result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception ignored) {
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
