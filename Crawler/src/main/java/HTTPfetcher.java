import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HTTPfetcher {



    public static String returnHTML(String string_url) throws IOException {
        //read a blog and create this method
        try{
        URL url = new URL(string_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        //Map<String, String> parameters = new HashMap<>();
        //parameters.put("param1", "val");

        con.setDoOutput(true);
        //set Timeouts
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setInstanceFollowRedirects(true);
        con.setFollowRedirects(true);

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        //out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();

        //set Request Header
        //con.setRequestProperty("Content-Type", "application/json");
        //String contentType = con.getHeaderField("Content-Type");



        //read the cookies from a response
        //String cookiesHeader = con.getHeaderField("Set-Cookie");
        //List<HttpCookie> cookies = HttpCookie.parse(cookiesHeader);

        //add the cookies ;to the cookie store
/*
        CookieManager cookieManager = null;
        cookies.forEach(cookie -> cookieManager.getCookieStore().add(null, cookie));

        Optional<HttpCookie> usernameCookie = cookies.stream()
                .findAny().filter(cookie -> cookie.getName().equals("username"));
        if (usernameCookie == null) {
            cookieManager.getCookieStore().add(null, new HttpCookie("username", "john"));
        }


        //add the cookies to the request
        con.disconnect();
        con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Cookie", string_url.join((CharSequence) cookieManager.getCookieStore().getCookies(), ";"));

        //enable or disable automatically following redirects for a specific connection


        //enable or disable automatic redirect for all connections:



        int status = con.getResponseCode();

        if (status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_MOVED_PERM) {
            String location = con.getHeaderField("Location");
            URL newUrl = new URL(location);
            con = (HttpURLConnection) newUrl.openConnection();
        }
*/

        //To execute the request, we can use the getResponseCode(), connect(), getInputStream() or getOutputStream() methods:
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        //we can consume the stream provided by HttpUrlConnection.getErrorStream().
        /*
        int status = con.getResponseCode();

        Reader streamReader = null;

        if (status > 299) {
            streamReader = new InputStreamReader(con.getErrorStream());
        } else {
            streamReader = new InputStreamReader(con.getInputStream());
        }*/

        //close the connection
        con.disconnect();
        in.close();
        out.close();

        return content.toString();
        }
        catch (Exception e){
            return null;
        }


    }
}


