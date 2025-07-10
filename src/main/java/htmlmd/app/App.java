package htmlmd.app;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.jsoup.Jsoup;
import java.io.*;

public class App {
    // private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        File markdownFile;
        File htmlFile;
        boolean htmlToMd = true;

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
                    markdownFile = new File(args[i+1]);
                    htmlToMd = false;
                    // System.out.println("input file is " + markdownFile.getName());
                } else if (args[i+1].endsWith(".html")) {
                    htmlFile = new File(args[i+1]);
                    htmlToMd = true;
                    // System.out.println("input file is " + htmlFile.getName());
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
                    markdownFile = new File(args[i+1]);
                    if (!htmlToMd) {
                        System.out.println("error: can't convert from markdown to markdown");
                        return;
                    }
                    // System.out.println("output file is " + markdownFile.getName());
                } else if (args[i+1].endsWith(".html")) {
                    htmlFile = new File(args[i+1]);
                    if (htmlToMd) {
                        System.out.println("error: can't convert from html to html");
                        return;
                    }
                    // System.out.println("output file is " + htmlFile.getName());
                }

            }

            System.out.print(args[i] + " ");
        }
        System.out.println("");




    }
}
