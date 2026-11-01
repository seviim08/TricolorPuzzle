package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Mode;
import controller.Strategy;
import model.PuzzleStatus;
import model.PuzzleMessage;

/**
 * Die Gui-Klasse erweitert JFrame und stellt die grafische Benutzeroberflaeche 
 * für das Tricoloring-Puzzle bereit.
 * Sie verwaltet die Darstellung der Pyramide und ermöglicht die Interaktion des 
 * Endbenutzers mit dem Spiel.
 */
public class Gui extends JFrame {
	private static final long serialVersionUID = 1L;
	Strategy strategy;
	
	private JPanel contentPanel;
    private JButton[][] buttons;
    private Mode mode;

    private JButton resetButton;
    private JButton checkButton;
    private JButton solveButton; 
    private JButton newGameButton;
    private JButton changeModeButton; 

    /**
     * Erstellt eine GUI, die eine Pyramide mit der angegebenen Hoehe darstellt. 
     * Die Hoehe bestimmt die Anzahl der Ebenen der Pyramide. Der uebergebene Controller 
     * (Strategie) wird verwendet, um das Verhalten des Spiels zu steuern.
     * 
     * @param controller Die Strategie oder der Controller, der das Verhalten der GUI steuert.
     * @param height     Die Hoehe der Pyramide in Ebenen.
     */   
	public Gui(Strategy controller, int height) {  
        strategy = controller;
		setTitle("Tricoloring Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); //macht ein Rand um den ganzen Panel
		setContentPane(contentPanel);
		contentPanel.setLayout(new BorderLayout(0, 0)); //Borderlayout wird kreiert; 0 und 0 stehen für den Abstand zwischen den Komponenten

        // Pyramidpanel wird im Center des Borderlayouts gemacht
        JPanel pyramidPanel = new JPanel();
        
        pyramidPanel.setLayout(new GridLayout(18,1,2,2));
        add(pyramidPanel, BorderLayout.CENTER); // PyramidenPanel wird in der Mitte platziert

        // Buttons der Pyramide werden hinzugefügt/ initialisiert
        buttons = new JButton[height][];
        for (int i = 0; i < height; i++) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0)); //Durch Flowlayout werden die Buttons in jeder Zeile mittig platziert
            buttons[i] = new JButton[i + 1]; //für jede Zeile 0,1,2,... gibt es 1,2,3,.. Buttons
            for (int j = 0; j <= i; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(40, 40)); 
                button.addActionListener(new ChangeColorListener(i, j)); //Action für die PyramidenButtons
                buttons[i][j] = button;
                rowPanel.add(button);
            }
            pyramidPanel.add(rowPanel); // Panel für jede Zeile hinzufügen
        }
        
        
        //unteren Buttons
        JPanel buttom = new JPanel();
		contentPanel.add(buttom, BorderLayout.SOUTH);
		
		changeModeButton = new JButton("Switch to Test Mode"); // Wechselt Spielmodus
		changeModeButton.addActionListener(new ChangeModeListener()); 
        buttom.add(changeModeButton);
     
        resetButton = new JButton("Reset");// Resetbutton
        resetButton.addActionListener(new ResetButtonListener());
        buttom.add(resetButton);
        
        newGameButton = new JButton("New Game"); // Neues-Spiel-Button
        newGameButton.addActionListener(new NewGameButtonListener()); 
        buttom.add(newGameButton);
        
        //seitliche Buttons
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS)); 
		contentPanel.add(right, BorderLayout.EAST);
		
		checkButton = new JButton("Check"); // Checkbutton
        checkButton.addActionListener(new CheckButtonListener(this));  
        right.add(checkButton);
		
		solveButton = new JButton("Solve");// Solvebutton
        solveButton.addActionListener(new SolveButtonListener()); 
        right.add(solveButton);

        //Setzt Grösse der Frame und zentriert es in die Mitte des Bildschirms
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(890,890);
        Dimension frameSize = getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        setLocation(x, y);
    }

	/**
	 * Aktualisiert die GUI basierend auf den neuen Buttonfarben, fixierten Buttons und dem aktuellen Modus.
	 * 
	 * Diese Methode wird verwendet, um die Farben der Buttons in der Pyramide zu aktualisieren und 
	 * die fixierten Buttons mit einem Rand zu kennzeichnen. Der aktuelle Spielmodus wird ebenfalls 
	 * aktualisiert, da die GUI abhaengig vom Spielmodus gewisse Buttons zeigt und unsichtbar macht.
	 *
	 * @param buttonColors    Ein zweidimensionales Array, das die neuen Farben der Buttons in der Pyramide enthält
	 * @param fixButtonColors Ein zweidimensionales Array, das die fixierten Buttons kennzeichnet
	 * @param mode            Der aktuelle Modus des Spiels.
	 */
    public void aktualisiere(Color[][] buttonColors, Color[][] fixButtonColors, Mode mode) { //Nur die Buttonfarben werden aktualisiert bei jedem Button den man drückt
        this.mode = mode;
    	for (int i = 0; i < buttonColors.length; i++) {
            for (int j = 0; j < buttonColors[i].length; j++) {
            	//buttonFarben werden aktualisiert
                buttons[i][j].setBackground(buttonColors[i][j]);
                //neue FixButtons werden umrandet
                if(fixButtonColors[i][j] != null) {
					buttons[i][j].setBorder(new LineBorder(Color.BLACK, 5));
                } else {
					buttons[i][j].setBorder(new LineBorder(Color.BLACK, 0));
                }
            }
        }
    }
    
    /**
     * Listener, der die Farbe eines Buttons in der Pyramide ändert
     */
    class ChangeColorListener implements ActionListener {
        private final int row;
        private final int col;

        public ChangeColorListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
		public void actionPerformed(ActionEvent e) {
			strategy.changeColor(row, col);
		}
    }

    /**
     * Listener, der das Spielfeld auf den Ursprungszustand zuruecksetzt
     */
    class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            strategy.reset();
        }
    }

    /**
     * Listener, der ein neues Spiel startet
     */
	class NewGameButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			strategy.newGame();
		}
	}

	/**
	 * Listener, der die aktuelle Lösung überprüft und entsprechende Nachrichten anzeigt
	 */
    class CheckButtonListener implements ActionListener {
        private final JFrame parentFrame;

        public CheckButtonListener(JFrame parentFrame) {
            this.parentFrame = parentFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            PuzzleMessage puzzleMessage = strategy.check();
            PuzzleStatus puzzleStatus = puzzleMessage.getStatus();
            String message = puzzleMessage.getMessage();

            switch (puzzleStatus) {
            case CORRECT: 
            	//check icon
                ImageIcon originalCheckIcon = new ImageIcon(Gui.class.getResource("check.png"));
                Image scaledImage = originalCheckIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); //breite und höhe des icons in Pixel
                Icon greenCheckIcon = new ImageIcon(scaledImage);
                
            	JOptionPane.showMessageDialog(parentFrame,
            			message,
                	    ":)",
                	    JOptionPane.PLAIN_MESSAGE,
                	    greenCheckIcon);
            	break;
            case INCOMPLETE:
            	JOptionPane.showMessageDialog(parentFrame,
            			message,
                	    ":/",
                	    JOptionPane.WARNING_MESSAGE);
            	break;
            case INCORRECT:
            	JOptionPane.showMessageDialog(parentFrame,
            			message,
                	    ":(",
                	    JOptionPane.ERROR_MESSAGE);
            	break;

			default:
				break;
			}
                      
        }
    }

    /**
     * Listener, der das Spiel automatisch löst
     */
    class SolveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            strategy.solve();
        }
    }
    
    /**
     * Listener, der zwischen den Modi (Spielmodus und Testmodus) wechselt
     */
    class ChangeModeListener implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent e) {
			strategy.changeMode();
			switch (mode) {
			case GAME_MODE:
				changeModeButton.setText("Switch to Test Mode");
				solveButton.setVisible(true);
				newGameButton.setVisible(true);
				break;
			case TEST_MODE:
				changeModeButton.setText("Switch to Game Mode");
				solveButton.setVisible(false);
				newGameButton.setVisible(false);
				break;
			default:
				break;
			}
        }
    }
    
   
}
