package xiaoshuwu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;

public class PageClear {
    public static void main(String[] args) throws Exception {
        String path = "E:/Workspaces/Idea/luckbook/src/test/xiaoshuwu/";
        File parentFile = new File(path);
        if(parentFile.exists()) {
            String[] fileNames = parentFile.list();
            if(fileNames.length > 0) {
                for (int i = 0; i < fileNames.length; i++) {
                    try {
                        File input = new File("E:/Workspaces/Idea/luckbook/src/test/xiaoshuwu/"+i+".html");
                        if(input.exists()) {
                            Document doc = Jsoup.parse(input, "UTF-8", "http://mebook.cc/");
                            String title = doc.title();
                            System.out.println(i + " : " + title);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }
}
