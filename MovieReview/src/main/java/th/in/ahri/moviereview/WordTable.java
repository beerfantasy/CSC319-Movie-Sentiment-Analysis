package th.in.ahri.moviereview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * WordTable
 *
 * @author Jittapan
 */
public class WordTable {
    private List<WordEntry> wordEntries;
    private static final WordEntry DEFAULT_SCORE = new WordEntry(null, 2);

    public WordTable(int s) {
        wordEntries = new ArrayList<>(s);
    }

    public void put(String s, int score) {
        Optional<WordEntry> word = wordEntries.stream().filter((e) -> e.getWord().equals(s)).findFirst();
        if(word.isPresent()) {
            word.get().addNewAppearance(score);
        } else {
            wordEntries.add(new WordEntry(s, score));
        }
    }

    public void addExisting(String s, int score, int appearance) {
        WordEntry entry = new WordEntry(s, 0);
        entry.setNumAppearance(appearance);
        entry.setTotalScore(score);
        wordEntries.add(entry);
    }

    public double getAverage(String s) {
        WordEntry wordEntry = wordEntries.stream().filter((e) -> e.getWord().equals(s)).findFirst().orElse(DEFAULT_SCORE);
        return wordEntry.getAverage();
    }

    public List<WordEntry> getReadOnlyWordList() {
        return Collections.unmodifiableList(wordEntries);
    }
}