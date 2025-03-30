package com.example;

import java.util.*;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.io.*;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ArticleRetriever {
    private static final String BASE_URL = "https://techcrunch.com/latest/";
    private static final int ARTICLES_COUNT = 50;
    private static final int PAUSE_DURATION = 1000; // 1000 ms = 1s 

    public static void runScraper(int article_count, int pause_duration, JTextArea logArea) throws IOException{
        //* Fetch the page HTML */  
        // Need an HTTP client to send the request 
        // --> use WebClient from HtmlUnit
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(true);           // disable JavaScript
        client.getOptions().setJavaScriptEnabled(false);    // --> faster scraping

        // Create base directory 
        File baseDirectory = new File("TechCrunch Articles");
        if(!baseDirectory.exists()) { baseDirectory.mkdir();}
        // Create a subdirectory for unknown categories if there's any
        File otherDirectory = new File(baseDirectory, "Others");
        if(!otherDirectory.exists()) { otherDirectory.mkdir(); }

        String page_url = BASE_URL;      // url of each page 
        int count = 1;

        while (count < article_count && page_url != null) {
            HtmlPage page = client.getPage(page_url);       
            
            //* Parse the HTML document */
            List<HtmlElement> articles = page.getByXPath("//li[contains(@class,'wp-block-post')]");

            //* Fetch article's category, title and url */
            for (HtmlElement article : articles) {
                if (count > article_count) { break; }      // the program can only retrieve 50 articles 
                HtmlAnchor itemURL = article.getFirstByXPath(".//a");
                    String url = itemURL.getHrefAttribute();
                HtmlElement itemTitle = article.getFirstByXPath(".//h3[@class='loop-card__title']");
                    String title = itemTitle.asNormalizedText();
                HtmlElement itemCategory = article.getFirstByXPath(".//a[@class='loop-card__cat']");
                    String category = (itemCategory != null) ? itemCategory.asNormalizedText() : "In Brief";

                // print out to check that 50 articles are retrieved 
                System.out.println(category);
                System.out.println(title);
                System.out.println(url);
                System.out.println("--------------");
                
                // make the directory's name valid 
                String validCategory = category.replaceAll(" ", "").replaceAll("[^a-zA-Z0-9\\\\.\\\\-]","").replaceAll(":", "_");
                File categoryDirectory = createDirectory(baseDirectory, validCategory);       // Create a subdirectory for each category

                //* append article's title after being scraped */
                SwingUtilities.invokeLater(() -> logArea.append("Scraped Article: " + title + "\n"));

                //* Fetch article's text content */
                String content = fetchContent(client, page_url);

                //* Save articles to disk */
                saveArticleToDisk(title, page_url, content, categoryDirectory);

                count++;
                System.out.println(count);

                // Pause between requests 
                try {
                    Thread.sleep(pause_duration);
                } catch (InterruptedException e) { 
                    logArea.append("Scraping interrupted.\n");
                 }

            }
            // to iterate through multiple pages until 50 articles are retrieved 
            HtmlElement nextPageButton = page.getFirstByXPath("//nav[@data-wp-class--force-hide]");
            if (nextPageButton != null) {
                HtmlAnchor nextPageURL = nextPageButton.getFirstByXPath(".//a");
                page_url = (nextPageURL != null) ? nextPageURL.getHrefAttribute() : null;
            } else { page_url = null; }
        }
        client.close();
        logArea.append("Scraping completed.\n");
    }

    private static File createDirectory(File parentDir, String subDirName){
        File directory = new File(parentDir, subDirName);
        if (!directory.exists()) { directory.mkdir(); }
        return directory;
    }

    public static String fetchContent (WebClient client, String url) throws IOException {
        HtmlPage article = client.getPage(url);
        HtmlElement content = article.getFirstByXPath("//*[@div='wp-site-blocks']");

        if(content != null){
            return content.asXml();
        } else {
            HtmlElement body = article.getBody();
            return body.asXml();
        }
    }

    public static void saveArticleToDisk(String title, String url,String content, File directory) throws IOException {
        // Transwer into valid filename
        String filename = title.replaceAll(" ", "").replaceAll("[^a-zA-Z0-9\\\\.\\\\-]","").replaceAll(":", "_");

        // get the specific path to find the folder in the computer easier 
        System.out.println("save article to directory: " + directory.getAbsolutePath());
        System.out.println("Content: " + content);

        // save as html file
        File htmlFile = new File(directory, filename + ".html");
        try (FileWriter writer = new FileWriter(htmlFile)){
            writer.write(content);
        }
    }

}