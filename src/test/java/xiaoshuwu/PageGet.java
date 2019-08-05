package xiaoshuwu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class PageGet {

    final static int minPage = 0;
    final static int maxPage = 30446;
    final static String storeFilePattern = "E:/Workspaces/Idea/luckbook/src/test/xiaoshuwu/%d.html";
    final static String downloadPagePattern = "http://mebook.cc/download.php?id=%d";

    public static void main(String[] args) throws Exception {

        int threadPages = 5;
        int threadPageSize = maxPage / threadPages + (maxPage % threadPages > 0 ? 1: 0);

        for (int i = 0; i < threadPages ; i++) {
            int currentMinPage = threadPageSize * i;
            int currentMaxPage = (threadPageSize) * (i + 1) - 1;
            if(currentMaxPage > maxPage) {
                currentMaxPage = maxPage;
            }
            System.out.println("GET PAGE: " + currentMinPage + " -> " + currentMaxPage);
            new Process(i, currentMinPage, currentMaxPage).start();
        }
    }

    private static void storeUrl2Path(String downloadUrl, String storeFilePath) {
        File file = new File(storeFilePath);
        if(!file.exists()) {
            try{
                URLConnection conn = new URL(downloadUrl).openConnection();
                conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");
                InputStream is = conn.getInputStream();
                FileOutputStream out = new FileOutputStream(file);
                int a=0;
                while((a = is.read()) != -1){
                    out.write(a);
                }
                is.close();
                out.close();
            }catch(Exception e){
                System.out.println(e);
            }

        }
    }

    static class Process extends Thread{
        private int threadId;
        private int minPage;
        private int maxPage;
        public Process(int threadId, int minPage, int maxPage){
            this.threadId = threadId;
            this.minPage = minPage;
            this.maxPage = maxPage;
        }
        public void run() {
            int length = maxPage - minPage;
            for (int i = minPage; i <= maxPage ; i++) {
                int process = (i - minPage) * 100 / length;
                System.out.println("THREAD "+threadId+" -> PROCESS: "+process+"%, PAGE: " + i);
                String storeFilePath = String.format(storeFilePattern, i);
                String downloadUrl = String.format(downloadPagePattern, i);
                storeUrl2Path(downloadUrl, storeFilePath);
            }
        }
    }
}
