package user_interface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import image.Image;
import image.Selection;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

	Image image = Image.getImage();
	private Selection curSelection = null;

	public ImagePanel() {
		this.setBackground(Color.DARK_GRAY);
		this.setOpaque(true);
    }


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		image.create();
		this.setMinimumSize(new Dimension((int) image.getWidth(), (int) image.getHeight()));
		this.setMaximumSize(new Dimension((int) image.getWidth(), (int) image.getHeight()));
		this.setPreferredSize(new Dimension((int) image.getWidth(), (int) image.getHeight()));

		for (int i = 0; i < image.getHeight(); i++)
			for (int j = 0; j < image.getWidth(); j++) {
				g.setColor(image.getProject().getMatrix()[i][j].toColor());
				g.drawLine(j, i, j, i);
			}
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(dashed);
		g2.setPaint(Color.RED);
		drawRect(g2, x, y, x2, y2);
		if (curSelection != null)
			curSelection.getRectangles().stream().forEach(rect -> g2.draw(rect));
	}

	private int x = 0, y = 0, x2 = 0, y2 = 0;
	final static BasicStroke dashed = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f }, 0.0f);

	public void setStartPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setEndPoint(int x, int y) {
		x2 = (x);
		y2 = (y);
	}

	public void drawRect(Graphics2D g, int x, int y, int x2, int y2) {
		g.draw(new Rectangle2D.Double(Math.min(x, x2), Math.min(y, y2), Math.abs(x - x2), Math.abs(y - y2)));
	}

	private MyMouseListener listener = new MyMouseListener();

	public void activateListener(String text) {
		curSelection = new Selection(text);

		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
	}

	public void deactivateListener() {
		this.image.addSelectionActive(curSelection);

		curSelection = null;
		this.removeMouseListener(listener);
		this.removeMouseMotionListener(listener);
		x = y = x2 = y2 = 0;
		this.repaint();
	}

	class MyMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			setStartPoint(e.getX(), e.getY());
		}

		public void mouseDragged(MouseEvent e) {
			setEndPoint(e.getX(), e.getY());
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			setEndPoint(e.getX(), e.getY());
			curSelection.addRectangle(new Rectangle2D.Double(Math.min(x, x2), Math.min(y, y2), Math.abs(x - x2), Math.abs(y - y2)));
			repaint();
		}
	}

	public void setSelection(Selection selection) {
		this.curSelection = selection;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}