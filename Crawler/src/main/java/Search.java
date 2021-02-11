import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Search {
    //Search method
    static List<ArrayList<String>> searchWord(String SearchWord, Map<String, ArrayList<String>> urlIndexHtmlData, Map<String, List<String>> wordIndexTop10Urls) {

        List<ArrayList<String>> returnStringList = new ArrayList<>();
        for (Map.Entry<String, List<String>> EntryOfWordIndexTop10Urls : wordIndexTop10Urls.entrySet()) {
            if (EntryOfWordIndexTop10Urls.getKey().equals(SearchWord)) {
                List<String> tempUrls = EntryOfWordIndexTop10Urls.getValue();
                for (String tempString : tempUrls) {
                    for(Map.Entry<String, ArrayList<String>> EntryUrlIndexHtmlData :urlIndexHtmlData.entrySet()){
                        if (EntryUrlIndexHtmlData.getKey().equals(tempString)){
                            returnStringList.add(EntryUrlIndexHtmlData.getValue());
                        }
                    }
                }

                break;
            }
        }
        return returnStringList;
    }
}
