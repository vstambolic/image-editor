package user_interface;

import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.MaskFormatter;

import image.CompositeOperation;

@SuppressWarnings("serial")
class MyCellRenderer extends JLabel implements ListCellRenderer<Object> {
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		String s = value.toString();
		setText(s);
		setEnabled(list.isEnabled());
		setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));
		setOpaque(true);
		setBorder(BorderFactory.createLineBorder(Color.black));

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		return this;
	}
}

@SuppressWarnings("serial")
public class OperationGeneratorCard extends Box {
	// Fields -----------------------------------
	private UserInterface ui;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private DefaultListModel<String> inputListDemo = new DefaultListModel();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private DefaultListModel<String> outputListDemo = new DefaultListModel();
	private JList<String> inputList;
	private CompositeOperation compositeOperation;

	// Constructor ------------------------------
	public OperationGeneratorCard(UserInterface ui) {
		super(BoxLayout.Y_AXIS);
		this.ui = ui;

		this.add(Box.createVerticalGlue());
		this.add(createCenterPanel());
		this.add(Box.createVerticalGlue());
	}

	private JPanel createCenterPanel() {
		JPanel centerPanel = new JPanel();

		final Dimension centerPanelDimension = new Dimension(UserInterface.FRAME_DIMENSION.width, UserInterface.FRAME_DIMENSION.height - 75);
		centerPanel.setPreferredSize(centerPanelDimension);
		centerPanel.setMaximumSize(centerPanelDimension);
		centerPanel.setMinimumSize(centerPanelDimension);
		centerPanel.setBackground(Color.GRAY);

		// Border
		TitledBorder titledBorder = new TitledBorder(BorderFactory.createStrokeBorder(new BasicStroke(5.0f)), "Composite Operations Generator");
		titledBorder.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
		titledBorder.setTitleJustification(TitledBorder.CENTER);
		centerPanel.setBorder(titledBorder);

		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createRigidArea(new Dimension(0, 50)));
		box.add(createCenterBox());
		box.add(Box.createRigidArea(new Dimension(0, 50)));

		centerPanel.add(box);

		return centerPanel;
	}

	private Box createCenterBox() {
		Box box = new Box(BoxLayout.X_AXIS);
		//box.setBackground(Color.DARK_GRAY);
		box.setOpaque(true); //
		final Dimension boxDimension = (new Dimension(920, 450));
		box.setPreferredSize(boxDimension);
		box.setMaximumSize(boxDimension);
		box.setMinimumSize(boxDimension);


		box.add(this.createLeftSector());
		box.add(Box.createRigidArea(new Dimension(40, 0)));
		box.add(this.createCenterSector());
		box.add(Box.createRigidArea(new Dimension(40, 0)));
		box.add(this.createRightSector());

		return box;
	}

	private ScrollPane createLeftSector() {
		inputList = createEmptyList(inputListDemo);
		CompositeOperation.NAME_TO_CODE_MAP.keySet().stream().forEachOrdered(str -> inputListDemo.addElement(str));


		ScrollPane scroll = new ScrollPane(inputList);

		final Dimension scrollDimension = new Dimension(300, 450);
		scroll.setPreferredSize(scrollDimension);
		scroll.setMinimumSize(scrollDimension);
		scroll.setMaximumSize(scrollDimension);

		return scroll;
	}

	private Box createCenterSector() {
		Box box = new Box(BoxLayout.Y_AXIS);
		box.setOpaque(true);
		final Dimension boxDimension = (new Dimension(240, 450));
		box.setPreferredSize(boxDimension);
		box.setMaximumSize(boxDimension);
		box.setMinimumSize(boxDimension);

		JLabel parametersLabel = new JLabel("Set parameter:");
		parametersLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		parametersLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		parametersLabel.setForeground(Color.BLACK);

		MaskFormatter mask = null;
		try {
			mask = new MaskFormatter("###.#");
			//mask.setPlaceholderCharacter('0');

		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFormattedTextField parametersTextField = new JFormattedTextField(mask);
		parametersTextField.setPreferredSize(new Dimension(70, 30));
		parametersTextField.setMaximumSize(new Dimension(70, 30));
		parametersTextField.setBackground(Color.DARK_GRAY);
		parametersTextField.setForeground(Color.white);
		parametersTextField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		parametersTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		parametersTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		parametersTextField.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		parametersTextField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent event) {
				char c = event.getKeyChar();
				if (!Character.isDigit(c) && c != '\n' /*|| parametersTextField.getText().length() >= 4*/) {
					event.consume();
				}
			}
		});

		Box innerBox = new Box(BoxLayout.X_AXIS);
		innerBox.add(parametersLabel);
		innerBox.add(Box.createRigidArea(new Dimension(20, 0)));
		innerBox.add(parametersTextField);

		JButton btn = new JButton("Add");
		btn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		btn.setFocusable(false);
		btn.setPreferredSize(new Dimension(120, 60));
		btn.setMaximumSize(new Dimension(120, 60));
		btn.setBackground(Color.DARK_GRAY);
		btn.setForeground(Color.WHITE);
		btn.setAlignmentX(CENTER_ALIGNMENT);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String str = inputList.getSelectedValue();
				String code = CompositeOperation.NAME_TO_CODE_MAP.get(str);
				if (str != null) {
					if (CompositeOperation.doesntNeedArg(str))
						outputListDemo.addElement(str);
					else {
						String arg;
						System.out.println("'" + parametersTextField.getText() + "'");
						if (parametersTextField.getText().equals("   . ")) {
							arg = String.valueOf(CompositeOperation.DEFAULT_ARG);
						}
						else {
							arg = parametersTextField.getText();
						}
						outputListDemo.addElement(str + " " + arg);
						code += " " + arg;
					}

				}
				OperationGeneratorCard.this.compositeOperation.addOperation(code);

			}
		});

		box.add(Box.createRigidArea(new Dimension(0, 45))); //150

		box.add(innerBox);
		box.add(Box.createRigidArea(new Dimension(0, 30)));
		box.add(btn);

		box.add(Box.createVerticalGlue());

		return box;

	}

	private JPanel createRightSector() {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.setBackground(Color.GRAY);

		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		btnSave.setFocusable(false);
		btnSave.setPreferredSize(new Dimension(120, 60));
		btnSave.setMaximumSize(new Dimension(120, 60));
		btnSave.setBackground(Color.DARK_GRAY);
		btnSave.setForeground(Color.WHITE);
		btnSave.setAlignmentX(CENTER_ALIGNMENT);
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				outputListDemo.removeAllElements();
				CompositeOperation.addNewCompositeOperation(compositeOperation);
				((CardLayout) ui.getCardWrapperPanel().getLayout()).show(ui.getCardWrapperPanel(), UserInterface.MAIN_CARD);
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		btnCancel.setFocusable(false);
		btnCancel.setPreferredSize(new Dimension(120, 60));
		btnCancel.setMaximumSize(new Dimension(120, 60));
		btnCancel.setBackground(Color.DARK_GRAY);
		btnCancel.setForeground(Color.WHITE);
		btnCancel.setAlignmentX(CENTER_ALIGNMENT);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				outputListDemo.removeAllElements();

				((CardLayout) ui.getCardWrapperPanel().getLayout()).show(ui.getCardWrapperPanel(), UserInterface.MAIN_CARD);
			}
		});

		//box.add(Box.createRigidArea(rigidDimension));
		Box horizontalBox = new Box(BoxLayout.X_AXIS);
		Box verticalBox = new Box(BoxLayout.Y_AXIS);
		verticalBox.add(Box.createRigidArea(new Dimension(0, 35)));
		verticalBox.add(horizontalBox);
		horizontalBox.add(btnSave);
		horizontalBox.add(Box.createHorizontalGlue());
		horizontalBox.add(btnCancel);

		JList<String> outputList = createEmptyList(outputListDemo);
		panel.add(new ScrollPane(outputList));
		panel.add(verticalBox);

		return panel;
	}

	// List of operations -------------------------------------
	public static JList<String> createEmptyList(DefaultListModel<String> demoList) {
		JList<String> list = new JList<String>(demoList);

		list.setBackground(Color.DARK_GRAY);
		list.setForeground(Color.white);
		list.setSelectionBackground(new Color(0x111111));
		list.setSelectionForeground(Color.white);

		list.setCellRenderer(new MyCellRenderer());

		return list;
	}

	public static JScrollPane createScrollPane(JList<String> list) {

		JScrollPane scroll = new JScrollPane(list);
		JScrollBar scrollbar = new JScrollBar();
		scrollbar.setUI(new BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = Color.BLACK;
				this.trackColor = new Color(0x777777);
			}

			@Override
			protected JButton createDecreaseButton(int orientation) {
				JButton button = super.createDecreaseButton(orientation);
				button.setBackground(Color.BLACK);
				button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				return button;
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				JButton button = super.createIncreaseButton(orientation);
				button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				button.setBackground(Color.BLACK);
				return button;
			}

		});
		scrollbar.setForeground(Color.blue);
		scroll.setVerticalScrollBar(scrollbar);

		scroll.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(4.0f), Color.black));

		return scroll;
	}

	public void setCompositeOperation(CompositeOperation compositeOperation) {
		this.compositeOperation = compositeOperation;
	}
}
