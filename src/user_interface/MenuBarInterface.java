package user_interface;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import formatters.Formatter.FormatNotSupported;
import image.CompositeOperation;
import image.Image;

public class MenuBarInterface extends JMenuBar {
	// Serialization -------------------------------------
	private static final long serialVersionUID = 1L;

	// Fields --------------------------------------------
	UserInterface ui;
	JMenu fileMenu;
	JMenu operationsMenu;
	JMenu helpMenu;

	private JDialog compositeOperationSelectionDialog;

	// Constructor --------------------------------------------

	public MenuBarInterface(UserInterface ui) {
		this.ui = ui;
		this.initFileMenu();
		this.initOperationsMenu();
		this.initHelpMenu();
	}

	// Operations Menu -----------------------------------------------------------------
	private void initOperationsMenu() {
		JMenu operationsMenu = new JMenu("Composite operations");
		operationsMenu.setFont(UserInterface.DEFAULT_FONT);

		JMenuItem menuItem_CreateComp = new JMenuItem("New");
		menuItem_CreateComp.setFont(UserInterface.DEFAULT_FONT);
		menuItem_CreateComp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

				JDialog dialog = new JDialog(ui, "New Composite Operation", true);
				dialog.setBounds(ui.getX() + ui.getWidth() / 2 - 175, ui.getY() + ui.getHeight() / 2 - 60, 350, 120);

				Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);

				JPanel tmpPanel = new JPanel();
				tmpPanel.setLayout(new BoxLayout(tmpPanel, BoxLayout.Y_AXIS));
				tmpPanel.setBackground(Color.DARK_GRAY);

				JLabel label = new JLabel("Operation name:");
				label.setForeground(Color.white);
				label.setFont(font);
				label.setAlignmentY(CENTER_ALIGNMENT);
				label.setVerticalAlignment(SwingConstants.CENTER);
				JTextField textField = new JTextField();
				textField.setMinimumSize(new Dimension(150, 30));
				textField.setPreferredSize(new Dimension(150, 30));
				textField.setMaximumSize(new Dimension(150, 30));

				textField.setBackground(Color.DARK_GRAY);
				textField.setForeground(Color.white);
				textField.setFont(font);
				textField.setHorizontalAlignment(SwingConstants.RIGHT);
				textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				textField.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
				textField.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dialog.dispose();
						if (textField.getText().length() != 0) {
							ui.getOperationGeneratorCard().setCompositeOperation(new CompositeOperation(textField.getText()));
						((CardLayout) ui.getCardWrapperPanel().getLayout()).show(ui.getCardWrapperPanel(), UserInterface.OPERATION_GENERATOR_CARD);
						}
					}
				});

				JPanel midPanel = new JPanel();
				midPanel.add(label);
				midPanel.add(textField);
				midPanel.setOpaque(false);

				tmpPanel.add(Box.createVerticalGlue());
				tmpPanel.add(midPanel);
				tmpPanel.add(Box.createVerticalGlue());

				dialog.add(tmpPanel);
				dialog.setResizable(false);
				dialog.setVisible(true);

			}
		});
		JMenuItem menuItem_ImportComp = new JMenuItem("Import");
		menuItem_ImportComp.setFont(UserInterface.DEFAULT_FONT);
		menuItem_ImportComp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				int rVal = c.showOpenDialog(ui);
				if (rVal == JFileChooser.APPROVE_OPTION && Pattern.matches(".*\\.fun", c.getSelectedFile().toString())) {
					CompositeOperation.importFromFile(c.getSelectedFile().toString());
				}
			}
		});

		initCompositeOperationSelectionDialog();
		JMenuItem menuItem_ExportComp = new JMenuItem("Export");
		menuItem_ExportComp.setFont(UserInterface.DEFAULT_FONT);
		menuItem_ExportComp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				compositeOperationSelectionDialog.setBounds(ui.getX() + ui.getWidth() / 2 - 250, ui.getY() + ui.getHeight() / 2 - 150, 500, 250);
				compositeOperationSelectionDialog.setVisible(true);
			}
		});
		operationsMenu.add(menuItem_CreateComp);
		operationsMenu.add(menuItem_ImportComp);
		operationsMenu.add(menuItem_ExportComp);

		this.add(operationsMenu);
	}

	private void initCompositeOperationSelectionDialog() {
		compositeOperationSelectionDialog = new JDialog(ui, "Composite Operation Selection", true);
		DefaultListModel<String> listModel = CompositeOperation.listModel;
		JList<String> list = OperationGeneratorCard.createEmptyList(listModel);
		JScrollPane scroll = OperationGeneratorCard.createScrollPane(list);
		scroll.setMaximumSize(new Dimension(200, 200));

		JButton applyBtn = new JButton("Export");
		applyBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		applyBtn.setFocusable(false);
		applyBtn.setPreferredSize(new Dimension(120, 60));
		applyBtn.setMaximumSize(new Dimension(120, 60));
		applyBtn.setMinimumSize(new Dimension(120, 60));
		applyBtn.setBackground(Color.DARK_GRAY);
		applyBtn.setForeground(Color.WHITE);
		applyBtn.setAlignmentX(CENTER_ALIGNMENT);
		applyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

					if (list.getSelectedValue() != null) {
						JFileChooser c = new JFileChooser();
						int rVal = c.showSaveDialog(ui);
						if (rVal == JFileChooser.APPROVE_OPTION) {
							CompositeOperation.compositeOperationsMap.get(list.getSelectedValue()).exportToFile(c.getSelectedFile().toString());
						compositeOperationSelectionDialog.dispose();
						}
					}

			}
		});
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		cancelBtn.setFocusable(false);
		cancelBtn.setPreferredSize(new Dimension(120, 60));
		cancelBtn.setMaximumSize(new Dimension(120, 60));
		cancelBtn.setMinimumSize(new Dimension(120, 60));

		cancelBtn.setBackground(Color.DARK_GRAY);
		cancelBtn.setForeground(Color.WHITE);
		cancelBtn.setAlignmentX(CENTER_ALIGNMENT);
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				compositeOperationSelectionDialog.dispose();
			}
		});

		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalGlue());
		box.add(applyBtn);
		box.add(Box.createRigidArea(new Dimension(0, 30)));
		box.add(cancelBtn);
		box.add(Box.createVerticalGlue());

		JPanel tmpPanel = new JPanel(new GridBagLayout());
		tmpPanel.setBackground(Color.GRAY);
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.25;
		tmpPanel.add(box, c);

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.75;
		c.fill = GridBagConstraints.VERTICAL;
		c.ipadx = 200;

		tmpPanel.add(scroll, c);

		compositeOperationSelectionDialog.add(tmpPanel);

		compositeOperationSelectionDialog.setResizable(false);
	}

	// File Menu --------------------------------------------------------
	private void initFileMenu() {
		this.fileMenu = new JMenu("File");
		fileMenu.setFont(UserInterface.DEFAULT_FONT);

		JMenuItem menuItem_New = new JMenuItem("New Project");
		menuItem_New.setFont(UserInterface.DEFAULT_FONT);
		menuItem_New.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Image.getImage().clear();
			}
		});
		JMenuItem menuItem_Open = new JMenuItem("Open File");
		menuItem_Open.setFont(UserInterface.DEFAULT_FONT);
		menuItem_Open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				int rVal = c.showOpenDialog(MenuBarInterface.this.ui);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					try {
					
						Image.getImage().load(c.getSelectedFile().toString());
				
					}
					catch (IOException | FormatNotSupported ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
			}
			}
		});

		JMenuItem menuItem_SaveAs = new JMenuItem("Save Project As...");
		menuItem_SaveAs.setFont(UserInterface.DEFAULT_FONT);
		menuItem_SaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				// Demonstrate "Save" dialog:
				int rVal = c.showSaveDialog(MenuBarInterface.this.ui);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					try {
						Image.getImage().save(c.getSelectedFile().toString());
					}
					catch (FormatNotSupported e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		JMenuItem menuItem_Exit = new JMenuItem("Exit");
		menuItem_Exit.setFont(UserInterface.DEFAULT_FONT);
		menuItem_Exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new SaveProjectDialog(ui);
			}
		});
		fileMenu.add(menuItem_New);
		fileMenu.add(menuItem_Open);
		fileMenu.add(menuItem_SaveAs);
		fileMenu.addSeparator();
		fileMenu.add(menuItem_Exit);

		this.add(fileMenu);
	}

	// Help Menu ----------------------------------------------------------------------
	private void initHelpMenu() {
		helpMenu = new JMenu("Help");
		helpMenu.setFont(UserInterface.DEFAULT_FONT);

		JMenuItem menuItem_Help = new JMenuItem("No Help");
		menuItem_Help.setFont(UserInterface.DEFAULT_FONT);
		helpMenu.add(menuItem_Help);

		this.add(helpMenu);
	}

}
