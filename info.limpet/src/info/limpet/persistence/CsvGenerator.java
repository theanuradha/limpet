package info.limpet.persistence;

import info.limpet.Document;
import info.limpet.ILocations;
import info.limpet.IStoreItem;
import info.limpet.LocationDocument;
import info.limpet.NumberDocument;

import java.awt.geom.Point2D;
import java.util.Date;
import java.util.Iterator;

public class CsvGenerator
{
  /**
   * prevent accidental instance declaration
   * 
   */
  protected CsvGenerator()
  {

  }

  private static final String LEFT_BRACKET = "[";
  private static final String RIGHT_BRACKET = "]";
  private static final String RIGHT_PARENTHESES = ")";
  private static final String LEFT_PARENTHESES = "(";
  private static final String COMMA_SEPARATOR = ",";
  private static final String LINE_SEPARATOR = "\n";

  public static String generate(IStoreItem doc)
  {
    if (!(doc instanceof Document))
    {
      return null;
    }
    Document collection = (Document) doc;
    StringBuilder header = new StringBuilder();
    if (collection.isIndexed())
    {
      header.append("Time,");
    }
    if (collection instanceof ILocations)
    {
      header.append("Lat(Degs),Long(Degs)");
    }
    else
    {
      // "(" and "(" has special meaning in CsvParser (separate unit)
      // replace with "[" and "]"
      String name = collection.getName();
      name = name.replace(LEFT_PARENTHESES, LEFT_BRACKET);
      name = name.replace(RIGHT_PARENTHESES, RIGHT_BRACKET);
      header.append(name);
      addUnit(header, collection);
    }
    header.append(LINE_SEPARATOR);

    Iterator<Long> timesIterator = null;
    if (collection.isIndexed())
    {
      timesIterator = collection.getIndices();
    }

    if (collection instanceof LocationDocument)
    {
      LocationDocument ldoc = (LocationDocument) collection;
      Iterator<Point2D> locs = ldoc.getLocationIterator();
      while (locs.hasNext())
      {
        if (timesIterator != null && timesIterator.hasNext())
        {
          Long time = timesIterator.next();
          header.append(CsvParser.getDateFormat().format(new Date(time)));
          header.append(COMMA_SEPARATOR);
        }
        Point2D point = (Point2D) locs.next();
        header.append(point.getY());
        header.append(COMMA_SEPARATOR);
        header.append(point.getX());
        header.append(LINE_SEPARATOR);
      }
    }
    else if (collection instanceof NumberDocument)
    {
      NumberDocument ldoc = (NumberDocument) collection;
      Iterator<Double> locs = ldoc.getIterator();
      while (locs.hasNext())
      {
        if (timesIterator != null && timesIterator.hasNext())
        {
          Long time = timesIterator.next();
          header.append(CsvParser.getDateFormat().format(new Date(time)));
          header.append(COMMA_SEPARATOR);
        }
        Double point = locs.next();
        header.append(point);
        header.append(LINE_SEPARATOR);
      }
    }

    return header.toString();
  }

  private static void addUnit(StringBuilder header, Document collection)
  {
    if (collection.isQuantity())
    {
      header.append(LEFT_PARENTHESES);
      NumberDocument nd = (NumberDocument) collection;
      String unitSymbol = nd.getUnits().toString();
      // DEGREE_ANGLE
      if ("°".equals(unitSymbol))
      {
        header.append("Degs");
      }
      else
      {
        header.append(unitSymbol);
      }
      header.append(RIGHT_PARENTHESES);
    }
  }
}