package th.in.ahri.moviereview;

import org.omg.CORBA.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * MovieReview
 *
 * @author Jittapan
 */
public class Driver {

    private static final List<String> IGNORE_LIST = Arrays.asList("'s", ",", ".", "--");
    private static HashTable hashTable = new HashTable(2000);

    public static void main(String[] args) {
        try {
            List<String> lines = readLines("movieReviews.txt");
            for(String s : lines) {
                System.out.print("\rProcessing : " + s);
                StringTokenizer tokenizer = new StringTokenizer(s, " ");
                String token;
                int score = Integer.parseInt(tokenizer.nextToken());
                while(tokenizer.hasMoreTokens()) {
                    token = tokenizer.nextToken().trim();
                    if(!IGNORE_LIST.contains(token)) {
                        hashTable.put(token, score);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading data source.");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid score format.");
            System.exit(1);
        }

        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("\nInput review : ");
            String review = scanner.nextLine();
            System.out.println();
            System.out.println("Score : " + processLine(review));
        }
    }

    private static List<String> readLines(String file) throws IOException {
        return Files.readAllLines(Paths.get(file));
    }

    private static double processLine(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, " ");
        double totalScore = 0;
        int wordCount = 0;
        while(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if(!IGNORE_LIST.contains(token)) {
                totalScore += hashTable.getAverage(token);
                ++wordCount;
            }
        }
        return totalScore/wordCount;
    }
}
