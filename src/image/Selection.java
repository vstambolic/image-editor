package image;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class Selection {
	
	private LinkedList<Rectangle2D> rectangles = new LinkedList<Rectangle2D>();
	private String name;

	public Selection(String text) {
		this.name = text;
	}

	public void addRectangle(final Rectangle2D newRectangle) {
		if (rectangles.stream().anyMatch(rect -> {
			return rect.contains(newRectangle);
		}))
			return;

		//		rectangles.stream().forEach(rect -> {
		//			if (rect.contains(newRectangle)) {
		//				return;
		//			}
		//		});

		rectangles.removeIf(rect -> {
			return newRectangle.contains(rect);
		});
		this.rectangles.add(newRectangle);
	}

	public LinkedList<Rectangle2D> getRectangles() {
		return this.rectangles;
	}

	public String getName() {
		return this.name;
	}

}
