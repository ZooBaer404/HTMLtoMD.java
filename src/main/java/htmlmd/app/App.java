package htmlmd.app;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Scanner;
import java.util.List;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Node;
import java.io.*;

public class App {
    // private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
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
        }

        try {
            inputFileReader = new FileReader(inputFile);
            outputFileReader = new FileReader(outputFile);
        } catch (FileNotFoundException ex) {
            ex.getMessage();
        }

        System.out.println("Done!");
        System.out.println("Parsing input file...");

        StringBuilder inputText = new StringBuilder();
        // HtmlTreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        Parser htmlParser = Parser.htmlParser();

        List<Node> nodeList = htmlParser.parseFragmentInput(inputFileReader, null, "");

        for (Node node : nodeList) {
            System.out.println(node.nodeName());
        }



        System.out.println("Done!");
        System.out.println("Saving output file...");

        System.out.println("Done!");
    }
}
