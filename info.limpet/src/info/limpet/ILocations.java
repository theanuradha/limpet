package info.limpet;

import java.awt.geom.Point2D;
import java.util.Iterator;

public interface ILocations
{
  Iterator<Point2D> getLocationIterator();
}