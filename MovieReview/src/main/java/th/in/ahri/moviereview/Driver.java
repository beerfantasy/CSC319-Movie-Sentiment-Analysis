package th.in.ahri.moviereview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
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

    private static final List<String> IGNORE_LIST = Arrays.asList("'s", ",", ".", "--", "", " ");
    private static WordTable wordTable = new WordTable(2000);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:words.sqlite");
            Statement stmt = connection.createStatement();
            stmt.setQueryTimeout(30);
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `words` (word string, score int, appearance int)");
            ResultSet rs = stmt.executeQuery("SELECT * FROM `words`");
            while(rs.next()) {
                wordTable.addExisting(rs.getString(1), rs.getInt(2), rs.getInt(3));
            }

            System.out.print("Enter file name to load data from (\\s to skip): ");
            String in = scanner.nextLine();
            if(!in.equalsIgnoreCase("\\s")) {
                List<String> lines = readLines(in);
                for (String s : lines) {
                    System.out.print("\rProcessing : " + s);
                    StringTokenizer tokenizer = new StringTokenizer(s, " ");
                    String token;
                    int score = Integer.parseInt(tokenizer.nextToken());
                    while (tokenizer.hasMoreTokens()) {
                        token = tokenizer.nextToken().trim();
                        if (!IGNORE_LIST.contains(token)) {
                            wordTable.put(token, score);
                        }
                    }
                }

                // Only update the DB if we actually loaded a new data source.

                // Prepare statement to prevent SQL Injection
                List<WordEntry> words = wordTable.getReadOnlyWordList();
                float percent = 0;
                int count = 0;

                System.out.println("\nSaving data to database...");
                connection.setAutoCommit(false);
                PreparedStatement pstmt = connection.prepareStatement("REPLACE INTO `words` VALUES (?,?,?)");
                for(WordEntry wordEntry : words) {
                    ++count;
                    percent = count / (float)words.size() * 100;
                    System.out.printf("\rSaving " + wordEntry.getWord() + "... [%.2f%%]", percent);
                    pstmt.setString(1, wordEntry.getWord());
                    pstmt.setInt(2, wordEntry.getTotalScore());
                    pstmt.setInt(3, wordEntry.getNumAppearance());
                    pstmt.executeUpdate();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading data source.");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid score format.");
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getSQLState());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.exit(1);
        } finally {
            try {
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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
                totalScore += wordTable.getAverage(token);
                ++wordCount;
            }
        }
        return totalScore/wordCount;
    }
}
