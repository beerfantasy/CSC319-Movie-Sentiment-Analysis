package th.in.ahri.moviereview;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * HashTable
 *
 * @author Jittapan
 */
public class HashTable {
    private List<WordEntry> wordEntries;

    public HashTable(int s) {
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

    public double getAverage(String s) {
        WordEntry wordEntry = wordEntries.stream().filter((e) -> e.getWord().equals(s)).findFirst().orElse(new WordEntry(null, 2));
        return wordEntry.getAverage();
    }
}