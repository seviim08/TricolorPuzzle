package observer;

/**
 * Das Subjekt-Interface definiert die Methoden zur Verwaltung von Beobachtern. 
 * Beobachter wie die View koennen registriert, entfernt
 * und bei Aenderungen benachrichtigt werden.
 */
public interface Subjekt { 
	
	/**
	 * Registriert einen Beobachter am Subjekt.
	 * Diese Methode wird verwendet, um einen neuen Beobachter (hier nur die View) 
	 * in die Liste der Beobachter des Subjekts aufzunehmen. 
	 * 
	 * @param b der Beobachter, der registriert werden soll
	 */
	public void registriereBeobachter(Beobachter b);

	/**
	 * Entfernt einen Beobachter vom Subjekt.
	 * 
	 * Diese Methode entfernt einen zuvor registrierten Beobachter aus der Liste der Beobachter.
	 * Dadurch erhält der entfernte Beobachter keine weiteren Benachrichtigungen über Änderungen
	 * am Zustand des Subjekts.
	 * 
	 * @param b der Beobachter, der entfernt werden soll
	 */
	public void entferneBeobachter(Beobachter b);

	/**
	 * Benachrichtigt alle registrierten Beobachter über eine Zustandsänderung.
	 * Diese Methode wird aufgerufen, wenn sich der Zustand des Subjekts geändert hat.
	 * Sie iteriert durch alle registrierten Beobachter und informiert sie über die 
	 * Änderung, durch Aufruf der Methode {@code aktualisiere} des Beobachter-Interfaces.
	 */
	public void benachrichtigeBeobachter();

}
