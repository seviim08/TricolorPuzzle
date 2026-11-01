package view;

import java.awt.Color;

import javax.swing.JOptionPane;

import model.Mode;
import controller.Strategy;
import observer.Beobachter;
import observer.Subjekt;

/**
 * Implementiert das Beobachter-Interface. Reagiert auf Aenderungen des Modells
 * und aktualisiert die Darstellung.
 */
public class View implements Beobachter {
	
	/**
	 *  Referenz auf das Gui-Objekt, das die grafische Benutzeroberfläche des Spiels darstellt.
	 */
	private Gui frame;

	/**
	 * Registriert die View als Beobachter beim Subjekt. Fragt die Hoehe der
	 * Pyramide vom Benutzer ab. Danach wird die GUI erstellt, welche eine leere Pyramide gemaeß der 
	 * angegebenen Hoehe baut. Dann wird die Hoehe ueber den Controller auch im Model gesetzt. 
	 * Dann wird die GUI sichtbar gemacht.
	 *
	 * @param subjekt    Das Subjekt, bei dem die View als Beobachter registriert
	 *                   wird.
	 * @param controller Der Controller, ueber die die Hoehe der Pyramide gesetzt
	 *                   wird.
	 */
	public View(Subjekt subjekt, Strategy controller) {
		subjekt.registriereBeobachter(this);
		int height = requestPyramidHeight();
		try {
			if (height != -1) { 
				// Erstellt die GUI, die eine leere Pyramide mit der angegebenen Hoehe darstellt
				frame = new Gui(controller, height);
				// Setzt die Hoehe des Modells durch den Controller auf height
				controller.setHeight(height);
				// Macht die GUI sichtbar
				frame.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fragt die Hoehe der Pyramide vom Benutzer ab.
	 * @return Die eingegebene Hoehe oder -1, wenn der Benutzer abbricht.
	 */
	private int requestPyramidHeight() {
		int height = 0;
		boolean validInput = false;
		while (!validInput) {
			try {
				String input = JOptionPane.showInputDialog(null, "Please enter the height of the tricolor puzzle:",
						"Enter height", JOptionPane.QUESTION_MESSAGE);
				if (input == null) {
					return -1; // Bei Abbruch Wert -1
				}
				height = Integer.parseInt(input);
				if (height > 1 && height < 19) {
					validInput = true;
				} else {
					JOptionPane.showMessageDialog(null, "The height must be between 2 and 18", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Please enter a valid number!", "Invalid Input",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		return height;
	}

	@Override
	public void aktualisiere(Color[][] buttonColors, Color[][] fixButtonColors, Mode mode) {
		frame.aktualisiere(buttonColors, fixButtonColors, mode); 
	}
}
