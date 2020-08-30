package formatters;

import java.io.IOException;
import java.io.RandomAccessFile;

import image.Image;
import image.Layer;
import image.Pixel;

class BMP_Header {
	private static final short id = 0x424D;
	private long fileSize = 0x36;
	private static final short vacant1 = 0x0000;
	private static final short vacant2 = 0x0000;
	private long bitmapDataOffset = 0x36;

	public long getBitmapDataOffset() {
		return this.bitmapDataOffset;
	}

	public long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize += fileSize;
	}

	public RandomAccessFile scan(RandomAccessFile file) throws IOException {
		byte[] buffer = new byte[4];

		file.seek(0x2);
		file.read(buffer);
		this.fileSize = (buffer[0] & 0xFF) | (buffer[1] & 0xFF) << 8 | (buffer[2] & 0xFF) << 16 | (buffer[3] & 0xFF) << 24;

		file.seek(0xA);
		file.read(buffer);
		this.bitmapDataOffset = (buffer[0] & 0xFF) | (buffer[1] & 0xFF) << 8 | (buffer[2] & 0xFF) << 16 | (buffer[3] & 0xFF) << 24;

		return file;
	}

	public RandomAccessFile print(RandomAccessFile file) throws IOException {
		byte[] buffer = new byte[4];
		file.writeShort(BMP_Header.id);

		buffer[0] = (byte) (this.fileSize & 0xFF);
		buffer[1] = (byte) ((this.fileSize >> 8) & 0xFF);
		buffer[2] = (byte) ((this.fileSize >> 16) & 0xFF);
		buffer[3] = (byte) ((this.fileSize >> 24) & 0xFF);

		file.write(buffer);
		file.writeShort(BMP_Header.vacant1);
		file.writeShort(BMP_Header.vacant2);

		buffer[0] = (byte) (this.bitmapDataOffset & 0xFF);
		buffer[1] = (byte) ((this.bitmapDataOffset >> 8) & 0xFF);
		buffer[2] = (byte) ((this.bitmapDataOffset >> 16) & 0xFF);
		buffer[3] = (byte) ((this.bitmapDataOffset >> 24) & 0xFF);

		file.write(buffer);

		return file;
	}
}

//DIB Header -----------------------------------------------------------------------

class DIB_Header {

	public static final int _24_BIT = 24;
	public static final int _32_BIT = 32;

	private static final int dibHeaderSize = 0x28_00_00_00;
	private long width;
	private long height;
	private static final short colorPlanes = 0x01_00;
	private int bitsPerPixel = _32_BIT;
	private static final int bi_rgb = 0x00_00_00_00;
	private long bitmapSize;
	private static final int printResolution1 = 0x13_0B_00_00;
	private static final int printResolution2 = 0x13_0B_00_00;
	private static final int numberOfColors = 0x00_00_00_00;
	private static final int importantColors = 0x00_00_00_00;

	// Input / Output -----------------------------------

	public RandomAccessFile scan(RandomAccessFile file) throws IOException {
		byte[] buffer = new byte[4];

		file.seek(0x12);

		file.read(buffer);
		this.width = (buffer[0] & 0xFF) | (buffer[1] & 0xFF) << 8 | (buffer[2] & 0xFF) << 16 | (buffer[3] & 0xFF) << 24;

		file.read(buffer);
		this.height = (buffer[0] & 0xFF) | (buffer[1] & 0xFF) << 8 | (buffer[2] & 0xFF) << 16 | (buffer[3] & 0xFF) << 24;

		file.seek(0x1C);
		file.read(buffer, 0, 2);
		this.bitsPerPixel = (buffer[0] & 0xFF) | (buffer[1] & 0xFF) << 8;

		file.seek(0x22);
		file.read(buffer);
		this.bitmapSize = (buffer[0] & 0xFF) | (buffer[1] & 0xFF) << 8 | (buffer[2] & 0xFF) << 16 | (buffer[3] & 0xFF) << 24;

		return file;
	}

	public RandomAccessFile print(RandomAccessFile file) throws IOException {
		byte[] buffer = new byte[4];

		file.writeInt(DIB_Header.dibHeaderSize);

		buffer[0] = (byte) (this.width & 0xFF);
		buffer[1] = (byte) ((this.width >> 8) & 0xFF);
		buffer[2] = (byte) ((this.width >> 16) & 0xFF);
		buffer[3] = (byte) ((this.width >> 24) & 0xFF);
		file.write(buffer);

		buffer[0] = (byte) (this.height & 0xFF);
		buffer[1] = (byte) ((this.height >> 8) & 0xFF);
		buffer[2] = (byte) ((this.height >> 16) & 0xFF);
		buffer[3] = (byte) ((this.height >> 24) & 0xFF);
		file.write(buffer);

		file.writeShort(DIB_Header.colorPlanes);

		buffer[0] = (byte) (this.bitsPerPixel & 0xFF);
		buffer[1] = (byte) ((this.bitsPerPixel >> 8) & 0xFF);
		file.write(buffer, 0, 2);

		file.writeInt(DIB_Header.bi_rgb);

		buffer[0] = (byte) (this.bitmapSize & 0xFF);
		buffer[1] = (byte) ((this.bitmapSize >> 8) & 0xFF);
		buffer[2] = (byte) ((this.bitmapSize >> 16) & 0xFF);
		buffer[3] = (byte) ((this.bitmapSize >> 24) & 0xFF);
		file.write(buffer);

		file.writeInt(DIB_Header.printResolution1);
		file.writeInt(DIB_Header.printResolution2);
		file.writeInt(DIB_Header.numberOfColors);
		file.writeInt(DIB_Header.importantColors);
		return file;
	}

	// Getters / Setters --------------------------------
	long getWidth() {
		return width;
	}

	long getHeight() {
		return height;
	}

	int getBitsPerPixel() {
		return this.bitsPerPixel;
	}

	void setWidth(long _width) {
		this.width = _width;
	}

	void setHeight(long _height) {
		this.height = _height;
	}

	void setBitmapSize(long size) {
		this.bitmapSize = size;
	}
}

public class BMP_Formatter extends Formatter {

	public BMP_Formatter(String filePath) {
		super(filePath);
	}

	private BMP_Header bmp_header = new BMP_Header();
	private DIB_Header dib_header = new DIB_Header();

	@SuppressWarnings("serial")
	public static class BMPFormatNotSupported extends Formatter.FormatNotSupported {
		public BMPFormatNotSupported(int bits_per_pixel) {
			this.message = "BMP file with bits_per_pixel parameter " + bits_per_pixel + " not supported.";
		}

		@Override
		public String getMessage() {
			return this.message;
		}

	}

	@Override
	public Pixel getPixel(RandomAccessFile file) throws IOException {
		byte[] buffer = new byte[4];
		byte tmp;
		if (this.dib_header.getBitsPerPixel() == DIB_Header._32_BIT) {
			file.read(buffer); //BGRA
			tmp = buffer[0];
			buffer[0] = buffer[2];
			buffer[2] = tmp; //RGBA	
		}
		else {
			file.read(buffer, 0, 3); // BGR
			tmp = buffer[0];
			buffer[0] = buffer[2];
			buffer[2] = tmp; // RGB
			buffer[3] = (byte) 0xFF; //RGBA
		}

		return new Pixel(buffer[0], buffer[1], buffer[2], buffer[3]);
	}

	@Override
	protected void putPixel(RandomAccessFile file, Pixel pixel) throws IOException {
		byte[] buffer = new byte[4];
		buffer[0] = pixel.blue;
		buffer[1] = pixel.green;
		buffer[2] = pixel.red;
		buffer[3] = pixel.alpha;
		file.write(buffer);

	}

	@Override
	public void read(Image image) throws IOException, BMPFormatNotSupported {
		RandomAccessFile file = new RandomAccessFile(this.filePath, "r");
		this.bmp_header.scan(file);
		this.dib_header.scan(file);

		int bpp = this.dib_header.getBitsPerPixel();
		if (bpp != DIB_Header._24_BIT && bpp != DIB_Header._32_BIT)
			throw new BMPFormatNotSupported(bpp);

		final long m = this.dib_header.getHeight();
		final long n = this.dib_header.getWidth();
		Layer layer = new Layer(image, this.filePath, m, n, true, true, Layer.MAX_OPACITY, Layer.DEFAULT_COORDINTES, image.getLayerNum());

		final long leftover = n * bpp / 8 % 4;
		final long padding = (leftover != 0 ? 4 - leftover : 0);

		file.seek(this.bmp_header.getBitmapDataOffset());

		Pixel[][] matrix = layer.getMatrix();
		for (int i = (int) m - 1; i >= 0; --i) {
			for (int j = 0; j < n; ++j)
				matrix[i][j] = this.getPixel(file);
			file.seek(file.getFilePointer() + padding);

		}
		file.close();

		image.addLayer(layer);
	}

	@Override
	public void write(Image image) throws IOException {
		RandomAccessFile file = new RandomAccessFile(this.filePath, "rw");

		final Layer layer = image.getProject();
		this.bmp_header.setFileSize(layer.dataSize());
		this.dib_header.setWidth(layer.getWidth());
		this.dib_header.setHeight(layer.getHeight());
		this.dib_header.setBitmapSize(layer.dataSize());

		this.bmp_header.print(file);
		this.dib_header.print(file);

		final Pixel[][] matrix = layer.getMatrix();
		for (int i = (int) layer.getHeight() - 1; i >= 0; --i)
			for (int j = 0; j < layer.getWidth(); ++j)
				this.putPixel(file, matrix[i][j]);
		file.close();
	}

}
