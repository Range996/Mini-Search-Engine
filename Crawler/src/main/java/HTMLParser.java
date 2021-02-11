import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HTMLParser {

    public static HTMLData returnParsedHTML(String htmlContent, String url){
        try {
            //get the urls of the neighbors
            HTMLData htmldata = new HTMLData();
            Document doc = Jsoup.parse(htmlContent);
            List<String> links = new ArrayList<>();

            Element body = doc.body();

            for (Element element : body.select("a[href]")) {
                String href = element.attr("href");
                if (isValidURL(href))
                    links.add(href);

            }
            htmldata.neighbors = links;

            //get the title

            String title = doc.title();
            htmldata.title = title;


            //get the description
            Elements metalink = doc.select("meta[name=description]");
            String description = metalink.size() == 0 ? title : metalink.first().attr("content").toString(); //ternary expression
            htmldata.description = description;


            //get the url
            htmldata.url = url;

            //get the text
            htmldata.text = body.text();

            return htmldata;
        }
        catch (Exception e){
            return null;
        }
    }

    private static boolean isValidURL(String href){
        /* Try creating a valid URL */
        try {
            new URL(href).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

}
