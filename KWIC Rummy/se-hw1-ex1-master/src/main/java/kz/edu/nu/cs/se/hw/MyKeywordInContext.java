package kz.edu.nu.cs.se.hw;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyKeywordInContext implements KeywordInContext {

    private String name, path;
    private ArrayList<MyIndexable> indexedStrings;
    private ArrayList<String> store;

    public MyKeywordInContext(String name, String pathstring) {
        // TODO Auto-generated constructor stub
        this.name = name;
        this.path = pathstring;
        indexedStrings = new ArrayList<>();
        store = new ArrayList<>();
    }

    @Override
    public int find(String word) {
        String word2 = word.toLowerCase();
        for (int i = 0; i < indexedStrings.size(); i++) {
            if (indexedStrings.get(i).input.equals(word2)) return i;
        }
        return -1;
    }

    @Override
    public Indexable get(int i) {
        return indexedStrings.get(i);
    }

    @Override
    public void txt2html() {
        try {
            OutputStream newFile = new FileOutputStream(new File(name + ".html"));
            BufferedReader text = new BufferedReader(new FileReader(path));
            PrintStream out = new PrintStream(newFile);

            String inputText;
            while ((inputText = text.readLine()) != null) store.add(inputText);

            String start = "<!DOCTYPE html>";
            String start2 = "<html><head><title>Converted text to html</title><meta charset='UTF-8'></head><body>";
            String end = "</body></html>";

            for (int i = 0; i < store.size(); i++) {
                if (i == 0) {
                    out.println(start);
                    out.println(start2);
                    out.println("<div>");
                    out.println(store.get(0) + "<span id='line_" + (i+1) + "'>[&nbsp&nbsp" + (i + 1) + "]</span>" + "<br>");
                } else if (store.size() == i + 1) {
                    out.println(store.get(i) + "<span id='line_" + (i+1) + "'>[&nbsp&nbsp" + (i + 1) + "]</span>");
                    out.println("</div>" + end);
                } else out.println(store.get(i) + "<span id='line_" + (i+1) + "'>[&nbsp&nbsp" + (i + 1) + "]</span>" + "<br>");
            }
            text.close();
            out.close();
            newFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashSet<String> stopWords() {
        HashSet<String> words = new HashSet<>();
        try {
            for (int i = 0; i < indexedStrings.size(); i++)
                if (indexedStrings.get(i).input.length() > 3) words.add(indexedStrings.get(i).input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }

    @Override
    public void indexLines() {
        // TODO Auto-generated method stub
        HashSet<String> stopWords = stopWords();
        for (int i = 0; i < store.size(); i++) {
            Pattern pattern = Pattern.compile("([0-9a-zA-Z]+)");
            Matcher match = pattern.matcher(store.get(i));
            ArrayList<String> inputs = new ArrayList<>();
            while (match.find()) if (match.group().length()>1) inputs.add(match.group());
            HashMap<String, Integer> numbers = new HashMap<>();
            for (int j = 0; j < inputs.size(); j++) {
                int count = 0;
                String item = inputs.get(j);
                if (numbers.containsKey(item.toLowerCase())) count = numbers.get(item.toLowerCase());
                count++;
                numbers.put(item.toLowerCase(), count);
                if (!stopWords.contains(item)) {
                    MyIndexable getPair = new MyIndexable(item.toLowerCase(), i+1, count);
                    indexedStrings.add(getPair);
                }
            }
        }
        Collections.sort(indexedStrings);
    }

    @Override
    public void writeIndexToFile() {
        // TODO Auto-generated method stub
        try {
            OutputStream out = new FileOutputStream(new File("kwic-" + name + ".html"));
            PrintStream print = new PrintStream(out);

            String start = "<html><head><meta charset=\"UTF-8\"></head><body><div style=\"text-align:center;line-height:1.6\">";
            String end = "</div></body></html>";

            print.println(start);

            for (int i = 0; i < indexedStrings.size(); i++) {
                int j, count = 0;
                MyIndexable getPair = indexedStrings.get(i);
                int index = getPair.getInd();
                String row = store.get(getPair.getLineNumber()-1);
                String word = getPair.getEntry();

                for (j = 0; j <= row.length()-word.length(); j++) {
                    if (row.substring(j, j+word.length()).toLowerCase().equals(word)) {
                        count++;
                        if (index == count) {
                            print.print("<a href=\"" + name  + ".html#line_" + getPair.getLineNumber() +"\">" + word.toUpperCase() +"</a>");
                            j += word.length()-1;
                        } else print.print(row.charAt(j));
                    } else print.print(row.charAt(j));
                }
                for (;j < row.length();j++) print.print(row.charAt(j));
                print.println("<br>");
            }
            print.close();
            print.println(end);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
