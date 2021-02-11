
import java.util.*;

public class UrlContentPreWork {

    public static Map<String, Integer> urlContentPreWork(HTMLData htmlData) {
        ArrayList<String> titleToken = new ArrayList<String>();
        ArrayList<String> descriptionToken = new ArrayList<String>();
        ArrayList<String> textToken = new ArrayList<String>();

        String delim = " \n\r\t,.;:-";
        StringTokenizer st1 = new StringTokenizer(htmlData.title, delim);
        while (st1.hasMoreTokens()) {
            titleToken.add(st1.nextToken().toLowerCase());
        }

        StringTokenizer st2 = new StringTokenizer(htmlData.description, delim);
        while (st2.hasMoreTokens()) {
            descriptionToken.add(st2.nextToken().toLowerCase());
        }
        StringTokenizer st3 = new StringTokenizer(htmlData.text, delim);
        while (st3.hasMoreTokens()) {
            textToken.add(st3.nextToken().toLowerCase());
        }

        Map<String, Integer> urlContentMap = new HashMap<>();

        urlContentMap = frequencyAdder(urlContentMap, titleToken, 3);
        urlContentMap = frequencyAdder(urlContentMap, descriptionToken, 2);
        urlContentMap = frequencyAdder(urlContentMap, textToken, 1);


        return urlContentMap;

    }


    private static Map<String, Integer> frequencyAdder(Map<String, Integer> map, ArrayList<String> token, Integer factor) {
        for (String currentString : token) {
            Integer currentFrequency = factor;
            boolean equalDecider = false;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getKey().equals(currentString)) {
                    currentFrequency = entry.getValue() + factor;
                    equalDecider = true;
                }
            }

            if (equalDecider) {
                map.replace(currentString, currentFrequency);
            } else {
                map.put(currentString, currentFrequency);
            }
        }
        return map;
    }
}
