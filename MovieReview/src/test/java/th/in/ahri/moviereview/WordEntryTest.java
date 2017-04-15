package th.in.ahri.moviereview;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * MovieReview
 *
 * @author Jittapan
 */
public class WordEntryTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testNullStringWordEntry() {
        WordEntry word = new WordEntry(null, 2);
        word.getAverage();
        word.getWord();
    }
}