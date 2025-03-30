ArticleRetriever Project
-----------------------
## Objectives: 
The goal of this project is to retrieve the latest 50 articles from TechCrunch and saves them to disk. The program is designed to: 
- Scrape article data from the TechCrunch website.
- Organize articles into folders based on their categories/field.
- Save the article's title, URL and content into a text file. 


## Tools and Libraries used:
- Programming language: Java.
- Libraries: HtmlUnit.

## Instructions (if you decide to download the zip file):
- Prerequisites: Have Java and HtmlUnit library installed (if not using Maven) 
- Extract the .zip file.
- Open the project in an IDE or compile and run it from the command line. 
- Run the `ArticleRetriever.java` file.
- The program will create a TechCrunch Articles folder in the project directory and save the retrieved articles into subfolders based on their categories. 

## Notes for Local Execution
If you decide to download and run the program in your local IDE or command line, the console will display detailed logs to help track the progress, which include the category, title, URL and count of each article as it's fetched. 

## Known Issues
Content fetching failure: The program was not able to fetch and save the specific content (paragraphs) due to issues (which I believe) regarding: 
1. Line 88: The content's HTML path is incorrect, leading to failure in fetching the content.
2. Line 112: FileWriter function is not overwriting the text files as expected. 
Thus, only the title and URL are currently being written to the text files. 

