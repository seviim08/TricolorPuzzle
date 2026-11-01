package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import observer.Beobachter;
import observer.Subjekt;

/**
 * Implementiert das Model- und Subjekt-Interface. Sie enthaelt die
 * Verarbeitungslogik der Pyramide und benachrichtigt Beobachter bei Aenderungen
 * im Modell.
 */
public class TricoloringPuzzle implements Model, Subjekt {
	private final Color[] colors = { Color.BLUE, Color.CYAN, Color.YELLOW }; // Farbe der Tricolor   //ES WURDE PRIVATE WEG GEMACHT 
	private final Color defaultColor = Color.GRAY; // Ursprungsfarbe

	private ArrayList<Beobachter> beobachter;

	private Color[][] buttonColors;
	private Color[][] fixButtonColors;
	private int height;
	private Mode mode;
	private int[][] A;
	private int[][] N;
	private int[] solution;

	/**
	 * Standardkonstruktor der Klasse TricoloringPuzzle.
	 * Initialisiert die TricoloringPuzzle mit dem Standardmodus und einer leeren Beobachterliste.
	 */
	public TricoloringPuzzle() {
		mode = Mode.GAME_MODE; // default Modus des Spiels
		beobachter = new ArrayList<>();
	}
	
	/**
	 * Konstruktor für die Klasse TricoloringPuzzle mit spezifischer Hoehe fuer die Pyramide.
	 * Ruft den Standardkonstruktor auf und setzt anschließend die Hoehe der Pyramide.
	 *
	 * @param height die gewünschte Hoehe der Pyramide
	 */
	public TricoloringPuzzle(int height) { //Dieser Konstruktor für Test des Pyramidmodels. Wird beim Spiel jedoch nicht gebraucht.
		this();
		setHeight(height);
	}

	/* Initialisierungsmethoden, die die Grundlage bilden */

	/**
	 * Methode zur Erstellung der Koeffizientenmatrix A
	 */
	private void initializeCoefficientMatrix() {
		// Initialisierung der Matrix A
		int numRows = (height * (height - 1)) / 2; // Anzahl der Gleichungen
		int numVariables = (height * (height + 1)) / 2; // Anzahl der Variablen
		A = new int[numRows][numVariables];

		// Schleifen zur Erstellung der Matrix
		int equationIndex = 0;

		for (int row = 0; row < height - 1; row++) {
			for (int col = 0; col <= row; col++) {
				// Bestimmung der drei Variablen für Tripel
				int variable1 = (row * (row + 1)) / 2 + col; // Oben
				int variable2 = ((row + 1) * (row + 2)) / 2 + col; // Links
				int variable3 = ((row + 1) * (row + 2)) / 2 + (col + 1); // Rechts

				A[equationIndex][variable1] = 1;
				A[equationIndex][variable2] = 1;
				A[equationIndex][variable3] = 1;
				equationIndex++; // weiter zum nächsten Tripel
			}
		}
	}

	/**
	 * Initialisiert den Nullraum der Matrix A.
	 */
	private void initializeNullspace() {
		N = new int[height][A[0].length];

		for (int row = 0; row < height; row++) {
			int[] x = new int[A[0].length];
			for (int i = 0; i < x.length; i++) {
				if (i < (x.length - height)) { // die ersten werte werden -1 gesetzt
					x[i] = -1;
					continue;
				}

				if (i == (x.length - 1 - row)) { // "Einheismatrix" wird erstellt
					x[i] = 1;
					continue;
				}

				x[i] = 0;
			}
			int[] solutionOfNullspaceVector = solveHomogeneousLinearSystemWithMatrixA(x);
			for (int variable = 0; variable < solutionOfNullspaceVector.length; variable++) {
				N[row][variable] = solutionOfNullspaceVector[variable];
			}
		}
	}

	/**
	 * Initialisiert die Größe der Pyramide und setzt alle Buttons auf defaultColor.
	 * Gleichzeitig werden die festen Buttons zurückgesetzt.
	 */
	private void initializeButtonColors() {
		for (int i = 0; i < height; i++) {
			buttonColors[i] = new Color[i + 1]; // Anzahl der Spalten der jeweiligen Zeile
			fixButtonColors[i] = new Color[i + 1];
			for (int j = 0; j <= i; j++) {
				buttonColors[i][j] = defaultColor;
				fixButtonColors[i][j] = null; // zunächst null
			}
		}
	}

	/**
	 * Setzt height viele feste Buttons mit zufaelligen Farben an zufaelligen
	 * Stellen im Puzzle und stellt sicher, dass das Gleichungssystem korrekt
	 * geloest werden kann.
	 */
	private void setFixButtons() {
		solution = new int[A[0].length];

		// Bereite die Loesung vor, indem die ersten Variablen mit -1 und die letzten
		// (height viele) mit zufaelligen Farben initialisiert werden.
		for (int i = 0; i < solution.length; i++) {
			if (i < (solution.length - height)) {
				solution[i] = -1;
			} else {
				// Zufaellige Farbwahl fuer die letzten height Variablen,
				// um die Ruecksubstitution beim Loesen des Gleichungssystems zu ermoeglichen
				solution[i] = ThreadLocalRandom.current().nextInt(0, colors.length);
			}
		}

		// Loese das homogene lineare Gleichungssystem, um die restlichen Variablen zu
		// bestimmen
		solution = solveHomogeneousLinearSystemWithMatrixA(solution);

		// Generiere zufaellige Indizes, um die Positionen der festen Buttons zu
		// bestimmen
		int[] randomIndices = getRandomIndices(); 

		// Setze die festen Buttons basierend auf den zufaelligen Indizes und der
		// berechneten Loesung
		for (int i = 0; i < randomIndices.length; i++) {
			int index = randomIndices[i]; // Position des festen Buttons im 1-Dimensionalem
			int row = getRowFromIndex(index); // Konvertiere den 1D-Index in die Zeile der 2D-Position
			int col = getColFromIndex(index); // Konvertiere den 1D-Index in die Zeile der 2D-Position
			fixButtonColors[row][col] = colors[solution[index]];
			buttonColors[row][col] = colors[solution[index]];
		}
	}

	/* Strategiemethoden */

	@Override
	public void changeColor(int row, int col) {
		int currentIndex = getColorNumber(buttonColors[row][col]);

		switch (mode) {
		case GAME_MODE:
			if (fixButtonColors[row][col] == null) { // Farbe wird gewechselt, wenn es kein fester Button ist
				if (currentIndex == 2) { // Nach der letzten Farbe der 3 Farben wird die Farbe wieder in den Ursprung
											// gesetzt
					buttonColors[row][col] = defaultColor;
				} else {
					buttonColors[row][col] = colors[currentIndex + 1]; // Farbe wird eine "Stufe" erhöht
				}
			}
			break;

		case TEST_MODE:
			if (currentIndex != -1) {
				if (currentIndex == 2) {
					buttonColors[row][col] = defaultColor;
				} else {
					buttonColors[row][col] = colors[currentIndex + 1]; // Farbe wird eine "Stufe" erhöht
				}
				break;
			}

			boolean hasMaxColoredButtons = hasMaxColoredButtons();

			if (!hasMaxColoredButtons) {
				if (currentIndex == 2) {
					buttonColors[row][col] = defaultColor;
				} else {
					buttonColors[row][col] = colors[currentIndex + 1]; // Farbe wird eine "Stufe" erhöht
				}
			}
			break;

		default:
			throw new NullPointerException("Der Modus des Spiels ist Null");
		}

		benachrichtigeBeobachter(); //
	}

	@Override
	public void reset() {
		for (int i = 0; i < buttonColors.length; i++) {
			for (int j = 0; j <= i; j++) {
				if (fixButtonColors[i][j] == null) {
					buttonColors[i][j] = defaultColor;
				}
			}
		}
		benachrichtigeBeobachter();
	}

	@Override
	public PuzzleMessage check() {
		PuzzleMessage puzzleMessage = null;
		switch (mode) {
		case GAME_MODE:
			// 1.Fall: Mind. ein Button ist noch nicht gefärbt
			boolean atLeastOneButtonNotColored = Arrays.stream(buttonColors) // Stream der rows
					.flatMap(Arrays::stream) // Stream aller Elemente des zweidimensionalen Arrays
					.anyMatch(defaultColor::equals); // Sucht nach einem Element mit defaultColor

			if (atLeastOneButtonNotColored) {
				String message = "Deine Lösung ist noch nicht vollständig. Schau nochmal hin!";
				puzzleMessage = new PuzzleMessage(PuzzleStatus.INCOMPLETE, message);
				return puzzleMessage;
			}

			// 2./3. Fall: die abgegebene Loesung ist richtig/falsch
			int[] currentSolutionOfGameMode = currentSolution();

			// Vergleich der berechneten Lösung mit der aktuellen Puzzle-Lösung
			if (Arrays.equals(currentSolutionOfGameMode, solution)) {
				String message = "Glückwunsch! Du hast die richtige Lösung gefunden!";
				puzzleMessage = new PuzzleMessage(PuzzleStatus.CORRECT, message);
				return puzzleMessage;
			}

			if (isSolutionCorrect(currentSolutionOfGameMode)) {
				String message = "Glückwunsch! Du hast die richtige Lösung gefunden!";
				puzzleMessage = new PuzzleMessage(PuzzleStatus.CORRECT, message);
				return puzzleMessage;
			} else {
				String message = "Das ist leider nicht richtig. Versuch es nochmal!";
				puzzleMessage = new PuzzleMessage(PuzzleStatus.INCORRECT, message);
				return puzzleMessage;
			}

		case TEST_MODE:
			if (!hasMaxColoredButtons()) {
				String message = "Du musst " + height + " Buttons färben!";
				puzzleMessage = new PuzzleMessage(PuzzleStatus.INCOMPLETE, message);
				return puzzleMessage;
			}

			int[] currentSolutionOfTestMode = currentSolution();

			if (givesUniqueSolution(currentSolutionOfTestMode)) {
				String message = "Glückwunsch! Deine Wahl der Buttons ergibt für das Tricolor Puzzle eine eindeutige Lösung!";
				puzzleMessage = new PuzzleMessage(PuzzleStatus.CORRECT, message);
				return puzzleMessage;
			} else {
				String message = "Deine Wahl der Buttons ergibt für das Tricolor Puzzle leider keine eindeutige Lösung. Versuch es nochmal!";
				puzzleMessage = new PuzzleMessage(PuzzleStatus.INCORRECT, message);
				return puzzleMessage;
			}

		default:
			throw new NullPointerException("Mode of game is null.");
		}
	}

	@Override
	public void newGame() {
		initializeButtonColors();
		setFixButtons();
		benachrichtigeBeobachter();
	}

	@Override
	public void solve() {
		for (int row = 0; row < buttonColors.length; row++) {
			for (int col = 0; col < buttonColors[row].length; col++) {
				int indexOfButton = getIndexOfButton(row, col);
				int colorIndexOfButton = solution[indexOfButton];
				buttonColors[row][col] = colors[colorIndexOfButton];
			}
		}
		benachrichtigeBeobachter();
	}

	@Override
	public void changeMode() {
		initializeButtonColors();
		switch (mode) {
		case GAME_MODE:
			mode = Mode.TEST_MODE;
			break;

		case TEST_MODE:
			mode = Mode.GAME_MODE;
			setFixButtons();
			break;

		default:
			throw new IllegalArgumentException("Unvalid game mode: " + mode);
		}

		benachrichtigeBeobachter();
	}

	@Override
	public void setHeight(int height) {
		if (height < 2) {
            throw new IllegalArgumentException("Height must be greater than 2.");
        }
		
		this.height = height;
		initializeCoefficientMatrix();
		initializeNullspace();
		
		buttonColors = new Color[height][];
		fixButtonColors = new Color[height][];
		initializeButtonColors(); // initialisiert 2-dim Arrays ButtonColors mit der DefaultColoe und
									// fixbuttonColors mit null
		if (mode.equals(Mode.GAME_MODE)) {
			setFixButtons();
		}
		benachrichtigeBeobachter();
	}

	// Hilfsmethoden

	/**
	 * Ueberprueft im Test-Modus, ob die maximale Anzahl an farbigen Buttons
	 * erreicht wurde.
	 * 
	 * @return true, wenn die Anzahl der farbigen Buttons gleich height ist,
	 *         andernfalls false.
	 */
	private boolean hasMaxColoredButtons() {
		int coloredButtonsCount = 0;
		for (int i = 0; i < buttonColors.length; i++) {
			for (int j = 0; j < buttonColors[i].length; j++) {
				if (getColorNumber(buttonColors[i][j]) != -1) {
					coloredButtonsCount++;
				}
				if (coloredButtonsCount == height) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Die Methode erstellt ein Array, das den Farbindex jedes Buttons in der
	 * aktuellen Pyramide entsprechend seiner Position im eindimensionalen Array
	 * enthaelt.
	 * 
	 * @return Ein Array, das die aktuellen Farbindices aller Buttons repräsentiert.
	 */
	private int[] currentSolution() {
		int totalButtons = A[0].length;
		int[] currentSolution = new int[totalButtons];

		for (int row = 0; row < buttonColors.length; row++) {
			for (int col = 0; col < buttonColors[row].length; col++) {
				int indexOfButton = getIndexOfButton(row, col);
				currentSolution[indexOfButton] = getColorNumber(buttonColors[row][col]);
			}
		}
		return currentSolution;
	}

	/**
	 * Berechnet den Modulo-Wert von einer Zahl modulo 3.
	 * 
	 * @param val Die Eingabezahl, für die der Modulo-Wert berechnet werden soll.
	 * @return Der Modulo-Wert im Bereich 0 bis 2.
	 */
	private int mod3(int val) {
		int r = val % 3;
		return r < 0 ? r + 3 : r;
	}

	/**
	 * Prueft, ob die aktuelle Loesung im Testmodus eine eindeutige Loesung fuer das
	 * Puzzle darstellt.
	 * 
	 * @param solutionOfTestMode Die aktuelle Loesung im Testmodus.
	 * @return true, wenn die Loesung eindeutig ist, andernfalls false.
	 */
	private boolean givesUniqueSolution(int[] solutionOfTestMode) {
		int[] indices = new int[height];
		int countIndices = 0;
		for (int i = 0; i < solutionOfTestMode.length; i++) {
			if (solutionOfTestMode[i] != -1) {
				indices[countIndices] = i; // die Indizes der Buttons die eine Farbe haben werde gespeichert
				countIndices++;
			}
		}

		if (N.length != countIndices) {
			throw new IllegalArgumentException("Number of fixButtons (" + countIndices
					+ ") has to be equal to the rows of the nullSpace of A (" + N.length + ").");
		}

		// P Matrix wird erstellt durch den nullspace
		int[][] P = new int[indices.length][indices.length];
		for (int col = 0; col < P[0].length; col++) {
			int variable = indices[col];
			for (int row = 0; row < P.length; row++) {
				int value = N[row][variable];
				P[row][col] = value;
			}
		}

		// Gauss in Stufenform unter mod 3
		for (int col = 0; col < P[0].length; col++) {
			// Pivot finden
			int pivotRowIndex = -1;
			for (int row = col; row < P.length; row++) {
				if (P[row][col] != 0) {
					pivotRowIndex = row;
					break;
				}
			}

			if (pivotRowIndex == -1) {
				// keine Pivotzeile gefunden -> Nullspalte -> Rang kleiner -> return false
				return false;
			}

			// Falls pivotRowIndex != col, Zeilen tauschen
			if (pivotRowIndex != col) { 
				int[] temp = P[col];
				P[col] = P[pivotRowIndex];
				P[pivotRowIndex] = temp;
			}

			// Jetzt ist P[col][col] unser Pivot
			int pivot = P[col][col];

			// Pivotzeile normieren (durch Pivot teilen)
			for (int j = 0; j < P[col].length; j++) {
				P[col][j] = mod3(P[col][j] * pivot);
			}

			// Jetzt Pivotelimination in anderen Zeilen
			for (int row = 0; row < P.length; row++) {
				if (row == col)
					continue; // Pivotzeile wird nicht nochmal bearbeitet

				int factor = mod3(P[row][col]);
				if (factor != 0) {
					for (int j = 0; j < P[row].length; j++) {
						int newVal = P[row][j] - factor * P[col][j];
						P[row][j] = mod3(newVal);
					}
				}
			}
		}

		// Kontrolle auf Nullzeilen
		for (int row = 0; row < P.length; row++) {
			boolean isNullRow = true;
			for (int val : P[row]) {
				if (mod3(val) != 0) {
					isNullRow = false;
					break;
				}
			}
			if (isNullRow)
				return false;
		}

		return true;
	}

	/**
	 * Generiert ein Array der Groesse height mit zufaelligen, eindeutigen Indizes
	 * im Bereich der Anzahl der Buttons
	 * 
	 * @return Ein Array mit zufaelligen, eindeutigen Indizes.
	 */
	private int[] getRandomIndices() {
		int[] indices = new int[height];
		List<Integer> indicesList = new ArrayList<Integer>();
		for (int i = 0; i < indices.length; i++) {
			int randomIndex = ThreadLocalRandom.current().nextInt(0, A[0].length); // random Index einer Variable
			while (indicesList.contains(randomIndex)) {
				randomIndex = ThreadLocalRandom.current().nextInt(0, A[0].length);
			}

			indicesList.add(randomIndex);
			indices[i] = randomIndex;
		}
		return indices;
	}

	/**
	 * Berechnet die homogene Loesung für den Vektor x mithilfe der
	 * Koeffizientenmatrix A. Da die Matrix A bereits in Stufenform vorliegt und die
	 * letzten Komponenten des Vektors x vorgegeben sind, kann die Lösung durch
	 * Rueckwaertssubstitution ermittelt werden.
	 *
	 * @param x der Vektor x, bei dem die letzten 'height' Komponenten Werte
	 *          zwischen 0 und 2 enthalten und die ersten Komponenten auf -1 gesetzt
	 *          sind
	 * @return der vollstaendig aufgelöste Vektor x, bei dem die zuvor mit -1
	 *         markierten Komponenten Werte zwischen 0 und 2 erhalten
	 */
	private int[] solveHomogeneousLinearSystemWithMatrixA(int[] x) {
		int numEquations = A.length;
		int numVariables = A[0].length;

		if (numVariables != x.length) {
			throw new IllegalArgumentException("Dimensions of A and x are not compatible.");
		}

		int definedCount = 0;

		for (int val : x) {
			if (val != -1)
				definedCount++;
		}

		for (int i = 0; i < x.length; i++) {
			if (i < (x.length - height)) {
				if (x[i] != -1) {
					throw new IllegalArgumentException("x-vector has not expected values.");
				}
			} else {
				if (x[i] == -1) {
					throw new IllegalArgumentException("x-vector has not expected values.");
				}
			}
		}

		if (definedCount != height) {
			throw new IllegalArgumentException("x-vector does not have the correct number of predefined variables.");
		}

		// Rückwärtseinsetzen von unten nach oben:
		for (int equation = numEquations - 1; equation >= 0; equation--) {
			int sum = 0; // Summe der bekannten Werte der Gleichung
			int unknownVariable = -1; // Index der unbekannten Variable der Gleichung
			int unknownCount = 0; // Anzahl der Unbekannten der Gleichung, falls nur 1 dann kann die Gleichung
									// gelöst werden

			// Durch die Zeile iterieren und bekannte/noch unbekannte Variablen sichten
			for (int variable = 0; variable < numVariables; variable++) {
				if (A[equation][variable] == 1) {
					if (x[variable] == -1) {
						unknownVariable = variable;
						unknownCount++;
					} else {
						sum += x[variable]; // bekannten Werte werden summiert, um danach potenziell die Gleichung zu
											// lösen
					}
				}
			}

			// Wenn genau eine Variable unbekannt ist, kann sie eindeutig bestimmt werden
			if (unknownCount == 1 && unknownVariable != -1) {
				x[unknownVariable] = (3 - (sum % 3)) % 3;
			}
		}

		return x;
	}

	/**
	 * Berechnet die row eines Buttons aus einem eindimensionalen Index.
	 * 
	 * @param index Der eindimensionale Index des Buttons.
	 * @return row des Buttons in der Pyramidenstruktur.
	 */
	private int getRowFromIndex(int index) {
		int row = 0;
		while ((row * (row + 1)) / 2 <= index) {
			row++;
		}
		return row - 1;
	}

	/**
	 * Berechnet die col eines Buttons aus einem eindimensionalen Index.
	 * 
	 * @param index Der eindimensionale Index des Buttons.
	 * @return col des Buttons in der Pyramidenstruktur.
	 */
	private int getColFromIndex(int index) {
		int row = getRowFromIndex(index);
		return index - (row * (row + 1)) / 2;
	}

	/**
	 * Berechnet den eindimensionalen Index eines Buttons basierend auf seiner
	 * Position in der Pyramide.
	 * 
	 * @param row row des Buttons.
	 * @param col col des Buttons.
	 * @return Der eindimensionale Index des Buttons.
	 */
	private int getIndexOfButton(int row, int col) {
		return (row * (row + 1)) / 2 + col;
	}

	/**
	 * Ermittelt die Nummer der gegebenen Farbe.
	 * 
	 * @param color Die zu suchende Farbe.
	 * @return Der Index der Farbe im Array, oder -1, falls die Farbe nicht gefunden
	 *         wurde (defaultColor).
	 */
	private int getColorNumber(Color color) {
		for (int i = 0; i < colors.length; i++) {
			if (colors[i].equals(color)) {
				return i; // 1.Farbe = 0, 2.Farbe = 1, 3.Farbe = 2
			}
		}
		return -1; // Defaultnummer für Ursprungsfarbe = -1, weil wir durch +1 zu 0 kommen und dass
					// die indexnummer für die erste Tricolor ist
	}

	/**
	 * Prueft, ob die gegebene Loesung korrekt ist.
	 * 
	 * @param possibleSolution Ein Array, das die moegliche Loesung repraesentiert.
	 * @return true, wenn die Loesung korrekt ist, false andernfalls.
	 */
	private boolean isSolutionCorrect(int[] possibleSolution) {
		int[] realSolution = new int[possibleSolution.length];
		for (int variable = 0; variable < possibleSolution.length; variable++) {
			if (variable < possibleSolution.length - height) {
				// Fuer Variablen, die nicht Teil der letzten "height" Werte sind, werden -1
				// gesetzt.
				realSolution[variable] = -1;
			} else {
				// Fuer die letzten "height" Variablen uebernehmen wir die Werte aus der
				// moeglichen Loesung.
				realSolution[variable] = possibleSolution[variable];
			}
		}

		realSolution = solveHomogeneousLinearSystemWithMatrixA(realSolution);
		return Arrays.equals(realSolution, possibleSolution);
	}

	// Beobachter (View, ...) werden registriert, entfernt oder benachrichtigt

	@Override
	public void registriereBeobachter(Beobachter b) {
		beobachter.add(b);
	}

	@Override
	public void entferneBeobachter(Beobachter b) {
		beobachter.remove(b);
	}

	@Override
	public void benachrichtigeBeobachter() {
		for (Beobachter b : beobachter) {
			b.aktualisiere(buttonColors, fixButtonColors, mode);
		}
	}

	//Getter für Test der Klasse
	
	public Color[][] getButtonColors() {
		return buttonColors;
	}

	public Color[][] getFixButtonColors() {
		return fixButtonColors;
	}

	public Mode getMode() {
		return mode;
	}

	public int getHeight() {
		return height;
	}

	public int[][] getA() {
		return A;
	}

	public int[][] getN() {
		return N;
	}

	public int[] getSolution() {
		return solution;
	}

	public List<Beobachter> getBeobachter() {
		return beobachter;
	}
}