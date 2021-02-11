import java.util.*;

public class HTMLDataHandling {
    // 1. url -> <word, freq>
    static Map<String, Map<String, Integer>> getUrlIndexWordFrequency (List<HTMLData> htmlDataes){
        Map<String, Map<String, Integer>> urlIndexWordFrequency = new HashMap<>();
        for (HTMLData htmlData : htmlDataes) {
            String url = htmlData.url;
            Map<String, Integer> tokenizeddWords = UrlContentPreWork.urlContentPreWork(htmlData);
            urlIndexWordFrequency.put(url, tokenizeddWords);
        }
        return urlIndexWordFrequency;
    }


    // 2. word -> <url, freq>   =>  [url2, url1, url3]
    static Map<String, Map<String, Integer>> getWordIndexUrlFrequency(Map<String, Map<String, Integer>> urlIndexWordFrequency){
        Map<String, Map<String, Integer>> wordIndexUrlFrequency = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> indexEntry : urlIndexWordFrequency.entrySet()) {
            String url = indexEntry.getKey();
            Map<String, Integer> wordAndFrequency = indexEntry.getValue();
            for (Map.Entry<String, Integer> entry : wordAndFrequency.entrySet()) {
                String word = entry.getKey();
                Integer frequency = entry.getValue();

                //get the value of the Mapping word in invertedIndex
                Map<String, Integer> tempMap;
                tempMap = getInvertedIndexUrlAndFrequency(wordIndexUrlFrequency, word);

                //insert the value:url and frequency, of the key into the invertedIndex

                tempMap.put(url, frequency);
                wordIndexUrlFrequency.put(word, tempMap);

            }

        }
        return wordIndexUrlFrequency;
    }


    //3.url-> htmlData
    static Map<String, ArrayList<String>> getUrlIndexHtmlData(List<HTMLData> htmlDataes){
        Map<String, ArrayList<String>> urlIndexHtmlData = new HashMap<>();
        for(HTMLData htmlData : htmlDataes){
            ArrayList<String> tempString = new ArrayList<>();
                tempString.add(htmlData.url);
                tempString.add(htmlData.title);
                tempString.add(htmlData.description);
            urlIndexHtmlData.put(htmlData.url, tempString);
        }
        return urlIndexHtmlData;
    }



    //get the top ten url of the searching word
    static Map<String, List<String>> getWordIndexTop10Urls(Map<String, Map<String, Integer>> wordIndexUrlFrequency, int PAGE_NUM_URL){
        Map<String, List<String>> wordIndexTop10Urls = new HashMap<>();
        for(Map.Entry<String, Map<String, Integer>> invertedIndexEntry : wordIndexUrlFrequency.entrySet()){
            String currentString = invertedIndexEntry.getKey();
            Map<String,Integer> currentUrlMap = invertedIndexEntry.getValue();
            List<String> listOfUrl = sortByFrequency(currentUrlMap);
            if (listOfUrl.size() < PAGE_NUM_URL+1){
                wordIndexTop10Urls.put(currentString,listOfUrl);
            }
            else{
                List<String> top10ListOfUrl = new ArrayList<>();
                for (int i = 0; i < PAGE_NUM_URL; i++){
                    top10ListOfUrl.add(listOfUrl.get(i));
                }
                wordIndexTop10Urls.put(currentString, top10ListOfUrl);
            }
        }
        return wordIndexTop10Urls;
    }

    //confirm there a word that has already existed in the invertedIndex
    private static Map<String, Integer> getInvertedIndexUrlAndFrequency(Map<String, Map<String, Integer>> invertedIndex, String word) {
        Map<String, Integer> tempMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> invertedIndexEntry : invertedIndex.entrySet()) {
            if (invertedIndexEntry.getKey().equals(word)) {
                tempMap = invertedIndexEntry.getValue();
                return tempMap;
            }
        }
        return tempMap;
    }


    //sort the urls based on the frequency
    private static List<String> sortByFrequency(Map<String, Integer> invertedIndexValueMap){
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(invertedIndexValueMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String,Integer>>(){
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String,Integer> o2){
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        List<String> temp = new ArrayList<>();
        for (Map.Entry<String, Integer> entry: list){
            temp.add(entry.getKey());
        }

        return temp;
    }
}
