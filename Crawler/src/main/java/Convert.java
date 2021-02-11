import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.*;

public class Convert {

    private static final String CRAWLED_JSON_FILE = "..\\minigoogle\\src\\main\\webapp\\WEB-INF\\";

    //Convert Two Maps into Json String and put the Json String into Json File
    static void convertToJson(Map<String, List<String>> wordAndListUrl, Map<String, ArrayList<String>> urlAndHtmlInfo, Integer maxDistance) throws IOException {
        Gson gson = new Gson();
        Collection collection = new ArrayList<>();
        collection.add(wordAndListUrl);
        collection.add(urlAndHtmlInfo);
        String json = gson.toJson(collection);
        String clean_description = json.replaceAll("[^\\x20-\\x7e]", "");
        try {
            File crawledJsonFile = new File(CRAWLED_JSON_FILE+"CRAWLED JSON FILE "+maxDistance+".txt");
            crawledJsonFile.createNewFile();
            FileWriter myWriter = new FileWriter(CRAWLED_JSON_FILE+"CRAWLED JSON FILE "+maxDistance+".txt");
            myWriter.write(clean_description);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An Error Occurred when write wordAndListUrl & urlAndHtmlInfo into CRAWLED JSON FILE!");
            e.printStackTrace();
        }

    }


    //Convert the info in the Json file back to wordAndListUrl
    static Map<String, List<String>> convertJasonToWordIndexTop10Urls(Integer maxDistance) throws FileNotFoundException {
        Gson gson = new Gson();
        Map<String, List<String>> wordAndListUrl = new HashMap<>();
        try {
            Reader myReader = new FileReader(CRAWLED_JSON_FILE+"CRAWLED JSON FILE "+maxDistance+".txt");
            JsonArray array = JsonParser.parseReader(myReader).getAsJsonArray();
            wordAndListUrl = gson.fromJson(array.get(0), Map.class);

        } catch (IOException e) {
            System.out.println("An Error Occurred when READ WordAndListUrls FROM CRAWLED JSON FILE!");
            e.printStackTrace();
        }
        return wordAndListUrl;
    }


    //Convert the info in the Json file back to urlAndHtmlInfo
    static Map<String, ArrayList<String>> convertJasonToUrlIndexHtmlData(Integer maxDistance) throws FileNotFoundException {
        Gson gson = new Gson();
        Map<String, ArrayList<String>> urlAndHtmlInfo = new HashMap<>();
        try {
            Reader myReader = new FileReader(CRAWLED_JSON_FILE+"CRAWLED JSON FILE "+maxDistance+".txt");
            JsonArray array = JsonParser.parseReader(myReader).getAsJsonArray();
            urlAndHtmlInfo = gson.fromJson(array.get(1), Map.class);

        } catch (
                IOException e) {
            System.out.println("An Error Occurred when READ UrlAndHtmlInfos FROM CRAWLED JSON FILE!");
            e.printStackTrace();
        }
        return urlAndHtmlInfo;
    }
}
