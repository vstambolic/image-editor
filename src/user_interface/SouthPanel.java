package user_interface;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import image.Image;
import image.Layer;
import image.Selection;

@SuppressWarnings("serial")
class StatusInfoPanel extends JPanel {
	private JLabel statusLabel;
	private JPanel midPanel = new JPanel();

	public StatusInfoPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setOpaque(false);
		this.statusLabel = new JLabel("Image Editor.exe");
		//this.statusLabel = new JLabel("PHOTOSHOP ZA SIROMASNE...");
		//		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		//		statusLabel.setVerticalAlignment(SwingConstants.CENTER);
		statusLabel.setAlignmentX(CENTER_ALIGNMENT);
		statusLabel.setAlignmentY(CENTER_ALIGNMENT);
		statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
		statusLabel.setForeground(Color.BLACK);

		midPanel.add(statusLabel);
		midPanel.setOpaque(false);
		midPanel.setAlignmentX(CENTER_ALIGNMENT);
		midPanel.setAlignmentY(CENTER_ALIGNMENT);
		this.add(Box.createRigidArea(new Dimension(0, 45)));
		this.add(midPanel);
		this.add(Box.createVerticalGlue());

	}

	public void update(String string) {
		this.midPanel.removeAll();
		this.midPanel.add(statusLabel);
		statusLabel.setText(string);


	}

	public void update(String string, JComponent component) {
		statusLabel.setText(string);
		midPanel.add(component);
	}
}

@SuppressWarnings("serial")
class SelectionInfoPanel extends JPanel {
	private Selection curSelection;
	TitledBorder titledBorder;
	private JCheckBox selActiveCheck;
	private SouthPanel southPanel;
	private JCheckBox showCheck;
	
	public SelectionInfoPanel(SouthPanel parent) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.southPanel = parent;
		titledBorder = new TitledBorder(BorderFactory.createEmptyBorder()/*BorderFactory.createStrokeBorder(new BasicStroke(5.0f))*/, "Selection sajdsisdhaisdh");
		titledBorder.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		this.setBorder(titledBorder);
		this.setOpaque(false);

		JPanel tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tmpPanel.setOpaque(false);
		final JLabel selLbl = new JLabel("Active:");
		selLbl.setAlignmentY(CENTER_ALIGNMENT);
		selLbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));

		selActiveCheck = new JCheckBox();
		selActiveCheck.setOpaque(false);
		selActiveCheck.setSelected(true);
		selActiveCheck.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (selActiveCheck.isSelected())
					Image.getImage().setSelectionActive(curSelection.getName());
				else
					Image.getImage().setSelectionInactive(curSelection.getName());
			}
		});

		final JLabel showlLbl = new JLabel("        Show:");
		showlLbl.setAlignmentY(CENTER_ALIGNMENT);
		showlLbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));

		showCheck = new JCheckBox();
		showCheck.setOpaque(false);
		showCheck.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println(showCheck.isSelected());
				if (showCheck.isSelected())
					SelectionInfoPanel.this.southPanel.getMainPanel().getImagePanel().setSelection(curSelection);
				else
					SelectionInfoPanel.this.southPanel.getMainPanel().getImagePanel().setSelection(null);

				SelectionInfoPanel.this.southPanel.getMainPanel().getImagePanel().repaint();

			}
		});
		tmpPanel.add(selLbl);
		tmpPanel.add(selActiveCheck);
		tmpPanel.add(showlLbl);
		tmpPanel.add(showCheck);
		this.add(Box.createVerticalGlue());
		this.add(tmpPanel);
		this.add(Box.createVerticalGlue());

	}
	public void update(Selection selection) {
		this.curSelection = selection;
		showCheck.setSelected(true);
		this.titledBorder.setTitle("Selection \"" + selection.getName() + "\"");
		this.selActiveCheck.setSelected(true);
		
		this.repaint();
	}
}

@SuppressWarnings("serial")
class LayerInfoPanel extends JPanel {

	private SouthPanel parent;

	private TitledBorder titledBorder;
	private JLabel srcVal;
	private JLabel dimVal;

	private JTextField yCoord;
	private JTextField xCoord;

	private JTextField opacityTextField;
	private JCheckBox visCheck;
	private JCheckBox actCheck;
	private Layer curLayer;

	public LayerInfoPanel(SouthPanel parent) {
		super(new BorderLayout());
		this.parent = parent;

		titledBorder = new TitledBorder(BorderFactory.createEmptyBorder()/*BorderFactory.createStrokeBorder(new BasicStroke(4.0f))*/, "Layer x");

		//TitledBorder titledBorder = new TitledBorder(BorderFactory.createEmptyBorder(), "Layer x");
		titledBorder.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		this.setBorder(titledBorder);
		this.setOpaque(false);

		JPanel info1 = new JPanel(new GridBagLayout());
		info1.setOpaque(false);

		final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 22);

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		final JLabel srcLbl = new JLabel("Source file: ");
		srcLbl.setFont(font);
		c.gridx = 0;
		c.gridy = 0;
		info1.add(srcLbl, c);

		final JLabel dimLbl = new JLabel("Dimensions: ");
		dimLbl.setFont(font);

		c.gridx = 0;
		c.gridy = 1;
		info1.add(dimLbl, c);

		final JLabel posLbl = new JLabel("Position: ");
		posLbl.setFont(font);
		c.gridx = 0;
		c.gridy = 2;
		info1.add(posLbl, c);

		srcVal = new JLabel("C:\\Users\\Vaso\\Desktop\\iamgagme.os");
		srcVal.setFont(font);
		srcVal.setMaximumSize(new Dimension(50, 50));
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		info1.add(srcVal, c);

		dimVal = new JLabel("");
		dimVal.setFont(font);
		c.gridx = 1;
		c.gridy = 1;
		info1.add(dimVal, c);

		JPanel positionPanel = new JPanel();
		positionPanel.setOpaque(false);
		JLabel xlbl = new JLabel("x: ");
		xlbl.setFont(font);

		JLabel ylbl = new JLabel("y: ");
		ylbl.setFont(font);

		xCoord = new JTextField();
		xCoord.setPreferredSize(new Dimension(50, 30));
		xCoord.setMaximumSize(new Dimension(50, 30));
		xCoord.setBackground(Color.DARK_GRAY);
		xCoord.setForeground(Color.white);
		xCoord.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		xCoord.setHorizontalAlignment(SwingConstants.RIGHT);
		xCoord.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		xCoord.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		xCoord.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent event) {
				char c = event.getKeyChar();
				if (!Character.isDigit(c) && c != '\n' || xCoord.getText().length() == 4) {
					event.consume();
				}
			}
		});
		/* TODO NOT TESTED  */
		xCoord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				curLayer.setX(Integer.parseInt(xCoord.getText()));
				LayerInfoPanel.this.parent.getMainPanel().getImagePanel().repaint();
			}
		});

		yCoord = new JTextField();
		yCoord.setPreferredSize(new Dimension(50, 30));
		yCoord.setMaximumSize(new Dimension(50, 30));
		yCoord.setBackground(Color.DARK_GRAY);
		yCoord.setForeground(Color.white);
		yCoord.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		yCoord.setHorizontalAlignment(SwingConstants.RIGHT);
		yCoord.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		yCoord.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		yCoord.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent event) {
				char c = event.getKeyChar();
				if (!Character.isDigit(c) && c != '\n' || yCoord.getText().length() >= 4) {
					event.consume();
				}
			}
		});

		yCoord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				curLayer.setY(Integer.parseInt(yCoord.getText()));
				LayerInfoPanel.this.parent.getMainPanel().getImagePanel().repaint();
			}
		});
		positionPanel.add(xlbl);
		positionPanel.add(xCoord);
		positionPanel.add(ylbl);
		positionPanel.add(yCoord);

		c.gridx = 1;
		c.gridy = 2;
		info1.add(positionPanel, c);
		//-----

		JPanel info2 = new JPanel(new GridBagLayout());
		info2.setOpaque(false);
		final JLabel opcLbl = new JLabel("Opacity: ");
		opcLbl.setFont(font);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		info2.add(opcLbl, c);

		final JLabel visLbl = new JLabel("Visible: ");
		visLbl.setFont(font);
		c.gridx = 0;
		c.gridy = 1;
		info2.add(visLbl, c);

		final JLabel actLbl = new JLabel("Active: ");
		actLbl.setFont(font);
		c.gridx = 0;
		c.gridy = 2;
		info2.add(actLbl, c);

		opacityTextField = new JTextField();
		opacityTextField.setPreferredSize(new Dimension(50, 30));
		opacityTextField.setMaximumSize(new Dimension(50, 30));
		opacityTextField.setBackground(Color.DARK_GRAY);
		opacityTextField.setForeground(Color.white);
		opacityTextField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		opacityTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		opacityTextField.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		opacityTextField.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
		opacityTextField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent event) {
				char c = event.getKeyChar();
				if (!Character.isDigit(c) && c != '\n' || opacityTextField.getText().length() >= 3) {
					event.consume();
				}
			}
		});
		opacityTextField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				LayerInfoPanel.this.curLayer.setOpacity(Integer.parseInt(opacityTextField.getText()));
				LayerInfoPanel.this.parent.getMainPanel().getImagePanel().repaint();
			}
		});
		c.anchor = GridBagConstraints.CENTER;
		opacityTextField.setText("999");

		c.gridx = 1;
		c.gridy = 0;
		info2.add(opacityTextField, c);

		visCheck = new JCheckBox();
		visCheck.setOpaque(false);
		visCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LayerInfoPanel.this.curLayer.setVisible(visCheck.isSelected());
				LayerInfoPanel.this.parent.getMainPanel().getImagePanel().repaint();

			}
		});
		//visCheck.setBackground(Color.DARK_GRAY);

		c.gridx = 1;
		c.gridy = 1;
		info2.add(visCheck, c);

		actCheck = new JCheckBox();
		actCheck.setOpaque(false);
		actCheck.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				LayerInfoPanel.this.curLayer.setActive((actCheck.isSelected()));
			}
		});

		//actCheck.setBackground(Color.DARK_GRAY);

		c.gridx = 1;
		c.gridy = 2;
		info2.add(actCheck, c);

		//		c.gridx = 0;
		//		c.gridx = 0;
		//		c.weightx = 0;
		//		c.anchor = GridBagConstraints.WEST;

		this.add(info1, BorderLayout.WEST);
		//		c.gridx = 0;
		//		c.gridx = 1;
		//		c.weightx = 0;
		this.add(info2, BorderLayout.CENTER);

		JButton removeLayerBtn = new JButton("Remove");
		removeLayerBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		removeLayerBtn.setFocusable(false);

		removeLayerBtn.setPreferredSize(new Dimension(120, 60));
		removeLayerBtn.setMaximumSize(new Dimension(120, 60));
		removeLayerBtn.setMinimumSize(new Dimension(120, 60));
		removeLayerBtn.setBackground(Color.DARK_GRAY);
		removeLayerBtn.setForeground(Color.WHITE);
		removeLayerBtn.setAlignmentY(CENTER_ALIGNMENT);
		removeLayerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image.getImage().removeLayer(curLayer.getDepth());

				//LayerInfoPanel.this.parent.getMainPanel().removeLayer(curLayer);
				LayerInfoPanel.this.parent.getStatusInfoPanel().update("Removed layer " + curLayer.getDepth());

				((CardLayout) LayerInfoPanel.this.parent.getLayout()).show(LayerInfoPanel.this.parent, SouthPanel.STATUS);

			}
		});

		Box eastPart = new Box(BoxLayout.Y_AXIS);
		eastPart.add(Box.createVerticalGlue());
		eastPart.add(removeLayerBtn);
		eastPart.add(Box.createVerticalGlue());

		this.add(eastPart, BorderLayout.EAST);

	}

	public void update(Layer layer) {
		this.curLayer = layer;
		this.titledBorder.setTitle("Layer " + layer.getDepth());
		this.srcVal.setText(layer.getSourceFile());
		this.dimVal.setText(layer.getWidth() + " x " + layer.getHeight());
		this.xCoord.setText(String.valueOf(layer.getCoordinates().x));
		this.yCoord.setText(String.valueOf(layer.getCoordinates().y));
		this.opacityTextField.setText(String.valueOf(layer.getOpacity()));
		this.visCheck.setSelected(layer.getVisibleStatus());
		this.actCheck.setSelected(layer.getActiveStatus());
		this.repaint();

	}
}
@SuppressWarnings("serial")
public class SouthPanel extends JPanel {
	public static final String STATUS = "Status";
	public static final String LAYER = "Layer";
	public static final String SELECTION = "Selection";

	private StatusInfoPanel statusInfoPanel;
	private LayerInfoPanel layerInfoPanel;
	private SelectionInfoPanel selectionInfoPanel;
	private MainCard mainPanel;

	public SouthPanel(MainCard mainPanel) {
		super(new CardLayout());
		this.mainPanel = mainPanel;
		this.setPreferredSize(new Dimension(800, 137));
		this.setBackground(Color.GRAY);
		//this.setBorder(BorderFactory.createLineBorder(new Color(0x4169e1), 1));

		this.add(this.statusInfoPanel = new StatusInfoPanel(), STATUS);
		this.add(this.selectionInfoPanel = new SelectionInfoPanel(this), SELECTION);
		this.add(this.layerInfoPanel = new LayerInfoPanel(this), LAYER);

	}

	public StatusInfoPanel getStatusInfoPanel() {
		return statusInfoPanel;
	}

	public SelectionInfoPanel getSelectionInfoPanel() {
		return selectionInfoPanel;
	}



	public LayerInfoPanel getLayerInfoPanel() {
		return layerInfoPanel;
	}

	public MainCard getMainPanel() {
		return mainPanel;
	}

}
