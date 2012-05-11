package de.graphics.uni_konstanz.wordle;

import java.awt.Font;

public class FontManager {

	private final int minFS;
	private final int maxFS;
	private final Font font;

	public FontManager(final int _minFS, final int _maxFS, final String fontName) {
		minFS = _minFS;
		maxFS = _maxFS;
		font = Font.decode(fontName);
	}

	public Font get(final double value) {
		final float f = Math.round((1 - value) * minFS + value * maxFS);
		System.out.println("size: "+f + font);
		return font.deriveFont(f);
	}
}
