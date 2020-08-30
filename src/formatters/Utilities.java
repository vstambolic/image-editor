package formatters;

public class Utilities {
	public static int CHAR_BIT = 8;

	public static int ror(int x, int moves) {
		return (x >>> moves) | (x << (4 * CHAR_BIT - moves));
	}

	public static int rol(int x, int moves) {
		return (x << moves) | (x >>> (4 * CHAR_BIT - moves));
	}

}
