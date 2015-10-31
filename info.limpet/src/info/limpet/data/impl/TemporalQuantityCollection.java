package info.limpet.data.impl;

import info.limpet.ICommand;
import info.limpet.IQuantityCollection;
import info.limpet.ITemporalQuantityCollection;
import info.limpet.QuantityRange;
import info.limpet.data.impl.helpers.QuantityHelper;

import java.util.ArrayList;

import javax.measure.Measurable;
import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Dimension;
import javax.measure.unit.Unit;

//public class QuantityCollection<T extends Quantity> extends
//ObjectCollection<T> implements IQuantityCollection<T>

//public interface ITemporalQuantityCollection<Q extends Quantity> extends
//ITemporalObjectCollection<Measurable<Q>>,IBaseQuantityCollection<Q>, IQuantityCollection<Q>

public class TemporalQuantityCollection<T extends Quantity> extends
		TemporalObjectCollection<Measurable<T>> implements
		ITemporalQuantityCollection<T>, IQuantityCollection<T>
{
	transient private QuantityHelper<T> _qHelper;
	
	// TODO: we're duplicating range in this class and the helper, because the helper
	// is transient
	
	private QuantityRange<T> _range;

	private Unit<T> units;

	public TemporalQuantityCollection(String name, Unit<T> units)
	{
		this(name, null, units);
	}

	public TemporalQuantityCollection(String name, ICommand<?> precedent,
			Unit<T> units)
	{
		super(name);
		this.units = units;
		_qHelper = new QuantityHelper<T>((ArrayList<Measurable<T>>) values, units);
	}
	
	

	@Override
	public void add(long time, Measurable<T> object)
	{
		if(object instanceof Measure)
		{
			Measure<?, ?> oM = (Measure<?, ?>) object;
			Unit<?> hisUnits = oM.getUnit();
			if(getUnits() != null)
			{
				if(!getUnits().equals(hisUnits))
				{
					throw new RuntimeException("Measurement is in wrong units");
				}
			}
		}
		// double-check the units
		super.add(time, object);
	}

	@Override
	public void add(long time, Number value)
	{
		super.add(time, Measure.valueOf(value.doubleValue(), getUnits()));
	}

	@Override
	public void add(Number value)
	{
		throw new UnsupportedOperationException(
				"Please use add(time, value) for time series datasets");
	}

	@Override
	public Measurable<T> min()
	{
		initQHelper();
		return _qHelper.min();
	}

	@Override
	public Measurable<T> max()
	{
		initQHelper();
		return _qHelper.max();
	}

	@Override
	public Measurable<T> mean()
	{
		initQHelper();
		return _qHelper.mean();
	}

	@Override
	public Measurable<T> variance()
	{
		initQHelper();
		return _qHelper.variance();
	}

	@Override
	public Measurable<T> sd()
	{
		initQHelper();
		return _qHelper.sd();
	}

	@Override
	public boolean isQuantity()
	{
		return true;
	}

	@Override
	public boolean isTemporal()
	{
		return true;
	}

	protected void initQHelper()
	{
		if(_qHelper == null)
		{
			_qHelper = new QuantityHelper<T>((ArrayList<Measurable<T>>) values, units);
		}
	}
	

	@Override
	public Dimension getDimension()
	{
		initQHelper();
		return _qHelper.getDimension();
	}

	@Override
	public Unit<T> getUnits()
	{
		initQHelper();
		return _qHelper.getUnits();
	}

	@Override
	public void replaceSingleton(double newValue)
	{
		initQHelper();
		_qHelper.replace(newValue);
	}

	@Override
	public void setRange(QuantityRange<T> range)
	{
		initQHelper();
		_range = range;
		_qHelper.setRange(range);
	}

	@Override
	public QuantityRange<T> getRange()
	{
		return _range;
	}
}