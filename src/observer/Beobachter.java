package observer;

import java.awt.Color;
import model.Mode;

/**
 * Beobachter registrieren sich bei einem Subjekt und werden benachrichtigt,
 * wenn sich dessen Zustand ändert.
 */
public interface Beobachter {
	/**
	 * Diese Methode wird verwendet, um die Farben der Buttons in der Pyramide zu
	 * aendern und fixierte Buttons gegebenenfalls neu zu markieren. Aenderungen an
	 * den Buttons werden direkt im Frame dargestellt.
	 *
	 * @param buttonColors    Ein zweidimensionales Array, das die neuen Farben der
	 *                        Buttons in der Pyramide repräsentiert.
	 * @param fixButtonColors Ein zweidimensionales Array, das die fixierten Buttons
	 *                        kennzeichnet.
	 * @param mode            Der Modus, der bestimmt, welche Buttons sichtbar sind.
	 */
	public void aktualisiere(Color[][] buttonColors, Color[][] fixButtonColors, Mode mode);
}
