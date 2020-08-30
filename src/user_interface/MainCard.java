package user_interface;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import formatters.Formatter.FormatNotSupported;
import image.Image;
import image.Layer;
import image.Selection;

@SuppressWarnings("serial")
public class MainCard extends JPanel {

	// Fields ----------------------------------------------

	private UserInterface ui;
	// East
	private JButton newLayerBtn;
	private JPanel layerListPanel;
	private JButton newSelectionBtn;
	private JPanel selectionListPanel;
	private ArrayList<JButton> layerButtons = new ArrayList<JButton>();
	private ArrayList<JButton> selectionButtons = new ArrayList<JButton>();

	// Center
	private SouthPanel southPanel;
	private JPanel eastPanel;
	private ImagePanel imagePanel;
	private ScrollPane imageScrollPane;

	// Constructor -----------------------------------------
	public MainCard(UserInterface userInterface) {
		super(new BorderLayout());
		this.ui = userInterface;

		this.initEast();
		this.initWest();
		this.initCenter();

		Image.getImage().setMainCard(this);
	}

	// West -------------------------------------------------
	private void initWest() {

		WestPanel westPanel = new WestPanel(this.ui);
		this.add(westPanel, BorderLayout.WEST);
	}

	// Center --------------------------------------------------------------
	JPanel centerPanel;

	private void initCenter() {
		centerPanel = new JPanel(new BorderLayout());

		imageScrollPane = new ScrollPane(this.imagePanel = new ImagePanel());
		imageScrollPane.setOpaque(false);
		centerPanel.add(imageScrollPane, BorderLayout.CENTER);
		centerPanel.add(this.southPanel = new SouthPanel(this), BorderLayout.SOUTH);

		this.add(centerPanel, BorderLayout.CENTER);

	}

	// East --------------------------------------------------------------

	private void initEast() {
		eastPanel = new JPanel(new GridLayout(2, 1));
		eastPanel.setPreferredSize(new Dimension(300, 0));

		eastPanel.add(this.createLayerPanel());
		eastPanel.add(this.createSelectionPanel());

		this.add(eastPanel, BorderLayout.EAST);
	}
	public JButton createLayerButton(Layer layer) {
		JButton btn = new JButton("Layer " + layer.getDepth());
		btn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		btn.setBackground(new Color(0x222222));
		btn.setForeground(Color.white);
		btn.setFocusable(false);
		ImageIcon icon;
		if (Pattern.matches(".*\\.bmp$", layer.getSourceFile())) {
		try {
				icon = new ImageIcon(ImageIO.read(new File(layer.getSourceFile())));

			java.awt.Image img = icon.getImage();

			java.awt.Image newimg;

			double k = (double) icon.getIconWidth() / icon.getIconHeight();
			if (k > 1) {
				newimg = img.getScaledInstance(225, (int) (225 / k), java.awt.Image.SCALE_SMOOTH);
			}
			else
				newimg = img.getScaledInstance((int) (150 * k), 150, java.awt.Image.SCALE_SMOOTH);

			icon = new ImageIcon(newimg);

			btn.setIcon(icon);

		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}

			catch (IOException e) {
			e.printStackTrace();
		}
		}

		btn.setHorizontalTextPosition(AbstractButton.CENTER);
		btn.setVerticalTextPosition(AbstractButton.BOTTOM);

		btn.setMaximumSize(new Dimension(300, 200));
		btn.setPreferredSize(new Dimension(300, 200));

		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainCard.this.southPanel.getLayerInfoPanel().update(Image.getImage().getLayers().get(layer.getDepth()));
				((CardLayout) MainCard.this.southPanel.getLayout()).show(MainCard.this.southPanel, SouthPanel.LAYER);
			}
		});

		layerButtons.add(btn);
		layerListPanel.add(btn);
		layerListPanel.revalidate();
		imagePanel.repaint();

		MainCard.this.southPanel.getStatusInfoPanel().update("Added layer " + layer.getDepth());
		((CardLayout) MainCard.this.southPanel.getLayout()).show(MainCard.this.southPanel, SouthPanel.STATUS);
		//btn.paintComponents(getGraphics());

		return btn;
	}

	public void removeLayer(Layer layer) {

		this.layerListPanel.remove(this.layerButtons.get(layer.getDepth()));
		this.layerListPanel.revalidate();
		for (int i = layer.getDepth() + 1; i < this.layerButtons.size(); i++)
			this.layerButtons.get(i).setText("Layer " + (i - 1));
		this.layerButtons.remove(layer.getDepth());

		imagePanel.repaint();


	}

	private JPanel createLayerPanel() {
		JPanel layerPanel = new JPanel(new GridBagLayout());
		layerPanel.setPreferredSize(new Dimension(300, 0));
		GridBagConstraints c = new GridBagConstraints();

		// Label ------------------------------------------------
		JLabel layersLabel = new JLabel("Layers");
		layersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		layersLabel.setFont(UserInterface.DEFAULT_FONT);
		layersLabel.setBackground(new Color(0x22222));
		layersLabel.setForeground(new Color(0xFFFFFF));

		layersLabel.setOpaque(true);
		c.gridx = c.gridy = 0;
		c.ipadx = 180;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		layerPanel.add(layersLabel, c);

		// Button ------------------------------------------------
		newLayerBtn = new JButton("+");
		newLayerBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		newLayerBtn.setBackground(new Color(0x222222));
		newLayerBtn.setForeground(Color.white);
		newLayerBtn.setFocusable(false);

		// ScrollPane ------------------------------------------------
		ScrollPane scroll;
		layerListPanel = new JPanel();
		layerListPanel.setLayout(new BoxLayout(layerListPanel, BoxLayout.Y_AXIS));
		layerListPanel.setBackground(Color.LIGHT_GRAY);
		scroll = new ScrollPane(layerListPanel);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(300, 0));

		newLayerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser c = new JFileChooser();
				int rVal = c.showOpenDialog(MainCard.this.ui);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					String str = c.getSelectedFile().toString();
					if (str != null && (Pattern.matches(".*\\.bmp", str) || Pattern.matches(".*\\.pam", str))) {
						try {
							Image im = Image.getImage();
							im.load(str);
	
							//						ui.setMaximumSize(ui.getSize());
							//
							//						ui.pack();
							//						ui.setMaximumSize(null);
							ui.setSize(ui.getSize().width, ui.getSize().height + 1);
							//ui.setSize(ui.getSize().width, ui.getSize().height - 1);
	
						}
						catch (IOException | FormatNotSupported e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});

		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 0;
		c.weightx = 0;
		layerPanel.add(newLayerBtn, c);


		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weighty = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		layerPanel.add(scroll, c);

		return layerPanel;
	}

	private JPanel createSelectionPanel() {
		JPanel selectionPanel = new JPanel(new GridBagLayout());
		selectionPanel.setPreferredSize(new Dimension(300, 0));
		GridBagConstraints c = new GridBagConstraints();

		// Label ------------------------------------------------
		JLabel layersLabel = new JLabel("Selections");
		layersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		layersLabel.setFont(UserInterface.DEFAULT_FONT);
		layersLabel.setBackground(new Color(0x22222));
		layersLabel.setForeground(Color.WHITE);

		layersLabel.setOpaque(true);
		c.gridx = c.gridy = 0;
		c.ipadx = 180;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		selectionPanel.add(layersLabel, c);

		// Button ------------------------------------------------
		newSelectionBtn = new JButton("+");
		newSelectionBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		newSelectionBtn.setBackground(new Color(0x222222));
		newSelectionBtn.setForeground(Color.white);
		newSelectionBtn.setFocusable(false);
		newSelectionBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = new JDialog(MainCard.this.ui, "New selection", true);
				dialog.setBounds(ui.getX() + ui.getWidth() / 2 - 175, ui.getY() + ui.getHeight() / 2 - 60, 350, 120);

				Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);

				JPanel tmpPanel = new JPanel();
				tmpPanel.setLayout(new BoxLayout(tmpPanel, BoxLayout.Y_AXIS));
				tmpPanel.setBackground(Color.DARK_GRAY);

				JLabel label = new JLabel("Selection name:");
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
						MainCard.this.createNewSelection(textField.getText());
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
		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 0;
		c.weightx = 0;
		selectionPanel.add(newSelectionBtn, c);

		// ScrollPane ------------------------------------------------

		selectionListPanel = new JPanel();
		selectionListPanel.setLayout(new BoxLayout(selectionListPanel, BoxLayout.Y_AXIS));
		selectionListPanel.setBackground(Color.LIGHT_GRAY);
		ScrollPane scroll = new ScrollPane(selectionListPanel);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(300, 0));

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weighty = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		selectionPanel.add(scroll, c);
		return selectionPanel;

	}

	public void createSelectionButton(String text) {
		JButton btn = new JButton(text);
		btn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		btn.setBackground(new Color(0x222222));
		btn.setForeground(Color.white);
		btn.setFocusable(false);
		btn.setMaximumSize(new Dimension(300, 100));
		btn.setPreferredSize(new Dimension(300, 100));
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Selection selection = Image.getImage().getSelectionByName(text);
				MainCard.this.southPanel.getSelectionInfoPanel().update(selection);
				imagePanel.setSelection(selection);
				imagePanel.repaint();
				((CardLayout) MainCard.this.southPanel.getLayout()).show(MainCard.this.southPanel, SouthPanel.SELECTION);

			}
		});

		selectionListPanel.add(btn);
		selectionListPanel.revalidate();

	}

	public void createNewSelection(String text) {
		//	this.selectionButtons.stream().forEach(button -> button.setEnabled(false));
		this.imagePanel.repaint();


		JButton doneButton = new JButton("Done");
		doneButton.setMaximumSize(new Dimension(100, 100));
		doneButton.setBackground(Color.DARK_GRAY);
		doneButton.setForeground(Color.WHITE);
		doneButton.setFont(UserInterface.DEFAULT_FONT);
		doneButton.setFocusable(false);
		doneButton.setMinimumSize(new Dimension(120, 60));
		doneButton.setMaximumSize(new Dimension(120, 60));
		doneButton.setPreferredSize(new Dimension(120, 60));
		this.southPanel.getStatusInfoPanel().update("");
		this.southPanel.getStatusInfoPanel().update("Press button when done:    ", doneButton);
		doneButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainCard.this.imagePanel.deactivateListener();
				MainCard.this.southPanel.getStatusInfoPanel().update("Successfully created selection.");
				((CardLayout) MainCard.this.southPanel.getLayout()).show(MainCard.this.southPanel, SouthPanel.STATUS);
			}
		});
		((CardLayout) this.southPanel.getLayout()).show(this.southPanel, SouthPanel.STATUS);

		this.imagePanel.activateListener(text);


		
	}

	public ImagePanel getImagePanel() {
		return this.imagePanel;
	}

	public void clear() {
		this.layerButtons.clear();
		this.selectionButtons.clear();
		this.layerListPanel.removeAll();
		this.layerListPanel.revalidate();
		this.selectionListPanel.removeAll();
		this.selectionListPanel.revalidate();
		this.imagePanel.repaint();
	}

}
