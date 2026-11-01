import controller.Controller;
import controller.Strategy;
import model.Model;
import model.TricoloringPuzzle;
import observer.Subjekt;
import view.View;

/**
 * Die Anwendungsklasse dient als Einstiegspunkt fuer das Programm.
 * Sie initialisiert die Hauptkomponenten und startet das Spiel durch den Aufruf der Main-Methode.
 */
public class Anwendung {
	/**
	 * Die Main-Methode dient als Startpunkt der Anwendung.
	 * <ul>
	 *   <li>Erstellt ein Subjekt, das auch die Model-Schnittstelle implementiert.</li>
	 *   <li>Initialisiert einen Controller, der Benutzereingaben verarbeitet und das Model steuert.</li>
	 *   <li>Erzeugt eine View, die sich als Beobachter beim Subjekt registriert und den Controller erhaelt, um Benutzerinteraktionen zu ermöglichen.</li>
	 * </ul>
	 */
    public static void main(String[] args) {
    	Subjekt pyramid = new TricoloringPuzzle(); 
    	Strategy controller = new Controller((Model)pyramid); 
        new View(pyramid, controller); 
    }
}