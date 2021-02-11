import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BFS {



    public static List<HTMLData> bfsForSeed(String seedUrl, Integer maxDistance) throws IOException {


        //the List of HTMLData that needs to be returned at the end of this method
        List<HTMLData> htmlDataes = new ArrayList<>();

        //Queue for BFS
         Queue<String> queue = new LinkedList<>();

        //URLs already visited
         Map<String, Integer> marked = new HashMap<>();

         //mark the current url as visited and enqueue it
        marked.put(seedUrl,new Integer(0));
        queue.add(seedUrl);


        while (queue.size() > 0){
            //Dequeue a vertex from queue and get the information of the url and put it into the List of HTMLData
            String s = queue.poll();
            //System.out.println("Process " + s + "...");
            String htmlContent = HTTPfetcher.returnHTML(s);
            if (htmlContent == null || htmlContent.isEmpty()) {
                continue;
            }

            HTMLData htmlData = HTMLParser.returnParsedHTML(htmlContent, s);
            if (htmlData == null){
                continue;
            }
            htmlDataes.add(htmlData);

            //while(marked.containsKey(s) ){
                int currentPosition = getValueOfMap(s, marked);
                int nextPosition = currentPosition +1;
                if (currentPosition < maxDistance){
                    //String newHtmlContent = HTTPfetcher.returnHTML(s);
                    //HTMLData currentHTMLData =  HTMLParser.returnParsedHTML(newHtmlContent);
                    List<String> currentNeighbors = htmlData.neighbors;
                    for (String link : currentNeighbors){
                        if(!isUrlExisted(link, marked))
                            queue.add(link);
                        marked.put(link, new Integer(nextPosition));

                    }
                    //marked.remove(s);
                }
            //}
        }

        return htmlDataes;
    }

    //insert the neighbors of the current url into the queue
    private static Queue<String> returnQueue (Queue<String> queue, String inputUrl){

        Document doc = Jsoup.parse(inputUrl);
        Element body = doc.body();

        if (isValidURL(inputUrl)){
            for (Element element : body.select("a[href]")) {
                String href = element.attr("href");
                if (isValidURL(href))
                    queue.add(href);
            }
        }
        return queue;
    }


    //confirm the input url's validation
    private static boolean isValidURL(String href) {
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


//    //confirm the value of url is in the distance
//    private static boolean isWithinTheDistance(String url, Map<String, Integer> map){
//        for (Map.Entry<String, Integer> e : map.entrySet()) {
//            if (e.getKey().equals(url) && (e.getValue() < MAX_DISTANCE )) {
//                return true;
//            }
//        }
//        return false;
//    }

    //get the Value of the current key of HTML Map

    private static int getValueOfMap(String url, Map<String, Integer> map){
        for (Map.Entry<String, Integer> entry : map.entrySet()){
            if (entry.getKey().equals(url)){
                return entry.getValue();
            }
        }
        return 99999;
    }

    //confirm whether the url has already existed in the Map
    private static boolean isUrlExisted (String url, Map<String, Integer> map){
        for (Map.Entry<String, Integer> entry : map.entrySet()){
            if (entry.getKey().equals(url)){
                return true;
            }
        }
        return false;
    }
}
