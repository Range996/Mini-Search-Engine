
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPfetcher2 {

    public static String getHttpResponse(String link) {
        HttpURLConnection con = null;
        DataOutputStream out = null;
        BufferedReader in = null;
        try {
            System.out.println("Fetch link=" + link);
            // Establish connection
            URL url = new URL(link);
            con = (HttpURLConnection) url.openConnection();
            // Setup for request
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setConnectTimeout(2000);
            con.setReadTimeout(10000);
            out = new DataOutputStream(con.getOutputStream());
            out.flush();

            // Handle response
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String input;
            StringBuffer content = new StringBuffer();
            while ((input = in.readLine()) != null) {
                content.append(input);
            }

            System.out.println("Complete link=" + link);
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
            try {
                in.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }
}