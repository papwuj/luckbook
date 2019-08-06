package test.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

public class PageClear {
    public static void main(String[] args) throws Exception {
        String path = "/Users/wujing/Workspaces/idea/luckbook/books/pages/xiaoshuwu/";
        File parentFile = new File(path);
        if(parentFile.exists()) {
            String[] fileNames = parentFile.list();
            if(fileNames.length > 0) {
                for (int i = 0; i < fileNames.length; i++) {
                    try {
                        File input = new File("/Users/wujing/Workspaces/idea/luckbook/books/pages/xiaoshuwu/"+i+".html");
                        if(input.exists()) {

                            Document doc = Jsoup.parse(input, "UTF-8", "http://mebook.cc/");
                            String title = doc.title();

                            System.out.println(i + " : " + title);

                            if("下载页面".equals(title)) {
                                input.delete();
                            }

                            boolean hasDownloadLink = false;
                            Elements listElements =  doc.getElementsByClass("list");
                            if(listElements != null && listElements.size() > 0) {
                                Element listElement = listElements.get(0);
                                Elements links = listElement.children();
                                if(links != null && links.size() > 0) {
                                    for (Element link: links) {
                                        if("a".equals(link.tagName())) {
                                            hasDownloadLink = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if(!hasDownloadLink) {
                                input.delete();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }
}
