package th.in.ahri.moviereview;

/**
 * Represents a word entry
 *
 * @author Jittapan
 */
public class WordEntry {
    private String text;
    private int numAppearance;
    private int totalScore;

    public WordEntry(String text, int score) {
        this.text = text;
        this.totalScore = score;
        this.numAppearance = 1;
    }

    public void addNewAppearance(int s) {
        ++numAppearance;
        totalScore += s;
    }

    public String getWord() {
        return text;
    }

    public double getAverage() {
        return totalScore/(double)numAppearance;
    }
}
