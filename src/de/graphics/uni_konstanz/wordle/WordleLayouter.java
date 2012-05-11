package de.graphics.uni_konstanz.wordle;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WordleLayouter {

  enum RotationMode {
    NO_ROTATION, ROT_90_DEG_LEFT, ROT_90_DEG_RIGHT, ROT_90_DEG_LEFT_AND_RIGHT,
    ROT_90_DEG_RIGHT_AND_LEFT
  }

  public static List<Shape> generateLayoutLinear(final List<Shape> input,
      final boolean doSort, final RotationMode rotationMode) {
    if(doSort) {
      Collections.sort(input, new Comparator<Shape>() {

        @Override
        public int compare(final Shape o1, final Shape o2) {
          return Double.compare(o1.getBounds2D().getCenterX(), o2
              .getBounds2D().getCenterX());
        }
      });
    }
    final List<Shape> layouted = new ArrayList<Shape>();
    for(final Shape cur : input) {
      double t = 3.0;
      // spiral depending on the size of the object
      final double minSide = Math.min(cur.getBounds2D().getWidth(), cur
          .getBounds2D().getHeight());
      final double spiralFactor = minSide / 17.0;
      final double spiralStep = minSide / 10.0;
      while(true) {
        final double tx = Math.sin(t) * t * spiralFactor;
        final double ty = Math.cos(t) * t * spiralFactor;
        final AffineTransform at = new AffineTransform();
        at.translate(tx, ty);
        // transformed object
        final Area transformedArea = getTransformedArea(cur, at);
        if(!hasOverlap(layouted, transformedArea)) {
          // found placement
          layouted.add(transformedArea);
          break;
        }
        // no result by translation -> try rotation depending on
        // rotation mode

        final Rectangle2D rect = transformedArea.getBounds2D();
        final double centerX = rect.getCenterX();
        final double centerY = rect.getCenterY();

        boolean foundSolution = false;
        switch(rotationMode) {

          case ROT_90_DEG_LEFT:
            final AffineTransform rotLeft = new AffineTransform();
            rotLeft.rotate(-Math.PI / 2.0, centerX, centerY);
            final Area resLeft = getTransformedArea(transformedArea,
                rotLeft);
            if(!hasOverlap(layouted, resLeft)) {
              layouted.add(resLeft);
              foundSolution = true;
            }
            break;

          case ROT_90_DEG_RIGHT:
            final AffineTransform rotRight = new AffineTransform();
            rotRight.rotate(-Math.PI / 2.0, centerX, centerY);
            final Area resRight = getTransformedArea(transformedArea,
                rotRight);
            if(!hasOverlap(layouted, resRight)) {
              layouted.add(resRight);
              foundSolution = true;
            }
            break;

          case ROT_90_DEG_LEFT_AND_RIGHT:
            // left
            AffineTransform rot = new AffineTransform();
            rot.rotate(-Math.PI / 2.0, centerX, centerY);
            final Area rotL = getTransformedArea(transformedArea, rot);
            if(!hasOverlap(layouted, rotL)) {
              layouted.add(rotL);
              foundSolution = true;
            }
            // right
            rot = new AffineTransform();
            rot.rotate(Math.PI / 2.0, centerX, centerY);
            final Area rotR = getTransformedArea(transformedArea, rot);
            if(!hasOverlap(layouted, rotR)) {
              layouted.add(rotR);
              foundSolution = true;
            }
            break;

          case ROT_90_DEG_RIGHT_AND_LEFT:
            // right
            rot = new AffineTransform();
            rot.rotate(Math.PI / 2.0, centerX, centerY);
            final Area rot2 = getTransformedArea(transformedArea, rot);
            if(!hasOverlap(layouted, rot2)) {
              layouted.add(rot2);
              foundSolution = true;
            }
            // left
            rot = new AffineTransform();
            rot.rotate(-Math.PI / 2.0, centerX, centerY);
            final Area rot1 = getTransformedArea(transformedArea, rot);
            if(!hasOverlap(layouted, rot1)) {
              layouted.add(rot1);
              foundSolution = true;
            }
            break;
          case NO_ROTATION:
            break;
        }

        if(foundSolution) {
          break;
        }
        t += spiralStep / t;
      }
    }
    return layouted;
  }

  public static List<Shape> generateLayoutCircular(final List<Shape> input,
      final boolean doSort, final RotationMode rotationMode) {
    // calculate center
    double sumX = 0;
    double sumY = 0;
    int count = 0;
    for(final Shape s : input) {
      final Rectangle2D r = s.getBounds2D();
      sumX += r.getCenterX();
      sumY += r.getCenterY();
      ++count;
    }
    final Point2D center = new Point2D.Double(sumX / count, sumY / count);

    if(doSort) {
      Collections.sort(input, new Comparator<Shape>() {

        @Override
        public int compare(final Shape o1, final Shape o2) {
          final Rectangle2D r1 = o1.getBounds2D();
          final Rectangle2D r2 = o2.getBounds2D();
          final Point2D c1 = new Point2D.Double(r1.getCenterX(), r1
              .getCenterY());
          final Point2D c2 = new Point2D.Double(r2.getCenterX(), r2
              .getCenterY());
          return Double.compare(calcEuclideanDistance(c1, center),
              calcEuclideanDistance(c2, center));
        }
      });
    }

    final List<Shape> layouted = new ArrayList<Shape>();
    for(final Shape cur : input) {
      double t = 3.0;
      // spiral depending on the size of the object
      final double minSide = Math.min(cur.getBounds2D().getWidth(), cur
          .getBounds2D().getHeight());
      final double spiralFactor = minSide / 17.0;
      final double spiralStep = minSide / 10.0;
      while(true) {
        final double tx = Math.sin(t) * t * spiralFactor;
        final double ty = Math.cos(t) * t * spiralFactor;
        final AffineTransform at = new AffineTransform();
        at.translate(tx, ty);
        // transformed object
        final Area transformedArea = getTransformedArea(cur, at);
        if(!hasOverlap(layouted, transformedArea)) {
          // found placement
          layouted.add(transformedArea);
          break;
        }

        // no result by translation -> try rotation depending on
        // rotation mode

        final Rectangle2D rect = transformedArea.getBounds2D();
        final double centerX = rect.getCenterX();
        final double centerY = rect.getCenterY();

        boolean foundSolution = false;
        switch(rotationMode) {

          case ROT_90_DEG_LEFT:
            final AffineTransform rotLeft = new AffineTransform();
            rotLeft.rotate(-Math.PI / 2.0, centerX, centerY);
            final Area resLeft = getTransformedArea(transformedArea,
                rotLeft);
            if(!hasOverlap(layouted, resLeft)) {
              layouted.add(resLeft);
              foundSolution = true;
            }
            break;

          case ROT_90_DEG_RIGHT:
            final AffineTransform rotRight = new AffineTransform();
            rotRight.rotate(-Math.PI / 2.0, centerX, centerY);
            final Area resRight = getTransformedArea(transformedArea,
                rotRight);
            if(!hasOverlap(layouted, resRight)) {
              layouted.add(resRight);
              foundSolution = true;
            }
            break;

          case ROT_90_DEG_LEFT_AND_RIGHT:
            // left
            AffineTransform rot = new AffineTransform();
            rot.rotate(-Math.PI / 2.0, centerX, centerY);
            final Area rotL = getTransformedArea(transformedArea, rot);
            if(!hasOverlap(layouted, rotL)) {
              layouted.add(rotL);
              foundSolution = true;
            }
            // right
            rot = new AffineTransform();
            rot.rotate(Math.PI / 2.0, centerX, centerY);
            final Area rotR = getTransformedArea(transformedArea, rot);
            if(!hasOverlap(layouted, rotR)) {
              layouted.add(rotR);
              foundSolution = true;
            }
            break;

          case ROT_90_DEG_RIGHT_AND_LEFT:
            // right
            rot = new AffineTransform();
            rot.rotate(Math.PI / 2.0, centerX, centerY);
            final Area rot2 = getTransformedArea(transformedArea, rot);
            if(!hasOverlap(layouted, rot2)) {
              layouted.add(rot2);
              foundSolution = true;
            }
            // left
            rot = new AffineTransform();
            rot.rotate(-Math.PI / 2.0, centerX, centerY);
            final Area rot1 = getTransformedArea(transformedArea, rot);
            if(!hasOverlap(layouted, rot1)) {
              layouted.add(rot1);
              foundSolution = true;
            }
            break;
          case NO_ROTATION:
            break;
        }

        if(foundSolution) {
          break;
        }
        t += spiralStep / t;
      }
    }
    return layouted;
  }

  private static Area createBleededArea(final Area area) {
    final Area copy = new Area(area);
    final Stroke stroke = new BasicStroke(1.5f);
    final Shape bleed = stroke.createStrokedShape(area);
    final Area a = new Area(bleed);
    copy.add(a);
    return copy;
  }

  private static Area getTransformedArea(final Shape original,
      final AffineTransform transform) {
    final Area copy = new Area(original);
    copy.createTransformedArea(transform);
    return copy;
  }

  private static boolean hasOverlap(final List<Shape> alreadyLayouted,
      final Shape current) {
    for(final Shape s : alreadyLayouted) {
      if(hasOverlap(s, current)) {
        return true;
      }
    }
    return false;
  }

  private static boolean hasOverlap(final Shape s1, final Shape s2) {
    final Area a1 = new Area(s1);
    final Area a2 = new Area(s2);
    final Area a2bleeded = createBleededArea(a2);
    a1.intersect(a2bleeded);
    return !a1.isEmpty();
  }

  private static double calcEuclideanDistance(final Point2D p1,
      final Point2D p2) {
    return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
        + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
  }
}
