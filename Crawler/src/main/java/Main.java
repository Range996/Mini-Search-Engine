import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import org.w3c.dom.html.HTMLParagraphElement;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Main {
    private static final int MAX_DISTANCE = 5;
    private static final String ASU_URL = "https://www.asu.edu";
    private static final int PAGE_NUM_URL = 10;
    private static final String CRAWLED_URL_FILE = "..\\minigoogle\\src\\main\\webapp\\WEB-INF\\CRAWLED URL FILE.txt";
    private static final String CRAWLED_JSON_FILE = "..\\minigoogle\\src\\main\\webapp\\WEB-INF";


    public static void main(String[] args) throws IOException {
//        String htmlContent = HTTPfetcher.returnHTML("http://help.asu.edu/sims/selfhelp/SelfhelpKbView.seam?parature_id=8373-8193-4508&source=Selfhelp&cid=6113");
//        HTMLData htmldata = HTMLParser.returnParsedHTML(htmlContent, "http://help.asu.edu/sims/selfhelp/SelfhelpKbView.seam?parature_id=8373-8193-4508&source=Selfhelp&cid=6113");
//        if (htmldata == null){
//            return;
//        }
//        for(int i =0; i < htmldata.neighbors.size(); i++){
//            System.out.println(htmldata.neighbors.get(i));
//        }
//        System.out.println(htmldata.title);
//        System.out.println("this is description" + htmldata.description);
//        System.out.println("this is text" + htmldata.text);

        List<HTMLData> htmlDataes = new ArrayList<>();
        //1. url -> <word, freq>
        Map<String, Map<String, Integer>> urlIndexWordFrequency = new HashMap<>();
        // inverted index (Search)
        // 2. word -> <url, freq>   =>  [url2, url1, url3]
        Map<String, Map<String, Integer>> wordIndexUrlFrequency = new HashMap<>();
        //3.url-> htmlData
        Map<String, ArrayList<String>> urlIndexHtmlData = new HashMap<>();

        Map<String, List<String>> wordIndexTop10Urls = new HashMap<>();


        Map<String, Integer> listUrlCrawled = new HashMap<>();
        File crawledUrlFile = new File(CRAWLED_URL_FILE);
        if (crawledUrlFile.exists() && !crawledUrlFile.isDirectory()) {
            try {
                Reader myReader = new FileReader(CRAWLED_URL_FILE);
                Gson gson = new Gson();
                Type mapType = new TypeToken<Map<String, Integer>>() {
                }.getType();
                listUrlCrawled = gson.fromJson(myReader, mapType);
            } catch (IOException e) {
                System.out.println("An Error Occurred when READ data from CRAWLED URL FILE!");
                e.printStackTrace();
            }
        } else {
            crawledUrlFile.createNewFile();
        }


        if (listUrlCrawled.size() != 0) {
            int count = 0;
            for (Map.Entry<String, Integer> entryListUrlCrawled : listUrlCrawled.entrySet()) {
                count++;
                if (count < listUrlCrawled.size()) {
                    if (entryListUrlCrawled.getKey().equals(ASU_URL) && entryListUrlCrawled.getValue().equals(MAX_DISTANCE)) {
                        wordIndexTop10Urls = Convert.convertJasonToWordIndexTop10Urls(MAX_DISTANCE);
                        urlIndexHtmlData = Convert.convertJasonToUrlIndexHtmlData(MAX_DISTANCE);
//                    FileWriter myWriter1 = new FileWriter("SecondTime_WordIndexTop10Urls.txt");
//                    for (Map.Entry<String, List<String>> EntryWordIndexTop10Urls : wordIndexTop10Urls.entrySet()) {
//                        myWriter1.write(EntryWordIndexTop10Urls.getKey());
//                        myWriter1.write(System.getProperty("line.separator"));
//                    }
//
//                    myWriter1.close();
//
//                    FileWriter myWriter2 = new FileWriter(("SecondTime_UrlIndexHtmlData.txt"));
//                    for (Map.Entry<String, ArrayList<String>> EntryUrlIndexHtmlData : urlIndexHtmlData.entrySet()) {
//                        myWriter2.write(EntryUrlIndexHtmlData.getKey());
//                        myWriter2.write(System.getProperty("line.separator"));
//
//                    }
//                    myWriter2.close();
                        break;
                    }
                } else {
                    htmlDataes = BFS.bfsForSeed(ASU_URL, MAX_DISTANCE);

                    urlIndexWordFrequency = HTMLDataHandling.getUrlIndexWordFrequency(htmlDataes);
                    wordIndexUrlFrequency = HTMLDataHandling.getWordIndexUrlFrequency(urlIndexWordFrequency);
                    urlIndexHtmlData = HTMLDataHandling.getUrlIndexHtmlData(htmlDataes);
                    wordIndexTop10Urls = HTMLDataHandling.getWordIndexTop10Urls(wordIndexUrlFrequency, PAGE_NUM_URL);

                    Convert.convertToJson(wordIndexTop10Urls, urlIndexHtmlData, MAX_DISTANCE);

                    Gson gson = new Gson();
                    listUrlCrawled.put(ASU_URL, MAX_DISTANCE);
                    String json = gson.toJson(listUrlCrawled);
                    try {
                        FileWriter myWriter = new FileWriter(CRAWLED_URL_FILE);
                        myWriter.write(json);
                        myWriter.close();
                    } catch (IOException e) {
                        System.out.println("An Error Occurred when write dataes into URL CRAWLED FILE!");
                        e.printStackTrace();
                    }

                }
            }
        } else {
            listUrlCrawled = new HashMap<>();
            Gson gson = new Gson();
            listUrlCrawled.put(ASU_URL, MAX_DISTANCE);
            String json = gson.toJson(listUrlCrawled);
            try {
                FileWriter myWriter = new FileWriter(CRAWLED_URL_FILE);
                myWriter.write(json);
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An Error Occurred when write dataes into URL CRAWLED FILE!");
                e.printStackTrace();
            }
            htmlDataes = BFS.bfsForSeed(ASU_URL, MAX_DISTANCE);
            urlIndexWordFrequency = HTMLDataHandling.getUrlIndexWordFrequency(htmlDataes);
            wordIndexUrlFrequency = HTMLDataHandling.getWordIndexUrlFrequency(urlIndexWordFrequency);
            urlIndexHtmlData = HTMLDataHandling.getUrlIndexHtmlData(htmlDataes);
            wordIndexTop10Urls = HTMLDataHandling.getWordIndexTop10Urls(wordIndexUrlFrequency, PAGE_NUM_URL);
            Convert.convertToJson(wordIndexTop10Urls, urlIndexHtmlData, MAX_DISTANCE);

//            FileWriter myWriter1 = new FileWriter("Firstime_WordIndexTop10Urls.txt");
//            for (Map.Entry<String, List<String>> EntryWordIndexTop10Urls : wordIndexTop10Urls.entrySet()) {
//                myWriter1.write(EntryWordIndexTop10Urls.getKey());
//                myWriter1.write(System.getProperty("line.separator"));
//
//            }
//            myWriter1.close();
//            FileWriter myWriter2 = new FileWriter(("FirstTime_UrlIndexHtmlData.txt"));
//            for (Map.Entry<String, ArrayList<String>> EntryUrlIndexHtmlData : urlIndexHtmlData.entrySet()) {
//                myWriter2.write(EntryUrlIndexHtmlData.getKey());
//                myWriter2.write(System.getProperty("line.separator"));
//
//            }
//            myWriter2.close();
        }

        System.out.println("Please enter the word you want to search in this Search Engine!");
        Scanner enteredWord = new Scanner(System.in);
        String searchWord = enteredWord.nextLine();
        List<ArrayList<String>> theResultOfResearchedWord = Search.searchWord(searchWord, urlIndexHtmlData, wordIndexTop10Urls);

        System.out.println("The Word you search is " + searchWord);
        System.out.println("Here is the results of it:");
        for (
                ArrayList<String> EntryTheResultOfResearchedWord : theResultOfResearchedWord) {
            System.out.println(EntryTheResultOfResearchedWord.get(0));
            System.out.println(EntryTheResultOfResearchedWord.get(1));
            System.out.println(EntryTheResultOfResearchedWord.get(2));
            System.out.println();
        }

//        for (HTMLData htmlData : htmlDataes) {
//            System.out.println(htmlData.url);
//            System.out.println(htmlData.title);
//            System.out.println(htmlData.description);
//            System.out.println();
//        }


//        // 1. url -> <word, freq>
//        Map<String, Map<String, Integer>> urlIndexWordFrequency = HTMLDataHandling.getUrlIndexWordFrequency(htmlDataes);


//        // inverted index (Search)
//        // 2. word -> <url, freq>   =>  [url2, url1, url3]
//        Map<String, Map<String, Integer>> wordIndexUrlFrequency = HTMLDataHandling.getWordIndexUrlFrequency(urlIndexWordFrequency);


//        //3.url-> htmlData
//        Map<String, String[]> urlIndexHtmlData = HTMLDataHandling.getUrlIndexHtmlData(htmlDataes);


        //print word, url, frequency
//        for (Map.Entry<String, Map<String, Integer>> invertedIndexEntry : wordIndexUrlFrequency.entrySet()) {
//            System.out.println(invertedIndexEntry.getKey());
//            for (Map.Entry<String, Integer> invertedIndexKeyEntry : invertedIndexEntry.getValue().entrySet()) {
//                System.out.println(invertedIndexKeyEntry.getKey() + " " + invertedIndexKeyEntry.getValue());
//            }
//            System.out.println();
//        }


//        //get the top ten url of the searching word
//        Map<String, List<String>> wordIndexTop10Urls = HTMLDataHandling.getWordIndexTop10Urls(wordIndexUrlFrequency, PAGE_NUM_URL);


//        //print the word and the top ten urls
//        for (Map.Entry<String, List<String>> searchIndexEntry : wordIndexTop10Urls.entrySet()) {
//            System.out.println(searchIndexEntry.getKey());
//            for (String listOfUrl : searchIndexEntry.getValue()) {
//                System.out.println(listOfUrl);
//            }
//            System.out.println();
//        }
//
//        //print the Url and the content of htmlData
//        for (Map.Entry<String, ArrayList<String>> EntryUrlIndexHtmlData : urlIndexHtmlData.entrySet()) {
//            System.out.println(EntryUrlIndexHtmlData.getKey());
//
//            for (String tempString : EntryUrlIndexHtmlData.getValue()) {
//                System.out.println(tempString);
//            }
//            System.out.println();
//
//        }


//        System.out.println("Please enter the word you want to search in this Search Engine!");
//        Scanner enteredWord = new Scanner(System.in);
//        String searchWord = enteredWord.nextLine();
//        //Search method
//        List<String[]> ListUrlContentAfterSearch = Search.searchWord()
    }


}
