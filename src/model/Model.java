package model;

/**
 * Das Model-Interface kapselt die wesentlichen Methoden, um ein Pyramiden-Puzzle zu erstellen.
 * Es verwaltet den Spielzustand und implementiert die zugrunde liegenden Spielregeln.
 */
public interface Model { 	
	/**
	 * Aendert die Farbe eines Buttons, der durch die Position (row, col) identifiziert wird.
	 *<p>
	 * Test-Modus: <br>
	 * - Wenn der Button bereits gefaerbt war, wird er veraendert. <br>
	 * - Andernfalls wird ueberprueft, ob die maximale Anzahl an faerbaren Buttons erreicht ist. <br>
	 *   Ist dies nicht der Fall, wird der Button gefaerbt. <br>
	 *</p>
	 *<p>
	 * Spiel-Modus:<br>
	 * - Die Farbe des Buttons wird geaendert. 
	 * </p>
	 *
	 * @param row Zeilenposition des Buttons
	 * @param col Spaltenposition des Buttons
	 */
	public void changeColor(int row, int col);
	
	/**
	 * Test-Modus: Alle Farben werden zurueckgesetzt.
	 * <p>
	 * Spiel-Modus: Alle Farben, ausser die der festen Buttons, werden zurueckgesetzt.
	 * </p>
	 */
	public void reset();
    
    /**
     * Ueberprueft den aktuellen Status des Puzzles und liefert eine entsprechende Nachricht.
     * Die Ueberpruefung erfolgt abhaengig vom Spielmodus (GAME_MODE oder TEST_MODE).
     *
     * @return eine {@link PuzzleMessage}, die den aktuellen Status (z. B. INCOMPLETE, CORRECT, INCORRECT) und eine passende Nachricht enthaelt
     * @throws NullPointerException falls der Modus null ist
     */
	public PuzzleMessage check();
    
    /**
     * Spiel-Modus: Startet ein neues Spiel, indem neue feste Buttons gesetzt werden 
     * und alle Farben zurueckgesetzt werden.
     */
	public void newGame();
    
    /**
     * Spiel-Modus: Loest das Spiel, indem alle Buttons entsprechend einer gueltigen Loesung gefaerbt werden.
     */
	public void solve();
    
    /**
     * Initialisiert die eingegebene Groesse als Hoehe des Pyramidenpuzzles.
     * @param height die Hoehe des Puzzles, die festgelegt wird
     */
	public void setHeight(int height);
    
    /**
     * Wechselt zwischen dem Test-Modus und Spiel-Modus
     */
	public void changeMode();
    
}
