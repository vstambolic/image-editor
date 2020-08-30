package user_interface;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

@SuppressWarnings("serial")
public class ScrollPane extends JScrollPane {
	public ScrollPane(JComponent component) {
		super(component);

		ScrollBarUI scrollBarUI = new BasicScrollBarUI() {
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

		};
		ScrollBarUI scrollBarUI2 = new BasicScrollBarUI() {
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

		};
		this.getHorizontalScrollBar().setUI(scrollBarUI);
		this.getVerticalScrollBar().setUI(scrollBarUI2);

		//this.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(4.0f), Color.black));
	}


}
