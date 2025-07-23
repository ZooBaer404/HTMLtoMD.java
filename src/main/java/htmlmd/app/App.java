package htmlmd.app;

import java.nio.file.Files;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.commonmark.*;
import java.io.*;

public class App {
    // private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws IOException {
        File inputFile = null;
        File outputFile = null;
        Reader inputFileReader = null;
        Reader outputFileReader = null;
        boolean htmlToMd = true;

        System.out.println("Parsing arguments...");
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--input")) {
                if (args.length == i) {
                    System.out.println("invalid length of arguments!!!");
                    return;
                }

                // if (!args[i+1].endsWith("md") || !args[i+1].endsWith("html")) {
                //     System.out.println("invalid argument for --input: " + args[i+1]);
                //     return;
                // }

                if (args[i+1].endsWith(".md")) {
                    inputFile = new File(args[i+1]);
                    htmlToMd = false;
                    System.out.println("input file is " + inputFile.getName());
                } else if (args[i+1].endsWith(".html")) {
                    inputFile = new File(args[i+1]);
                    htmlToMd = true;
                    System.out.println("input file is " + inputFile.getName());
                }
            } else if (args[i].equals("--output")) {
                if (args.length == i) {
                    System.out.println("invalid length of arguments!!!");
                    return;
                }

                // if (!args[i+1].endsWith(".md") || !args[i+1].endsWith(".html")) {
                //     System.out.println("invalid argument for --output: " + args[i+1]);
                //     return;
                // }

                if (args[i+1].endsWith(".md")) {
                    htmlToMd = true;
                    outputFile = new File(args[i+1]);
                    if (!htmlToMd) {
                        System.out.println("error: can't convert from markdown to markdown " + args[i+1]);
                        return;
                    }
                    // System.out.println("output file is " + markdownFile.getName());
                } else if (args[i+1].endsWith(".html")) {
                    htmlToMd = false;
                    outputFile = new File(args[i+1]);
                    if (htmlToMd) {
                        System.out.println("error: can't convert from html to html " + args[i+1]);
                        return;
                    }
                    // System.out.println("output file is " + htmlFile.getName());
                }
            }

            System.out.print(args[i] + " ");
        }
        System.out.println("");

        if (inputFile == null) {
            System.out.println("error: input file not specified!!");
            return;
        }

        if (outputFile == null) {
            String inputFileName = inputFile.getName();
            String autoOutputFile = inputFileName.substring(0, inputFileName.lastIndexOf(".")).concat(((htmlToMd) ? ".md" : ".html"));
            System.out.println("info: output file is automatically set to " + autoOutputFile);
            outputFile = new File(autoOutputFile);
        }

        assert outputFile != null;

        try {
            inputFileReader = new FileReader(inputFile);
            outputFileReader = new FileReader(outputFile);
        } catch (FileNotFoundException ex) {
            ex.getMessage();
        }

        System.out.println("input file is: " + inputFile.getName());
        System.out.println("output file is: " + outputFile.getName());


        System.out.println("Done!");
        System.out.println("Parsing input file...");

        StringBuilder inputText = new StringBuilder();
        // HtmlTreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        Parser htmlParser = Parser.htmlParser();

        assert inputFileReader != null;
        Document document = htmlParser.parseInput(inputFileReader, "");

        StringBuilder finalOutputMD = new StringBuilder();
        StringBuilder finalOutputHTML = new StringBuilder();


        if (htmlToMd) {

            Element body = document.body();
            ArrayList<Element> bodyChildren = body.getAllElements().asList();

            // Handle nested tags like <bold> in <p>

            for (Element node : bodyChildren) {
                System.out.println(node.getAllElements().asList());

                switch (node.nodeName()) {
                case "h1":
                    finalOutputMD.append("# ");
                    finalOutputMD.append(node.ownText());
                    finalOutputMD.append("\n");
                    break;
                case "h2":
                    finalOutputMD.append("## ");
                    finalOutputMD.append(node.ownText());
                    finalOutputMD.append("\n");
                    break;
                case "h3":
                    finalOutputMD.append("### ");
                    finalOutputMD.append(node.ownText());
                    finalOutputMD.append("\n");
                    break;
                case "h4":
                    finalOutputMD.append("#### ");
                    finalOutputMD.append(node.ownText());
                    finalOutputMD.append("\n");
                    break;
                case "h5":
                    finalOutputMD.append("##### ");
                    finalOutputMD.append(node.ownText());
                    finalOutputMD.append("\n");
                    break;
                case "img":
                    if (!node.attribute("src").getValue().equals("")) {
                        finalOutputMD.append("![" + node.attribute("alt").getValue() + "](" + node.attribute("src").getValue()  + ")");
                    } else {
                        System.out.println("image data is empty!");
                    }
                    break;
                case "a":
                    finalOutputMD.append("[" + node.ownText() + "]" + "(" + node.attribute("href").getValue() + ")");
                    break;
                case "p":
                    finalOutputMD.append(node.ownText());
                    finalOutputMD.append("\n");
                    break;
                    // case "bold":
                    //     finalOutputMD.append("**");
                    //     finalOutputMD.append(node.ownText());
                    //     finalOutputMD.append("**");
                    //     break;
                    // case "i":
                    //     finalOutputMD.append("_");
                    //     finalOutputMD.append(node.ownText());
                    //     finalOutputMD.append("_");
                    //     break;
                }

                System.out.println(finalOutputMD);
            }

        } else {
            StringBuilder contentBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(inputFileReader)) {
                String line;
                while ((line = br.readLine()) != null) {
                    contentBuilder.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
	            throw new RuntimeException(e);
            }
	        String finalInputMD = contentBuilder.toString();
            finalOutputHTML = new StringBuilder(convertMarkdownToHTML(finalInputMD));
        }

        System.out.println("Done!");
        System.out.println("Saving output file...");

        if (htmlToMd) {
            FileWriter fw = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(String.valueOf(finalOutputMD));
            bw.close();
        } else {
            FileWriter fw = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(String.valueOf(finalOutputHTML));
            bw.close();
        }

        System.out.println("Done!");
    }

    public static String convertMarkdownToHTML(String markdownInput) {
        org.commonmark.parser.Parser parser = org.commonmark.parser.Parser.builder().build();
        org.commonmark.node.Node document = parser.parse(markdownInput);
        org.commonmark.renderer.html.HtmlRenderer htmlRenderer = org.commonmark.renderer.html.HtmlRenderer.builder().build();
        return htmlRenderer.render(document);
    }

}
