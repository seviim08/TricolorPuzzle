package controller;

import model.Model;
import model.PuzzleMessage;


/**
 * Die Controller-Klasse implementiert die Strategie zur Steuerung des Modells.
 * <p>
 * Sie verarbeitet Benutzereingaben und führt die entsprechenden Änderungen am Model durch,
 * indem sie die Schnittstelle {@link Strategy} implementiert.
 * </p>
 */
public class Controller implements Strategy { 
	private Model model;

	/**
	 * Konstruktor für den Controller.
	 * <p>
	 * Initialisiert den Controller mit dem gegebenen Model, das gesteuert werden soll.
	 * </p>
	 * 
	 * @param model das Model, das der Controller steuern soll
	 */
	public Controller(Model model) { 
		this.model = model;
	}

	@Override
	public void changeColor(int row, int col) {
		model.changeColor(row, col); 
	}

	@Override
	public void reset() {
		model.reset(); 
	}

	@Override
	public PuzzleMessage check() {
		return model.check();
	}

	@Override
	public void newGame() {
		model.newGame();
	}

	@Override
	public void solve() {
		model.solve();
	}

	@Override
	public void changeMode() {
		model.changeMode();
	}

	@Override
	public void setHeight(int height) {
		model.setHeight(height);
	}
}
