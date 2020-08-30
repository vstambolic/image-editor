package image;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coordinates {
	public int x = 0, y = 0;

	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coordinates() {

	}
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Coordinates))
			return false;
		Coordinates other = (Coordinates) object;
		return this.x == other.x && this.y == other.y;
	}

	public static Coordinates parse(String str) {
		Pattern p = Pattern.compile("(.*) (.*)");
		Matcher m = p.matcher(str);
		if (m.matches())
			return new Coordinates(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
		return new Coordinates();
	}
}
