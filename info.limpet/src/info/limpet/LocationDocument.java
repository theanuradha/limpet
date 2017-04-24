package info.limpet;

import info.limpet.operations.spatial.GeoSupport;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.january.DatasetException;
import org.eclipse.january.dataset.Dataset;
import org.eclipse.january.dataset.DatasetUtils;
import org.eclipse.january.dataset.DoubleDataset;
import org.eclipse.january.dataset.ILazyDataset;
import org.eclipse.january.dataset.IndexIterator;
import org.eclipse.january.dataset.ObjectDataset;
import org.eclipse.january.metadata.AxesMetadata;

public class LocationDocument extends Document implements ILocations,
    IObjectDocument
{

  public LocationDocument(ObjectDataset dataset, ICommand predecessor)
  {
    super(dataset, predecessor);
  }

  public boolean isQuantity()
  {
    return false;
  }

  @Override
  public String toString()
  {
    StringBuffer res = new StringBuffer();

    ObjectDataset dataset = (ObjectDataset) this.getDataset();
    final AxesMetadata axesMetadata =
        dataset.getFirstMetadata(AxesMetadata.class);
    final IndexIterator iterator = dataset.getIterator();

    final DoubleDataset axisDataset;
    if (axesMetadata != null && axesMetadata.getAxes().length > 0)
    {
      DoubleDataset doubleAxis = null;
      try
      {
        ILazyDataset rawAxis = axesMetadata.getAxes()[0];
        Dataset axis = DatasetUtils.sliceAndConvertLazyDataset(rawAxis);
        doubleAxis = DatasetUtils.cast(DoubleDataset.class, axis);
      }
      catch (DatasetException e)
      {
        e.printStackTrace();
      }
      axisDataset = doubleAxis != null ? doubleAxis : null;
    }
    else
    {
      axisDataset = null;
    }

    res.append(dataset.getName() + ":\n");
    while (iterator.hasNext())
    {
      final String indexVal;
      if (axisDataset != null)
      {
        indexVal = "" + axisDataset.getElementDoubleAbs(iterator.index);
      }
      else
      {
        indexVal = "N/A";
      }

      res.append(indexVal + " : " + dataset.getElementDoubleAbs(iterator.index));
      res.append(";");
    }
    res.append("\n");

    return res.toString();
  }

  /**
   * retrieve the location at the specified time (even if it's a non-temporal collection)
   * 
   * @param iCollection
   *          set of locations to use
   * @param thisTime
   *          time we're need a location for
   * @return
   */
  public Point2D locationAt(long thisTime)
  {
    Point2D res = null;
    if (isIndexed())
    {
      res = interpolateValue(thisTime);
    }
    else
    {
      res = getLocationIterator().next();
    }
    return res;
  }

  //
  // public Point2D interpolateValue(long i, InterpMethod linear)
  // {
  // Point2D res = null;
  //
  // // do we have axes?
  // AxesMetadata index = dataset.getFirstMetadata(AxesMetadata.class);
  // ILazyDataset indexDataLazy = index.getAxes()[0];
  // try
  // {
  // Dataset indexData = DatasetUtils.sliceAndConvertLazyDataset(indexDataLazy);
  //
  // // check the target index is within the range
  // double lowerIndex = indexData.getDouble(0);
  // int indexSize = indexData.getSize();
  // double upperVal = indexData.getDouble(indexSize - 1);
  // if(i >= lowerIndex && i <= upperVal)
  // {
  // // ok, create an dataset that captures this specific time
  // LongDataset indexes = (LongDataset) DatasetFactory.createFromObject(new Long[]{i});
  //
  // // perform the interpolation
  // Dataset dOut = Maths.interpolate(indexData, ds, indexes, 0, 0);
  //
  // // get the single matching value out
  // res = dOut.getDouble(0);
  // }
  // }
  // catch (DatasetException e)
  // {
  // e.printStackTrace();
  // }
  //
  // return res;
  // }

  private Point2D interpolateValue(long time)
  {
    final Point2D res;

    // ok, find the values either side
    int beforeIndex = -1, afterIndex = -1;
    long beforeTime = 0, afterTime = 0;

    Iterator<Long> tIter = getIndices();
    int ctr = 0;
    while (tIter.hasNext())
    {
      Long thisT = (Long) tIter.next();
      if (thisT <= time)
      {
        beforeIndex = ctr;
        beforeTime = thisT;
      }
      if (thisT >= time)
      {
        afterIndex = ctr;
        afterTime = thisT;
        break;
      }

      ctr++;
    }

    if (beforeIndex >= 0 && afterIndex == 0)
    {
      ObjectDataset od = (ObjectDataset) getDataset();
      res = (Point2D) od.get(beforeIndex);
    }
    else if (beforeIndex >= 0 && afterIndex >= 0)
    {
      if (beforeIndex == afterIndex)
      {
        // special case - it falls on one of our values
        ObjectDataset od = (ObjectDataset) getDataset();
        res = (Point2D) od.get(beforeIndex);
      }
      else
      {
        final ObjectDataset od = (ObjectDataset) getDataset();
        final Point2D beforeVal = (Point2D) od.get(beforeIndex);
        final Point2D afterVal = (Point2D) od.get(afterIndex);

        double latY0 = beforeVal.getY();
        double latY1 = afterVal.getY();

        double longY0 = beforeVal.getX();
        double longY1 = afterVal.getX();

        double x0 = beforeTime;
        double x1 = afterTime;
        double x = time;

        double newResLat = latY0 + (latY1 - latY0) * (x - x0) / (x1 - x0);
        double newResLong = longY0 + (longY1 - longY0) * (x - x0) / (x1 - x0);

        // ok, we can do the calc
        res = GeoSupport.getCalculator().createPoint(newResLong, newResLat);
      }
    }
    else
    {
      res = null;
    }

    return res;
  }

  public MyStats stats()
  {
    return new MyStats();
  }

  public class MyStats
  {
    public double min()
    {
      DoubleDataset ds = (DoubleDataset) dataset;
      return (Double) ds.min(true);
    }

    public double max()
    {
      DoubleDataset ds = (DoubleDataset) dataset;
      return (Double) ds.max();

    }

    public double mean()
    {
      DoubleDataset ds = (DoubleDataset) dataset;
      return (Double) ds.mean(true);
    }

    public double variance()
    {
      DoubleDataset ds = (DoubleDataset) dataset;
      return (Double) ds.variance(true);
    }

    public double sd()
    {
      DoubleDataset ds = (DoubleDataset) dataset;
      return (Double) ds.stdDeviation(true);
    }
  }

  @Override
  public Iterator<Point2D> getLocationIterator()
  {
    final Iterator<?> oIter = getObjectIterator();
    return new Iterator<Point2D>()
    {

      @Override
      public boolean hasNext()
      {
        return oIter.hasNext();
      }

      @Override
      public Point2D next()
      {
        return (Point2D) oIter.next();
      }

      @Override
      public void remove()
      {
        oIter.remove();
      }
    };
  }

  @Override
  public Iterator<?> getObjectIterator()
  {
    ObjectDataset od = (ObjectDataset) dataset;
    Object[] strings = od.getData();
    Iterable<Object> iterable = Arrays.asList(strings);
    return iterable.iterator();
  }
}