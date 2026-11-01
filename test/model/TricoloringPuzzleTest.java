package model;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import observer.Beobachter;

/**
 * Testklasse der Klasse TricoloringPuzzle
 */
class TricoloringPuzzleTest {
	TricoloringPuzzle puzzle;

	@BeforeEach
	void setUp() {
		int height = 3;
		puzzle = new TricoloringPuzzle(height); 
        assertEquals(height, puzzle.getHeight());
        assertEquals(Mode.GAME_MODE, puzzle.getMode());
        assertNotNull(puzzle.getBeobachter());
        assertTrue(puzzle.getBeobachter().isEmpty());
	}

	@Test
	void testChangeColor1() {
		// Überprüft, ob die Farbe des ersten nicht fixen Buttons korrekt geändert wird
		Color[][] fixButtonColors = puzzle.getFixButtonColors();
		int row = -1;
		int col = -1;

		outer: for (int i = 0; i < fixButtonColors.length; i++) {
			for (int j = 0; j < fixButtonColors[i].length; j++) {
				if (fixButtonColors[i][j] == null) {
					row = i;
					col = j;
					break outer;
				}
			}
		}
		puzzle.changeColor(row, col);
		Color[][] buttonColors = puzzle.getButtonColors();

		Color actual = buttonColors[row][col]; 
		Color expected = Color.BLUE; 

		assertEquals(actual, expected);
	}

	@Test
	void testChangeColor2() {
		// Überprüft, ob die Farbe des ersten nicht fixen Buttons korrekt geändert wird
		Color[][] fixButtonColors = puzzle.getFixButtonColors();
		int row = -1;
		int col = -1;

		outer: for (int i = 0; i < fixButtonColors.length; i++) {
			for (int j = 0; j < fixButtonColors[i].length; j++) {
				if (fixButtonColors[i][j] == null) {
					row = i;
					col = j;
					break outer;
				}
			}
		}
		puzzle.changeColor(row, col); // Erster Farbwechsel
		puzzle.changeColor(row, col); // Zweiter Farbwechsel
		puzzle.changeColor(row, col); // Dritter Farbwechsel
		Color[][] buttonColors = puzzle.getButtonColors();

		Color actual = buttonColors[row][col]; 
		Color expected = Color.YELLOW; 

		assertEquals(actual, expected);
	}
	
	@Test
	void testChangeColor3() {
		// Testet Farbwechsel im Testmodus
		puzzle.changeMode(); // Wechsel zu Testmodus
		int row = 0;
		int col = 0;

		puzzle.changeColor(row, col); // Erster Farbwechsel
		puzzle.changeColor(row, col); // Zweiter Farbwechsel
		puzzle.changeColor(row, col); // Dritter Farbwechsel
		Color[][] buttonColors = puzzle.getButtonColors();

		Color actual = buttonColors[row][col]; 
		Color expected = Color.YELLOW; 

		assertEquals(actual, expected);
	}
	
	
	@Test
	void testChangeColor4() {
		 // Überprüft, ob ein fixierter Button nicht verändert wird
	    Color[][] fixButtonColors = puzzle.getFixButtonColors();
	    int fixRow = -1; 
	    int fixCol = -1;

	    outer: for (int i = 0; i < fixButtonColors.length; i++) {
	        for (int j = 0; j < fixButtonColors[i].length; j++) {
	            if (fixButtonColors[i][j] != null) {
	                fixRow = i;
	                fixCol = j;
	                break outer;
	            }
	        }
	    }

	    assertNotEquals(-1, fixRow);
	    assertNotEquals(-1, fixCol);

	    Color expected = puzzle.getButtonColors()[fixRow][fixCol]; 
	    puzzle.changeColor(fixRow, fixCol); // Versucht, die Farbe des fixierten Buttons zu ändern
	    Color actual = puzzle.getButtonColors()[fixRow][fixCol];
	
	    assertEquals(expected, actual);
	}
	
	@Test
	void testChangeColor5() {
		// Überprüft die Begrenzung der Anzahl farbiger Buttons im Testmodus
	    puzzle.changeMode(); // Wechsel zu Testmodus

	    int height = puzzle.getHeight();
	    int coloredButtons = 0;

	    // height viele Buttons werden gefärbt
	    outer: for (int i = 0; i < puzzle.getButtonColors().length; i++) {
	        for (int j = 0; j < puzzle.getButtonColors()[i].length; j++) {
	            puzzle.changeColor(i, j);
	            coloredButtons++;
	            if (coloredButtons == height) {
	                break outer;
	            }
	        }
	    }

	    // Versucht, einen weiteren Button zu färben
	    int extraRow = height - 1;
	    int extraCol = height - 1;

	    Color expected = puzzle.getButtonColors()[extraRow][extraCol]; 
	    puzzle.changeColor(extraRow, extraCol); //Versucht weiteren Button zu färben
	    Color actual = puzzle.getButtonColors()[extraRow][extraCol]; 

	    assertEquals(expected, actual);
	}
	

	@Test
	void testReset() {
		// Überprüft, ob TricoloringPuzzle korrekt zurückgesetzt wird
		Color[][] fixButtonColors = puzzle.getFixButtonColors();
		int row = 0;
		int col = 0;

		outer: for (int i = 0; i < fixButtonColors.length; i++) {
			for (int j = 0; j < fixButtonColors[i].length; j++) {
				if (fixButtonColors[i][j] == null) {
					row = i;
					col = j;
					break outer;
				}
			}
		}
		puzzle.changeColor(row, col); 
		puzzle.reset(); 
		Color[][] buttonColors = puzzle.getButtonColors();

		Color actual = buttonColors[row][col]; 
		Color expected = Color.GRAY; 

		assertEquals(expected, actual);
	}

	@Test
	void testCheck1() {
		//Testet incomplete Status von Gamemodus
		PuzzleMessage check = puzzle.check();
	
		PuzzleStatus actual = check.getStatus();
		PuzzleStatus expected = PuzzleStatus.INCOMPLETE;

		assertEquals(expected, actual);
	}

	@Test
	void testCheck2() {
		//Testet incorrect Status von Gamemodus
		int[] solution = puzzle.getSolution();
		Color[][] fixButtonColors = puzzle.getFixButtonColors();

		int buttonCounter = 0;
		boolean ersterSolution = true;
		for (int i = 0; i < fixButtonColors.length; i++) {
			for (int j = 0; j < fixButtonColors[i].length; j++) {
				int buttonColorNumberOfSolution = solution[buttonCounter];
				if (fixButtonColors[i][j] == null) {
					if (ersterSolution) { // Bei dem ersten Button welches kein Fixbutton ist wird die Farbe der Loesung
											// veraendert, so dass die Loesung falsch sein muss.
						buttonColorNumberOfSolution = (buttonColorNumberOfSolution + 1) % 3;
						ersterSolution = false;
					}

					for (int k = 0; k < buttonColorNumberOfSolution + 1; k++) {
						puzzle.changeColor(i, j);
					}
				}
				buttonCounter++;
			}
		}

		PuzzleMessage check = puzzle.check();

		PuzzleStatus actual = check.getStatus();
		PuzzleStatus expected = PuzzleStatus.INCORRECT;

		assertEquals(expected, actual);
	}

	@Test
	void testCheck3() {
		//Testet correct Status von Gamemodus
		int[] solution = puzzle.getSolution();
		Color[][] fixButtonColors = puzzle.getFixButtonColors();

		int buttonCounter = 0;
		for (int i = 0; i < fixButtonColors.length; i++) {
			for (int j = 0; j < fixButtonColors[i].length; j++) {
				int buttonColorNumberOfSolution = solution[buttonCounter];
				if (fixButtonColors[i][j] == null) {
					for (int k = 0; k < buttonColorNumberOfSolution + 1; k++) {
						puzzle.changeColor(i, j);
					}
				}
				buttonCounter++;
			}
		}

		PuzzleMessage check = puzzle.check();

		PuzzleStatus actual = check.getStatus();
		PuzzleStatus expected = PuzzleStatus.CORRECT;

		assertEquals(expected, actual);
	}
	
	@Test
	void testCheck4() {
		//Testet incomplete Status von Testmodus
		puzzle.changeMode(); //in Testmodus wechseln
		PuzzleMessage check = puzzle.check();
		PuzzleStatus actual = check.getStatus();
		PuzzleStatus expected = PuzzleStatus.INCOMPLETE;

		assertEquals(expected, actual);
	}
	
	@Test
	void testCheck5() {
		//Testet incorrect Status von Testmodus
		puzzle.changeMode(); //in Testmodus wechseln
		puzzle.changeColor(0, 0);
		puzzle.changeColor(1, 0);
		puzzle.changeColor(1, 1);
		
		PuzzleMessage check = puzzle.check();

		PuzzleStatus actual = check.getStatus();
		PuzzleStatus expected = PuzzleStatus.INCORRECT;

		assertEquals(expected, actual);
	}
	
	@Test
	void testCheck6() {
		//Testet correct Status von Testmodus
		puzzle.changeMode(); //in Testmodus wechseln
		puzzle.changeColor(0, 0);
		puzzle.changeColor(1, 1);
		puzzle.changeColor(2, 2);
		
		PuzzleMessage check = puzzle.check();

		PuzzleStatus actual = check.getStatus();
		PuzzleStatus expected = PuzzleStatus.CORRECT;

		assertEquals(expected, actual);
	}

	@Test
	void testNewGame1() {
		//Testet, ob nach neuem Spiel genau height viele Fixbuttons gesetzt sind
		puzzle.newGame();
		Color[][] fixButtonColors = puzzle.getFixButtonColors();

		int actual = 0;
		for (int i = 0; i < fixButtonColors.length; i++) {
			for (int j = 0; j < fixButtonColors[i].length; j++) {
				if (fixButtonColors[i][j] != null) {
					actual++;
				}
			}
		}
		int expected = puzzle.getHeight();

		assertEquals(expected, actual);
	}

	@Test
	void testNewGame2() {
		//Testet, ob nach neuem Spiel alle Buttons ausser den Fixbuttons auf default Color sind und die Fixbuttons ihre Farbe haben
		puzzle.changeColor(0, 0); // Farbe wird vor dem neuen Spiel veraendert
		puzzle.newGame();
		Color[][] fixButtonColors = puzzle.getFixButtonColors();
		Color[][] buttonColors = puzzle.getButtonColors();

		for (int i = 0; i < buttonColors.length; i++) {
			for (int j = 0; j < buttonColors[i].length; j++) {
				Color actual = buttonColors[i][j];
				Color expected = fixButtonColors[i][j] == null ? Color.GRAY : fixButtonColors[i][j];
				assertEquals(expected, actual);
			}
		}
	}

	@Test
	void testNewGame3() {
		//Testet, ob das neue Spiel loesbar ist
		puzzle.newGame();
		puzzle.solve();
		PuzzleMessage check = puzzle.check();

		PuzzleStatus actual = check.getStatus();
		PuzzleStatus expected = PuzzleStatus.CORRECT;

		assertEquals(expected, actual);
	}

	@Test
	void testSolve1() {
		//Testet, ob die Loesung von solve fuer alle erlaubten Hoehen beim Check als korrekt erkannt wird
		for (int height = 2; height <= 18; height++) {
			puzzle.setHeight(height);
			puzzle.solve();
			PuzzleMessage check = puzzle.check();

			PuzzleStatus actual = check.getStatus();
			PuzzleStatus expected = PuzzleStatus.CORRECT;

			assertEquals(expected, actual, "Fehler bei Hoehe " + height);
		}
	}

	@Test
	void testSolve2() {
		//Testet, ob solve die Farben der Fixbuttons nicht veraendert
		Color[][] fixButtonColors = puzzle.getFixButtonColors();
		puzzle.solve();
		Color[][] buttonColors = puzzle.getButtonColors();

		for (int i = 0; i < fixButtonColors.length; i++) {
			for (int j = 0; j < fixButtonColors[i].length; j++) {
				if (fixButtonColors[i][j] != null) {
					Color actual = buttonColors[i][j];
					Color expected = fixButtonColors[i][j];
					assertEquals(expected, actual);
				}
			}
		}
	}

	@Test
	void testChangeMode1() {	
		//Testet, ob man zum Testmodus wechselt
		puzzle.changeMode(); 
		Mode actual = puzzle.getMode();
		Mode expected = Mode.TEST_MODE;
		assertEquals(expected, actual);
	}
	
	@Test
	void testChangeMode2() {	
		//Testet, ob man zurück zum Spielmodus wechseln kann
		puzzle.changeMode(); // Zum Testmodus wechseln
		puzzle.changeMode(); // Zurück zum Spielmodus wechseln
		
		Mode actual = puzzle.getMode();
		Mode expected = Mode.GAME_MODE;
		assertEquals(expected, actual);
	}

	@Test
	void testSetHeight1() {
		//Testet, ob eine gültige Hohe gesetzt werden kann
	    int newHeight = 2;
	    puzzle.setHeight(newHeight);
	    assertEquals(newHeight, puzzle.getHeight());
	}
	
	
	@Test
	void testSetHeight2() {
		//Testet, ob bei einer ungültigen Hohe eine Exception fliegt
	    int newHeight = 1;  // Höhe unter der erlaubten Grenze
	    Assertions.assertThrows(IllegalArgumentException.class, () -> puzzle.setHeight(newHeight));
	}
	

	@Test
	void testSetHeight3() {
		//Nach newHeight muss die korrekte Pyramidenstruktur vorliegen. Hier wird dies getestet
	    int newHeight = 5;
	    puzzle.setHeight(newHeight);
	    Color[][] fixButtonColors = puzzle.getFixButtonColors();
	    Color[][] buttonColors = puzzle.getButtonColors();
	    
	    assertEquals(newHeight, buttonColors.length);
	    assertEquals(newHeight, fixButtonColors.length);
	    
	    for (int row = 0; row < buttonColors.length; row++) {
			int actualCols = buttonColors[row].length;
			int expectedCols = row + 1;
			assertEquals(expectedCols, actualCols);
		}
	}
	
	@Test
	void testSetHeight4() {
		//Nach newHeight muss die A Matrix neu erstellt werden
	    int newHeight = 3;
	    puzzle.setHeight(newHeight);
	    
	    int[][] actual = puzzle.getA();
	    int[][] expected = {
	            {1, 1, 1, 0, 0, 0},
	            {0, 1, 0, 1, 1, 0},
	            {0, 0, 1, 0, 1, 1}
	    };
	    
	    assertEquals(expected.length, actual.length);
	    assertEquals(expected[0].length, actual[0].length);   
	    
	    assertTrue(Arrays.deepEquals(actual, expected));
	}
	
	@Test
	void testSetHeight5() {
		//Nach newHeight muss die N Matrix neu erstellt werden
	    int newHeight = 3;
	    puzzle.setHeight(newHeight);
	    
	    int[][] actual = puzzle.getN();
	    int[][] expected = {
	            {1, 0, 2, 0, 0, 1},
	            {2, 2, 2, 0, 1, 0},
	            {1, 2, 0, 1, 0, 0}
	     };
	    
	    assertEquals(expected.length, actual.length);
	    assertEquals(expected[0].length, actual[0].length);   
	    
	    assertTrue(Arrays.deepEquals(actual, expected));	  
	}

	@Test
	void testRegistriereBeobachter() {
		//Testet, ob ein Beobachter korrekt registriert wird
		TestBeobachter beobachter = new TestBeobachter();
		puzzle.registriereBeobachter(beobachter);

		assertEquals(1, puzzle.getBeobachter().size());
		assertTrue(puzzle.getBeobachter().contains(beobachter));
	}

	@Test
	void testEntferneBeobachter() {
		//Testet, ob ein registrierter Beobachter korrekt entfernt wird
		TestBeobachter beobachter = new TestBeobachter();
		puzzle.registriereBeobachter(beobachter);
		puzzle.entferneBeobachter(beobachter);

		assertTrue(puzzle.getBeobachter().isEmpty());
	}

	@Test
	void testBenachrichtigeBeobachter1() {
		//Testet, ob der Beobachter bei changeColor benachrichtigt wird
		TestBeobachter beobachter = new TestBeobachter();
		puzzle.registriereBeobachter(beobachter);
		puzzle.changeColor(0, 0);

		int actual = beobachter.anzahlBenachrichtigungen;
		int expected = 1;

		assertEquals(expected, actual);
	}

	@Test
	void testBenachrichtigeBeobachter2() {
		//Testet, ob der Beobachter bei reset benachrichtigt wird
		TestBeobachter beobachter = new TestBeobachter();
		puzzle.registriereBeobachter(beobachter);
		puzzle.reset();

		int actual = beobachter.anzahlBenachrichtigungen;
		int expected = 1;

		assertEquals(expected, actual);
	}

	@Test
	void testBenachrichtigeBeobachter3() {
		//Testet, ob der Beobachter bei newGame benachrichtigt wird
		TestBeobachter beobachter = new TestBeobachter();
		puzzle.registriereBeobachter(beobachter);
		puzzle.newGame();

		int actual = beobachter.anzahlBenachrichtigungen;
		int expected = 1;

		assertEquals(expected, actual);
	}

	@Test
	void testBenachrichtigeBeobachter4() {
		//Testet, ob der Beobachter bei solve benachrichtigt wird
		TestBeobachter beobachter = new TestBeobachter();
		puzzle.registriereBeobachter(beobachter);
		puzzle.solve();

		int actual = beobachter.anzahlBenachrichtigungen;
		int expected = 1;

		assertEquals(expected, actual);
	}

	@Test
	void testBenachrichtigeBeobachter5() {
		//Testet, ob der Beobachter bei changeMode benachrichtigt wird
		TestBeobachter beobachter = new TestBeobachter();
		puzzle.registriereBeobachter(beobachter);
		puzzle.changeMode();

		int actual = beobachter.anzahlBenachrichtigungen;
		int expected = 1;

		assertEquals(expected, actual);
	}

	@Test
	void testBenachrichtigeBeobachter6() {
		//Testet, ob der Beobachter bei setHeight benachrichtigt wird
		TestBeobachter beobachter = new TestBeobachter();
		puzzle.registriereBeobachter(beobachter);
		puzzle.setHeight(4);

		int actual = beobachter.anzahlBenachrichtigungen;
		int expected = 1;

		assertEquals(expected, actual);
	}

	@Test
	void testBenachrichtigeBeobachter7() {
		//Testet, ob ein entfernter Beobachter nicht mehr benachrichtigt wird
		TestBeobachter beobachter = new TestBeobachter();
		puzzle.registriereBeobachter(beobachter);
		puzzle.entferneBeobachter(beobachter);
		puzzle.changeColor(0, 0);

		int actual = beobachter.anzahlBenachrichtigungen;
		int expected = 0;

		assertEquals(expected, actual);
	}

	@Test
	void testBenachrichtigeBeobachter8() {
		//Testet, ob alle registrierten Beobachter benachrichtigt werden
		TestBeobachter beobachter1 = new TestBeobachter();
		TestBeobachter beobachter2 = new TestBeobachter();
		puzzle.registriereBeobachter(beobachter1);
		puzzle.registriereBeobachter(beobachter2);
		puzzle.reset();

		assertEquals(1, beobachter1.anzahlBenachrichtigungen);
		assertEquals(1, beobachter2.anzahlBenachrichtigungen);
	}

	@Test
	void testBenachrichtigeBeobachter9() {
		//Testet, ob der Beobachter den aktuellen Zustand des Models erhaelt
		TestBeobachter beobachter = new TestBeobachter();
		puzzle.registriereBeobachter(beobachter);
		puzzle.changeMode(); // Wechsel zu Testmodus
		puzzle.changeColor(0, 0);

		assertTrue(Arrays.deepEquals(puzzle.getButtonColors(), beobachter.letzteButtonColors));
		assertTrue(Arrays.deepEquals(puzzle.getFixButtonColors(), beobachter.letzteFixButtonColors));
		assertEquals(Mode.TEST_MODE, beobachter.letzterMode);
	}

	/**
	 * Beobachter fuer die Tests, der die Anzahl der Benachrichtigungen zaehlt
	 * und den zuletzt erhaltenen Zustand speichert
	 */
	private static class TestBeobachter implements Beobachter {
		int anzahlBenachrichtigungen = 0;
		Color[][] letzteButtonColors;
		Color[][] letzteFixButtonColors;
		Mode letzterMode;

		@Override
		public void aktualisiere(Color[][] buttonColors, Color[][] fixButtonColors, Mode mode) {
			anzahlBenachrichtigungen++;
			letzteButtonColors = buttonColors;
			letzteFixButtonColors = fixButtonColors;
			letzterMode = mode;
		}
	}
}
