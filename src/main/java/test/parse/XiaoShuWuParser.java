package test.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;


public class XiaoShuWuParser {

    public static Set<String> keys = new TreeSet();
    public static BufferedWriter logFile;

    public static List<String> keyList = new ArrayList();
    static {
        keyList.add("360网盘密码");
        keyList.add("AZW3格式(推荐)");
        keyList.add("EPUB格式");
        keyList.add("MOBI格式");
        keyList.add("PDF格式");
        keyList.add("城通网盘(备份)");
        keyList.add("天翼云盘(推荐)");
        keyList.add("天翼云盘密码");
        keyList.add("微软云盘");
        keyList.add("百度网盘");
        keyList.add("百度网盘密码");

        try {
            logFile = new BufferedWriter(new FileWriter("/Users/wujing/Workspaces/idea/luckbook/books/pages/xiaoshuwu.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{


       int minPage = 0;
       int maxPage = 30446;

        System.out.println("文件名\t360网盘密码\tAZW3格式(推荐)\tEPUB格式\tMOBI格式\tPDF格式\t城通网盘(备份)\t天翼云盘(推荐)\t天翼云盘密码\t微软云盘\t百度网盘\t百度网盘密码");
        logFile.write("文件名\t360网盘密码\tAZW3格式(推荐)\tEPUB格式\tMOBI格式\tPDF格式\t城通网盘(备份)\t天翼云盘(推荐)\t天翼云盘密码\t微软云盘\t百度网盘\t百度网盘密码");
        logFile.newLine();
        for (int i = minPage; i < maxPage ; i++) {
            parse(i);
        }

        logFile.close();
    }

    public static void parse(int fileIndex) throws Exception {

        File input = new File("/Users/wujing/Workspaces/idea/luckbook/books/pages/xiaoshuwu/"+fileIndex+".html");
        if(input.exists()) {
            Document doc = Jsoup.parse(input, "UTF-8", "http://mebook.cc/");
            String title = doc.title();
            if("title".equals(title)) {
                return;
            }
//            System.out.println(title);

            Map<String, String> bookDetails = new TreeMap();

            String bookName = title;
            String secrets = "";
            Elements pElements = doc.getElementsByTag("p");
            if(pElements != null && pElements.size() > 0) {
                for (Element p: pElements) {
                    String text = p.text();
                    if(text.startsWith("文件名称：")) {
//                        System.out.println(text);
                        bookName = text.replaceFirst("文件名称：", "");
                        if("".equals(bookName.trim()) || "下载页面".equals(bookName)) {
                            return;
                        }
                    }
                    if(text.startsWith("网盘密码：")) {
//                        System.out.println(text);
                        secrets = text.replaceFirst("网盘密码：", "");
                    }
                }
            }

//            bookDetails.put("文件名称",bookName);

            if(secrets != null && !"".equals(secrets)) {
                if(secrets.contains(" ")) {
                    String[] secretList= secrets.split(" ");
                    if(secretList.length > 0) {
                        for (String secret : secretList) {
                            if(secret.contains("：")) {
                                String[] secretSplits= secret.split("：");
                                if(secretSplits.length > 1) {
                                    String s1 = secretSplits[0];
                                    String s2 = secretSplits[1];
                                    bookDetails.put(s1, s2);
                                    keys.add(s1);

                                }
                            }
                        }
                    }
                }
                else if(secrets.contains("：")) {
                    String[] secretSplits= secrets.split("：");
                    if(secretSplits.length > 1) {
                        String s1 = secretSplits[0];
                        String s2 = secretSplits[1];
                        bookDetails.put(s1, s2);
                        keys.add(s1);
                    }
                }
            }

            Elements listElements =  doc.getElementsByClass("list");
            if(listElements != null && listElements.size() > 0) {
                Element listElement = listElements.get(0);
                Elements links = listElement.children();
                if(links != null && links.size() > 0) {
                    for (Element link: links) {
                        if("a".equals(link.tagName())) {
                            String href = link.attr("href");
                            String text = link.text();
//                            System.out.println(text + " : " + href);
                            bookDetails.put(text,href);
                            keys.add(text);

                        }
                    }
                }
            }

            System.out.print(bookName + "\t");
            logFile.write(bookName + "\t");

            for (String key: keyList) {
                String value = "";
                if(bookDetails.containsKey(key)) {
                    value = bookDetails.get(key);
                }
                System.out.print(value + "\t");
                logFile.write(value + "\t");

            }
            System.out.println();
            logFile.newLine();
        }
    }
}
