package formatters;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import image.Image;
import image.Layer;
import image.Pixel;

public class PAM_Formatter extends Formatter {

	@SuppressWarnings("serial")
	public static class PAMFormatNotSupported extends Formatter.FormatNotSupported {
		public PAMFormatNotSupported(int depth) {
			message = "PAM file with depth parameter" + depth + " not supported.";
		}

		@Override
		public String getMessage() {
			return this.message;
		}

	}
	private long width;
	private long height;
	private int depth = 4;

	public PAM_Formatter(String filePath) {
		super(filePath);
	}

	@Override
	protected Pixel getPixel(RandomAccessFile reader) throws IOException {
		byte[] buffer = new byte[4];

		if (this.depth == 4) {
			reader.read(buffer);
		}
		else {
			reader.read(buffer, 0, 3);
			buffer[3] = (byte) 0xFF;
		}


		return new Pixel(buffer[0], buffer[1], buffer[2], buffer[3]);
	}

	@Override
	protected void putPixel(RandomAccessFile writer, Pixel pixel) throws IOException {
		byte[] buffer = { pixel.red, pixel.green, pixel.blue, pixel.alpha };
		writer.write(buffer);
	}

	@Override
	public void read(Image image) throws IOException, PAMFormatNotSupported {
		RandomAccessFile reader = new RandomAccessFile(this.filePath, "r");

		String str;
		Pattern pattern = Pattern.compile(".* (.*)");
		Matcher matcher;
		reader.readLine(); // P7\n

		str = reader.readLine(); //WIDTH xxxx\n
		matcher = pattern.matcher(str);
		if (matcher.matches())
			this.width = Long.parseLong(matcher.group(1));

		str = reader.readLine(); //HEIGHT xxxx\n
		matcher = pattern.matcher(str);
		if (matcher.matches())
		this.height = Long.parseLong(matcher.group(1));

		str = reader.readLine(); //DEPTH xxxx\n
		matcher = pattern.matcher(str);
		if (matcher.matches())
			this.depth = Integer.parseInt(matcher.group(1));

		if (depth < 3) {
			reader.close();
			throw new PAMFormatNotSupported(depth);

		}

		reader.readLine();
		reader.readLine();
		reader.readLine();

		Layer layer = new Layer(image, this.filePath, this.height, this.width, true, true, Layer.MAX_OPACITY, Layer.DEFAULT_COORDINTES, image.getLayerNum());
		Pixel[][] matrix = layer.getMatrix();
		for (int i = 0; i < layer.getHeight(); ++i)
			for (int j = 0; j < layer.getWidth(); ++j)
				matrix[i][j] = this.getPixel(reader);
		reader.close();
		image.addLayer(layer);

	}

	@Override
	public void write(Image image) throws IOException {
		final Layer layer = image.getProject();

		OutputStreamWriter headerWriter = new OutputStreamWriter(new FileOutputStream(this.filePath), StandardCharsets.UTF_8);

		StringBuilder str = new StringBuilder("P7\n");
		str.append("WIDTH " + layer.getWidth() + "\n");
		str.append("HEIGHT " + layer.getHeight() + "\n");
		str.append("DEPTH " + this.depth + "\n");
		str.append("MAXVAL 255\n");
		str.append("TUPLTYPE RGB_ALPHA\n");
		str.append("ENDHDR\n");
		headerWriter.write(str.toString());
		headerWriter.close();

		RandomAccessFile writer = new RandomAccessFile(this.filePath, "rw");
		writer.seek(writer.length());

		final Pixel[][] matrix = layer.getMatrix();
		for (int i = 0; i < layer.getHeight(); ++i)
			for (int j = 0; j < layer.getWidth(); ++j)
				this.putPixel(writer, matrix[i][j]);
		writer.close();

	}



}
