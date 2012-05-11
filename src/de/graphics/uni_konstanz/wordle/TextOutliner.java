package de.graphics.uni_konstanz.wordle;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;

public class TextOutliner {

	public static Shape getOutline(final Font font, final String string) {
		final BufferedImage img = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2d = img.createGraphics();
		final FontRenderContext frc = g2d.getFontRenderContext();
		final GlyphVector gv = font.createGlyphVector(frc, string);
		return gv.getOutline();
	}

}
