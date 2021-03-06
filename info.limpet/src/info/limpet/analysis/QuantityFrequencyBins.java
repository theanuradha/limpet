/*****************************************************************************
 *  Limpet - the Lightweight InforMation ProcEssing Toolkit
 *  http://limpet.info
 *
 *  (C) 2015-2016, Deep Blue C Technologies Ltd
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the Eclipse Public License v1.0
 *  (http://www.eclipse.org/legal/epl-v10.html)
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *****************************************************************************/
package info.limpet.analysis;

import info.limpet.ICollection;
import info.limpet.IQuantityCollection;
import info.limpet.IStoreItem;
import info.limpet.data.operations.CollectionComplianceTests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.measure.Measurable;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.apache.commons.math3.random.EmpiricalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public abstract class QuantityFrequencyBins extends CoreAnalysis
{
  private static final int MAX_SIZE = 10000;
  private static final double THRESHOLD_VALUE = 0.001;
  private final CollectionComplianceTests aTests;

  public QuantityFrequencyBins()
  {
    super("Quantity Frequency Bins");
    aTests = new CollectionComplianceTests();
  }

  public static class BinnedData extends ArrayList<Bin>
  {
    /**
		 * 
		 */
    private static final long serialVersionUID = 1L;

    public BinnedData()
    {
    }
  }

  public static class Bin
  {
    private final double lowerVal;
    private final double upperVal;
    private final long freqVal;

    public Bin(final double lower, final double upper, long freq)
    {
      upperVal = upper;
      lowerVal = lower;
      freqVal = freq;
    }

    public double getLowerVal()
    {
      return lowerVal;
    }

    public double getUpperVal()
    {
      return upperVal;
    }

    public long getFreqVal()
    {
      return freqVal;
    }
  }

  public static BinnedData doBins(IQuantityCollection<Quantity> collection)
  {
    // collate the values into an array
    double[] data = new double[collection.getValuesCount()];

    // Add the data from the array
    int ctr = 0;
    Iterator<Measurable<Quantity>> iterV = collection.getValues().iterator();
    while (iterV.hasNext())
    {
      Measurable<Quantity> object = (Measurable<Quantity>) iterV.next();

      Unit<Quantity> theseUnits = collection.getUnits();
      data[ctr++] = object.doubleValue(theseUnits);
    }

    // Get a DescriptiveStatistics instance
    DescriptiveStatistics stats = new DescriptiveStatistics(data);

    // also do some frequency binning
    double range = stats.getMax() - stats.getMin();

    // aah, double-check we don't have zero range
    final int binCount;
    if (range > 10)
    {
      binCount = 10;
    }
    else
    {
      binCount = (int) Math.max(2, range);
    }

    BinnedData res = new BinnedData();

    if (range > THRESHOLD_VALUE)
    {

      long[] histogram = new long[binCount];
      EmpiricalDistribution distribution = new EmpiricalDistribution(binCount);
      distribution.load(data);

      int k = 0;
      for (SummaryStatistics sStats : distribution.getBinStats())
      {
        histogram[k++] = sStats.getN();
      }

      double rangeSoFar = stats.getMin();
      double rangeStep = range / binCount;
      for (int i = 0; i < histogram.length; i++)
      {
        long l = histogram[i];
        res.add(new Bin(rangeSoFar, rangeSoFar + rangeStep, l));
        rangeSoFar += rangeStep;
      }
    }
    return res;
  }

  @Override
  public void analyse(List<IStoreItem> selection)
  {
    List<String> titles = new ArrayList<String>();
    List<String> values = new ArrayList<String>();

    // check compatibility
    if (appliesTo(selection) && selection.size() == 1)
    {
      // ok, let's go for it.
      for (Iterator<IStoreItem> iter = selection.iterator(); iter.hasNext();)
      {
        ICollection thisC = (ICollection) iter.next();
        @SuppressWarnings("unchecked")
        IQuantityCollection<Quantity> o = (IQuantityCollection<Quantity>) thisC;

        if (thisC.getValuesCount() > 1 && thisC.getValuesCount() < MAX_SIZE)
        {
          BinnedData res = doBins(o);

          // now output the bins
          StringBuffer freqBins = new StringBuffer();

          Iterator<Bin> bIter = res.iterator();
          while (bIter.hasNext())
          {
            QuantityFrequencyBins.Bin bin =
                (QuantityFrequencyBins.Bin) bIter.next();
            freqBins.append((int) bin.getLowerVal());
            freqBins.append("-");
            freqBins.append((int) bin.getUpperVal());
            freqBins.append(": ");
            freqBins.append(bin.getFreqVal());
            freqBins.append(", ");

          }

          titles.add("Frequency bins");
          values.add(freqBins.toString());
        }
      }
    }

    if (titles.size() > 0)
    {
      presentResults(titles, values);
    }

  }

  private boolean appliesTo(List<IStoreItem> selection)
  {
    return aTests.allCollections(selection) && aTests.allQuantity(selection)
        && aTests.allEqualUnits(selection);
  }

  protected abstract void presentResults(List<String> titles,
      List<String> values);
}
