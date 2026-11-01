package controller;

import model.PuzzleMessage;

/**
 * Interface fuer Strategien, die der Endbenutzer im Spiel verwenden kann.
 * Die Methoden rufen intern entsprechende Aktionen im Model auf.
 */
public interface Strategy { 
	 /**
     * Delegiert die Aktion des Farbwechselns eines Buttons an das Model. 
     * Der Button wird durch die angegebene Zeile und Spalte identifiziert. 
     * Wird durch die Buttons des Pyramidenpuzzles ausgeloest.
     *
     * @param row die Zeilenposition des Buttons
     * @param col die Spaltenposition des Buttons
     */
	public void changeColor(int row, int col);
    
    /**
     * Delegiert die Aktion des Zuruecksetzens aller Farben an das Model.
     * Wird durch den Reset-Button ausgeloest.
     */
	public void reset();
    
    /**
     * Delegiert die Pruefung der aktuellen Loesung des Puzzles an das Model
     * und gibt den entsprechenden Status zurueck. Wird vom Check-Button ausgeloest
     *
     * @return der PuzzleStatus, der den Zustand der aktuellen Loesung widerspiegelt
     */
	public PuzzleMessage check();
    
    /**
     * Delegiert das Starten eines neuen Spiels an das Model.
     * Wird ausgeloest, wenn der Endbenutzer den "New Game"-Button drueckt.
     */
	public void newGame();
    
    /**
     * Delegiert das Wechseln des aktuellen Modus an das Model.
     * Wird ausgeloest, wenn der Endbenutzer den Modus wechseln moechte.
     */
	public void changeMode();
    
    /**
     * Delegiert das automatische Loesen des Puzzles an das Model.
     * Wird ausgeloest, wenn der Solve-Button gedrueckt wird.
     */
	public void solve();
    
    /**
     * Delegiert die Festlegung der Hoehe des Puzzles an das Model.
     * Die Hoehe wird vom Endbenutzer eingegeben.
     *
     * @param height die vom Endbenutzer festgelegte Hoehe des Puzzles
     */
	public void setHeight(int height);
}
