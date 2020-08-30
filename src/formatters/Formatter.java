package formatters;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

import image.Image;
import image.Pixel;
abstract public class Formatter {

	@SuppressWarnings("serial")
	public static class FormatNotSupported extends Exception {
		protected String message = "Format not supported.";

		@Override
		public String getMessage() {
			return this.message;
		}
	}

	protected String filePath;

	protected abstract Pixel getPixel(RandomAccessFile reader) throws IOException;

	protected abstract void putPixel(RandomAccessFile writer, final Pixel pixel) throws IOException;

	public Formatter(String filePath) {
		this.filePath = filePath;
	}

	public abstract void read(Image image) throws IOException, FormatNotSupported;

	public abstract void write(final Image image) throws IOException;

	public static Formatter matchFormatter(String filePath) throws FormatNotSupported {
		if (Pattern.matches(".*\\.bmp$", filePath))
			return new BMP_Formatter(filePath);
		if (Pattern.matches(".*\\.pam$", filePath))
			return new PAM_Formatter(filePath);

		if (Pattern.matches(".*\\.xml$", filePath))
			return new XML_Formatter(filePath);

		throw new FormatNotSupported();

	}
	 

}