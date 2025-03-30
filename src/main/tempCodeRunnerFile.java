package com.example;
import javax.swing.*;
import javax.swing.border.Border;

//import com.fasterxml.jackson.core.JsonGenerationException;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * GUI interface for the Article Retriever
 */

public class ArticleRetrieverGUI {
    public static void main(String[] args) {
        //main frame
        JFrame frame = new JFrame("TechCrunch Scraper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,300);
        frame.setLayout(new BorderLayout());

        // input panel 
        JPanel inputPanel = new JPanel(new GridLayout(3,2));

        inputPanel.add(new JLabel("Number of articles to be scraped: "));
        JTextField articleCountField = new JTextField("50");
        inputPanel.add(articleCountField);

        inputPanel.add(new JLabel("Pause duration (ms): "));
        JTextField pauseDurationField = new JTextField("1000");
        inputPanel.add(pauseDurationField);   

        JButton startButton = new JButton("Start Scraping");
        JButton stopButton = new JButton("Stop Scraping");
        inputPanel.add(stopButton);
        inputPanel.add(startButton);

        // log panel
        JTextArea lgoArea = new JTextArea();
        lgoArea.setEditable(false);
        JScrollPane logJScrollPane = new JScrollPane(lgoArea);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(logJScrollPane, BorderLayout.CENTER);

        // start button action
        startButton.addActionListener(e -> {
            int articleCount;
            int pauseDuration;
            try{
                articleCount = Integer.parseInt(articleCountField.getText());
                pauseDuration = Integer.parseInt(pauseDurationField.getText());
            } catch (NumberFormatException ex) {
                lgoArea.append("Invalid input! Please enter numeric values.\n");
            }
            lgoArea.append("Scrapering...\n");
            // run articleRetriever in seperate thread 
            new Thread (() -> {
                try{
                    ArticleRetriever.runScraper(articleCount, pauseDuration, lgoArea);
                } catch (IOException ex) {
                    lgoArea.append("Error: " + ex.getMessage() + "\n");
                }
            }).start();
        });

        stopButton.addActionListener(e -> lgoArea.append("Stopped!"));

        // show frame
        frame.setVisible(true);
    }
}