package image;

import java.awt.Color;


public class Pixel implements Cloneable {

	// Fields ---------------------------------------------------------------
	public byte red = 0, green = 0, blue = 0, alpha = 0;
	private static int CHAR_BIT = 8;
	public static byte MAX_BYTE = (byte) 0xFF;

	public Pixel(byte red, byte green, byte blue, byte alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}


	// Conversion to Dword --------------------------------------------------
	public Pixel(int dword) {
		red = (byte) ((dword >> 24) & 0xFF);
		green = (byte) ((dword >> 16) & 0xFF);
		blue = (byte) ((dword >> 8) & 0xFF);
		alpha = (byte) (dword & 0xFF);
	}

	public Pixel() {
	}

	public int toDword() {
		int tmp = 0;
		tmp |= (int) red & 0xFF;
		tmp <<= CHAR_BIT;
		tmp |= (int) green & 0xFF;
		tmp <<= CHAR_BIT;
		tmp |= (int) blue & 0xFF;
		tmp <<= CHAR_BIT;
		tmp |= (int) alpha & 0xFF;
		return tmp;
	}

	public Pixel clone() {
		return new Pixel(this.red, this.green, this.blue, this.alpha);
	}
	public Color toColor() {
		return new Color(Byte.toUnsignedInt(this.red), Byte.toUnsignedInt(this.green), Byte.toUnsignedInt(this.blue), Byte.toUnsignedInt(this.alpha));
	}
	public double scaleAlpha() {
		return Byte.toUnsignedInt(alpha) / 255.;
	}


}
