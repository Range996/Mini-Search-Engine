package com.example.minigoogle;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.swing.plaf.multi.MultiFileChooserUI;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private static String CRAWLED_JSON_PATH = "/WEB-INF/";
    private String message;
    private static Map<String, List<String>> wordIndexTop10Urls;
    //3.url-> htmlData
    private static Map<String, ArrayList<String>> urlIndexHtmlData;

    private static Map<Integer, Boolean> theLargestDistance = new HashMap<>();

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyword = request.getParameter("keyword");
        populateMapOnce();
//      response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.append(search(keyword));
//        PrintWriter giao = new PrintWriter(System.out);
//        giao.println(keyword);
//        giao.flush();
    }

    public void destroy() {
    }

    private void populateMapOnce() {
        System.out.println("gg");
        if (wordIndexTop10Urls == null) {
            //constructMap by reading from file
            Gson gson = new Gson();
            wordIndexTop10Urls = new HashMap<>();
            urlIndexHtmlData = new HashMap<>();
            for (int i = 1; i < 11; i++) {
                String path = CRAWLED_JSON_PATH;
                String fileName = "CRAWLED JSON FILE " + i +".txt";
                if (getServletContext().getResourceAsStream(path+fileName) !=null) {
                    theLargestDistance.put(i, true);
                }
            }
            Integer maxInt = Collections.max(theLargestDistance.keySet());
            try {
                String json = readFromResource(CRAWLED_JSON_PATH + "CRAWLED JSON FILE " + maxInt + ".txt");
                JsonArray array = JsonParser.parseString(json).getAsJsonArray();
                wordIndexTop10Urls = gson.fromJson(array.get(0), Map.class);
                urlIndexHtmlData = gson.fromJson(array.get(1), Map.class);

            } catch (IOException e) {
                System.out.println("An Error Occurred when READ WordAndListUrls FROM CRAWLED JSON FILE!");
                e.printStackTrace();
            }
        }
    }

    // Return json string of the List<String[]> = 10 urls with tile and description
    private static String search(String keyword) throws IOException {

        List<ArrayList<String>> listUrlContent = new ArrayList<>();
        List<String> listUrls = new ArrayList<>();
        for (Map.Entry<String, List<String>> entryWordIndexTop10Urls : wordIndexTop10Urls.entrySet()) {
            if (entryWordIndexTop10Urls.getKey().equals(keyword)) {
                listUrls = entryWordIndexTop10Urls.getValue();
            }
        }

        for (String urlLink : listUrls) {
            ArrayList<String> listContent = new ArrayList<>();
            for (Map.Entry<String, ArrayList<String>> entryUrlIndexHtmlData : urlIndexHtmlData.entrySet()) {
                if (urlLink.equals(entryUrlIndexHtmlData.getKey())) {
                    for (String urlContent : entryUrlIndexHtmlData.getValue()) {
                        listContent.add(urlContent);
                    }
                    listUrlContent.add(listContent);
                }
            }
        }
        Gson gson = new Gson();
        Type tokenType = new TypeToken<List<ArrayList>>() {
        }.getType();
        String json = gson.toJson(listUrlContent, tokenType);
        return json;
    }


    private String readFromResource(String fileName) throws IOException {
        StringBuffer content = new StringBuffer();
        InputStream is = getServletContext().getResourceAsStream(fileName);
        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String text;
            while ((text = reader.readLine()) != null) {
                content.append(text);
            }
            reader.close();
        }
        return content.toString();
    }
}

