package user_interface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import formatters.Formatter.FormatNotSupported;
import image.Image;

@SuppressWarnings("serial")
public class SaveProjectDialog extends javax.swing.JDialog {

	@SuppressWarnings("unused")
	private UserInterface ui;

	private JButton createButton() {
		JButton btn = new JButton();
		btn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		btn.setFocusable(false);
		btn.setPreferredSize(new Dimension(120, 60));
		btn.setMaximumSize(new Dimension(120, 60));
		btn.setBackground(Color.DARK_GRAY);
		btn.setForeground(Color.WHITE);
		btn.setBorder(BorderFactory.createLineBorder(Color.black));
		btn.setAlignmentX(CENTER_ALIGNMENT);
		return btn;
	}
	public SaveProjectDialog(UserInterface ui) {
		super(ui, "Save Project", true);
		this.ui = ui;
		this.setBounds(ui.getX() + ui.getWidth() / 2 - 250, ui.getY() + ui.getHeight() / 2 - 150, 500, 250);
		Box box = new Box(BoxLayout.Y_AXIS);
		box.setBackground(Color.GRAY);
		box.setOpaque(true);
		JLabel lbl = new JLabel("Save changes in project?");
		lbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		lbl.setAlignmentX(CENTER_ALIGNMENT);
		//lbl.setForeground(Color.DARK_GRAY);
		box.add(Box.createRigidArea(new Dimension(0, 40)));
		box.add(lbl);
		box.add(Box.createRigidArea(new Dimension(0, 30)));

		Box buttonBox = new Box(BoxLayout.X_AXIS);

		JButton saveBtn = this.createButton();
		saveBtn.setText("Save");
		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				JFileChooser c = new JFileChooser();
				int rVal = c.showSaveDialog(ui);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					try {
						Image.getImage().save(c.getSelectedFile().toString());
					}
					catch (FormatNotSupported | IOException e1) {
						e1.printStackTrace();
					}
				}
				ui.dispose();
			}
		});
		JButton dontSave = this.createButton();
		dontSave.setText("Don't Save");
		dontSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ui.dispose();
			}
		});
		JButton cancel = this.createButton();
		cancel.setText("Cancel");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();

			}
		});

		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(saveBtn);

		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(dontSave);

		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(cancel);

		buttonBox.add(Box.createHorizontalGlue());

		box.add(buttonBox);
		box.add(Box.createVerticalGlue());
		
		
		
		this.add(box);
		this.setVisible(true);
	}
}
