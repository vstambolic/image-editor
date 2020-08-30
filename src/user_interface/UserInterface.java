package user_interface;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class UserInterface extends JFrame {


	// Static Fields -----------------------------
	public static final Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
	public static final Dimension FRAME_DIMENSION = new Dimension(1200, 700);

	public static final String MAIN_CARD = "Main Card";
	public static final String OPERATION_GENERATOR_CARD = "Generator Card";

	// Fields ------------------------------------
	private JPanel cardWrapperPanel = new JPanel(new CardLayout());
	private MainCard mainCard;
	private OperationGeneratorCard operationGeneratorCard;

	public JPanel getCardWrapperPanel() {
		return this.cardWrapperPanel;
	}

	public OperationGeneratorCard getOperationGeneratorCard() {
		return operationGeneratorCard;
	}

	public MainCard getMainCard() {
		return mainCard;
	}
	// Constructor -------------------------------
	public UserInterface() {
		super("Photoshop For Poor");

		cardWrapperPanel.setBackground(Color.DARK_GRAY);
		this.add(cardWrapperPanel);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				new SaveProjectDialog(UserInterface.this);

			}
		});

		// MenuBar ---------------------------------------------
		this.setJMenuBar(new MenuBarInterface(this));
		this.cardWrapperPanel.add(mainCard = new MainCard(this), MAIN_CARD);
		this.cardWrapperPanel.add(operationGeneratorCard = new OperationGeneratorCard(this), OPERATION_GENERATOR_CARD);

		this.setBounds(500, 250, FRAME_DIMENSION.width, FRAME_DIMENSION.height);
		this.setMinimumSize(FRAME_DIMENSION);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		this.setVisible(true);
	}


	// Main -----------------------------------------------------

	public static void main(String[] args) {
		EventQueue.invokeLater(UserInterface::new);
	}


}
