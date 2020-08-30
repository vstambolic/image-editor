package user_interface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import image.CompositeOperation;
import image.Image;

@SuppressWarnings("serial")
public class WestPanel extends JPanel {

	private UserInterface ui;
	private final static String[] operationNames = { "brightness", "invert", "blacknwhite", "grayscale", "paint", "median", "composite", "animation" };
	private final static HashMap<String, JButton> operationsMap = new HashMap<>();

	private JDialog compositeOperationSelectionDialog;
	//private DefaultListModel<String> listModel;


	public WestPanel(UserInterface ui) {
		this.ui = ui;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(Color.DARK_GRAY);
		this.setPreferredSize(new Dimension(70, 800));
		//this.setBorder(BorderFactory.createLineBorder(new Color(0x4169e1), 1));

		Stream.of(operationNames).forEach(opName -> {
			JButton button = new JButton();
			//button.setBackground(new Color(0x22222));
			//button.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			button.setBackground(Color.DARK_GRAY);
			button.setForeground(Color.white);
			button.setMaximumSize(new Dimension(70, 70));
			if (opName == "composite") {
				button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
				button.setText("C");
			}
			else
				if (opName == "animation") {
					button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
					button.setText("A");
				}
			else {
					button.setIcon(new ImageIcon("icons//" + opName + ".png"));
			}
			//button.setEnabled(true);
			button.setFocusable(false);
			operationsMap.put(opName, button);
			this.add(button);
		});

		this.initCompositeOperationSelectionDialog();
		this.initActionListeners();

	}

	private void initActionListeners() {
		operationsMap.get("composite").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				compositeOperationSelectionDialog.setBounds(ui.getX() + ui.getWidth() / 2 - 250, ui.getY() + ui.getHeight() / 2 - 150, 500, 250);
				compositeOperationSelectionDialog.setVisible(true);
			}
		});
		operationsMap.get("animation").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Image.getImage().sharpenAnimation();
			}
		});
		operationsMap.get("blacknwhite").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Image.getImage().apply(CompositeOperation.BLACK_N_WHITE);

			}
		});
		operationsMap.get("grayscale").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Image.getImage().apply(CompositeOperation.GRAYSCALE);

			}
		});
		operationsMap.get("brightness").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Image.getImage().apply(CompositeOperation.ENLIGHTEN);

			}
		});
		operationsMap.get("invert").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Image.getImage().apply(CompositeOperation.INVERT);

			}
		});
		operationsMap.get("median").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Image.getImage().apply(CompositeOperation.BLUR);

			}
		});
		operationsMap.get("paint").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog dialog = new JDialog(ui, "Paint", true);
				dialog.setBounds(ui.getX() + ui.getWidth() / 2 - 175, ui.getY() + ui.getHeight() / 2 - 150, 350, 250);

				class MyTextField extends JTextField {
					public MyTextField() {
						this.setPreferredSize(new Dimension(40, 30));
						this.setMaximumSize(new Dimension(40, 30));
						this.setBackground(Color.DARK_GRAY);
						this.setForeground(Color.white);
						this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
						this.setHorizontalAlignment(SwingConstants.RIGHT);
						this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
						this.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
						this.setText("00");
						this.addKeyListener(new KeyAdapter() {
							public void keyTyped(KeyEvent event) {
								char c = event.getKeyChar();
								if (!Character.isDigit(c) && c != '\n' || MyTextField.this.getText().length() == 3) {
									event.consume();
								}
							}
						});
					}
				}

				JLabel rLbl = new JLabel("R"), gLbl = new JLabel("G"), bLbl = new JLabel("B");
				final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 22);
				rLbl.setFont(font);
				gLbl.setFont(font);
				bLbl.setFont(font);

				MyTextField rTextField = new MyTextField(), bTextField = new MyTextField(), gTextField = new MyTextField();

				JPanel leftPanel = new JPanel(new GridBagLayout());
				leftPanel.setOpaque(false);

				GridBagConstraints c = new GridBagConstraints();
				c.anchor = GridBagConstraints.EAST;

				JPanel rPanel = new JPanel();
				rPanel.setOpaque(false);
				rPanel.add(rLbl);
				rPanel.add(rTextField);
				JPanel gPanel = new JPanel();
				gPanel.setOpaque(false);
				gPanel.add(gLbl);
				gPanel.add(gTextField);
				JPanel bPanel = new JPanel();
				bPanel.setOpaque(false);
				bPanel.add(bLbl);
				bPanel.add(bTextField);

				c.gridx = 0;
				c.gridy = 0;
				leftPanel.add(rPanel, c);
				c.gridx = 0;
				c.gridy = 1;
				leftPanel.add(gPanel, c);
				c.gridx = 0;
				c.gridy = 2;
				leftPanel.add(bPanel, c);

				JButton applyBtn = new JButton("Apply");
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
						CompositeOperation paintOperation = new CompositeOperation("paint");
						int r = (rTextField.getText().length() == 0 ? 0 : Integer.parseInt(rTextField.getText()));
						int g = (gTextField.getText().length() == 0 ? 0 : Integer.parseInt(gTextField.getText()));
						int b = (bTextField.getText().length() == 0 ? 0 : Integer.parseInt(bTextField.getText()));
						paintOperation.addOperation("paint " + r + " " + g + " " + b);
						Image.getImage().apply(paintOperation);
						dialog.dispose();
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
						dialog.dispose();
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
				c = new GridBagConstraints();

				c.gridx = 1;
				c.gridy = 0;
				c.weightx = 0.25;
				tmpPanel.add(box, c);

				c.gridx = 0;
				c.gridy = 0;
				c.weightx = 0.75;
				tmpPanel.add(leftPanel, c);

				dialog.add(tmpPanel);

				dialog.setResizable(false);
				dialog.setVisible(true);

			}
		});

	}

	private void initCompositeOperationSelectionDialog() {
		compositeOperationSelectionDialog = new JDialog(ui, "Composite Operation Selection", true);
		DefaultListModel<String> listModel = CompositeOperation.listModel;
		JList<String> list = OperationGeneratorCard.createEmptyList(listModel);
		JScrollPane scroll = OperationGeneratorCard.createScrollPane(list);
		scroll.setMaximumSize(new Dimension(200, 200));

		JButton applyBtn = new JButton("Apply");
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
					Image.getImage().apply(CompositeOperation.compositeOperationsMap.get(list.getSelectedValue()));
					compositeOperationSelectionDialog.dispose();
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

}
