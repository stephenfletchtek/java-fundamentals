package intellij_game;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class GameTest {
    private final Masker mockMasker = mock(Masker.class);
    private final WordChooser mockWordChooser = mock(WordChooser.class);
    private Game game;
    private ArrayList<Character> result;

    @Before
    public void initialize () {
        when(mockWordChooser.getRandomWordFromDictionary()).thenReturn("MAKERS");
        game = new Game(mockWordChooser, mockMasker, "");
        result = new ArrayList<>();
    }

    @Test
    public void testGetWordToGuess() {
        when(mockMasker.getMaskedWord("MAKERS", result)).thenReturn("M_____");
        assertEquals(game.getWordToGuess(), "M_____");
    }

    @Test
    public void testGetWordToGuessWithAttempt() {
        when(mockMasker.getMaskedWord("MAKERS", result)).thenReturn(("M_K___"));
        result.add('K');
        game.guessLetter('K');
        assertEquals(game.getWordToGuess(),"M_K___");
    }

    @Test
    public void guessRightLetters() {
        assertTrue(game.guessLetter('M'));
        assertTrue(game.guessLetter('A'));
        assertTrue(game.guessLetter('A'));
        result.add('M');
        result.add('A');
        assertEquals(game.getGuessed(), result);
        assertEquals(game.getRemainingAttempts(), Integer.valueOf(10));
    }

    @Test
    public void guessWrongLetter() {
        assertFalse(game.guessLetter('B'));
        assertEquals(game.getGuessed(), result);
        assertEquals(game.getRemainingAttempts(), Integer.valueOf(9));
    }

    @Test
    public void noWinOrLose() {
        assertFalse("Game is lost", game.isGameLost());
        assertFalse("Game is won", game.isGameWon());
    }

    @Test
    public void doLoseGame() {
        for (int i = 0; i < 10; i++) {
            game.guessLetter('Z');
        }
        assertTrue("Game is lost", game.isGameLost());
    }

    @Test
    public void doWinGame() {
        String word = mockWordChooser.getRandomWordFromDictionary();
        for (int i = 0; i < word.length(); i++) {
            game.guessLetter(word.charAt(i));
        }
        assertTrue("Game is won", game.isGameWon());
    }

    @Test
    public void doWinGameRepeatLetters() {
        when(mockWordChooser.getRandomWordFromDictionary()).thenReturn("AAB");
        game = new Game(mockWordChooser, new Masker(), "");
        game.guessLetter('A');
        game.guessLetter('B');
        assertTrue("Game is won", game.isGameWon());
    }
}
