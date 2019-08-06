package test.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;


public class XiaoShuWuParser {

    public static void main(String[] args) throws Exception{
       int minPage = 0;
       int maxPage = 30446;

        for (int i = minPage; i < maxPage ; i++) {
            parse(i);
        }
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
                        }
                    }
                }
            }

            System.out.println("文件编号 : " + fileIndex);
            System.out.println("文件名称" +" : "+  bookName);
            Iterator<String> keyIterator = bookDetails.keySet().iterator();
            while(keyIterator.hasNext()) {
                String key = keyIterator.next();
                String value = bookDetails.get(key);
                System.out.println(key +" : "+ value);
            }
        }
    }
}
