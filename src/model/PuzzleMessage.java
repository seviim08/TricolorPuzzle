package model;

/**
 * Die Klasse PuzzleMessage repraesentiert eine Nachricht, die den Status des Puzzles
 * und die zugehoerige Nachricht enthaelt.
 */
public class PuzzleMessage {
    private final PuzzleStatus status;
    private final String message;

    /**
    * Konstruktor zur Initialisierung einer PuzzleMessage mit einem Status und einer Nachricht.
    *
    * @param status der Status des Puzzles (z. B. INCOMPLETE, CORRECT, INCORRECT)
    * @param message die Nachricht, die den Status beschreibt
    */
    public PuzzleMessage(PuzzleStatus status, String message) {
        this.status = status;
        this.message = message;
    } 

    /**
     * Liefert den Status des Puzzles.
     * @return der Status als {@link PuzzleStatus}
     */
    public PuzzleStatus getStatus() {
        return status;
    }

    /**
     * Liefert die Nachricht, die den Status beschreibt.
     * @return die Nachricht als String
     */
    public String getMessage() {
        return message;
    }
}
