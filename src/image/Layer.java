package image;

import java.util.stream.Stream;

public class Layer {

	public static int MAX_OPACITY = 100;
	public static Coordinates DEFAULT_COORDINTES = new Coordinates();

	private String sourceFile;
	private long width;
	private long height;
	Pixel[][] pixelMatrix;
	boolean active;
	boolean visible;
	int opacity;
	int depth;
	Coordinates coordinates;
	Image image;

	// Constructor ------------------------------------------------------
	public Layer(Image image, final String filePath, long height, long width, boolean active, boolean visible, int opacity, final Coordinates coordinates, int depth) {
		this.sourceFile = filePath;
		this.height = height;
		this.width = width;
		this.pixelMatrix = new Pixel[(int) height][(int) width];
		this.active = active;
		this.visible = visible;
		this.opacity = opacity;
		this.coordinates = coordinates;
		this.depth = depth;
		this.image = image;
	}

	// Getters -----------------------------------------------------------
	public String getSourceFile() {
		return sourceFile;
	}

	public long getWidth() {
		return width;
	}

	public long getHeight() {
		return height;
	}

	public int getOpacity() {
		return opacity;
	}

	public boolean getVisibleStatus() {
		return visible;
	}

	public boolean getActiveStatus() {
		return active;
	}

	public long dataSize() {
		return width * height * 32;
	}

	public Pixel[][] getMatrix() {
		return this.pixelMatrix;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	// Setters -----------------------------------------------------------
	public void setOpacity(int opacity) {

		if (opacity < 0)
			this.opacity = 0;
		else
			this.opacity = opacity;

		Stream.of(this.pixelMatrix).forEach(row -> Stream.of(row).forEach(pixel -> {
			pixel.alpha = (byte) Math.min(255., ((double) opacity) / MAX_OPACITY * Byte.toUnsignedInt(pixel.alpha));

		}));
		this.image.setStatus(Image.Status.MODIFIED);
	}

	public int getDepth() {
		return this.depth;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}


	public void setVisible(boolean newStatus) {
		this.visible = newStatus;
		this.image.setStatus(Image.Status.MODIFIED);

	}

	public void setActive(boolean newStatus) {
		this.active = newStatus;
	}

	public void setPosition(Coordinates coordinates) {
		this.coordinates = coordinates;
	}


	public void setDepth(int i) {
		this.depth = i;
	}

	public void setX(int x) {
		this.coordinates.x = x;
		this.image.setStatus(Image.Status.MODIFIED);

	}

	public void setY(int y) {
		this.coordinates.y = y;
		this.image.setStatus(Image.Status.MODIFIED);

	}
	// Operations -----------------------------------------------------------

	public void sharpen(int sharpIndex) {
		for (int i = 0; i < this.height; i += sharpIndex)
			for (int j = 0; j < this.width; j += sharpIndex) {
				final int m = Math.min((int) this.height, i + sharpIndex);
				final int n = Math.min((int) this.width, j + sharpIndex);
				for (int a = i; a < m; a++)
					for (int b = j; b < n; b++)
						this.pixelMatrix[a][b] = this.pixelMatrix[i][j];
			}
	}

	public Layer clone() {
		Layer newLayer = new Layer(this.image, this.sourceFile, this.height, this.width, this.active, this.visible, this.opacity, this.coordinates, this.depth);
		for (int i = 0; i < this.height; i++)
			for (int j = 0; j < this.width; j++)
				newLayer.pixelMatrix[i][j] = this.pixelMatrix[i][j].clone();
		return newLayer;
	}

}
