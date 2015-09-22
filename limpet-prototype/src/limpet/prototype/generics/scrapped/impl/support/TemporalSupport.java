package limpet.prototype.generics.scrapped.impl.support;

import java.util.ArrayList;
import java.util.Collection;

import limpet.prototype.generics.iscrapped.nterfaces.ITemporalCollection;

/** utility class to provide support in temporal overview of data
 * 
 * @author ian
 *
 */
public class TemporalSupport implements ITemporalCollection
{
	private final ArrayList<Long> _times;

	public TemporalSupport(ArrayList<Long> times)
	{
		_times = times;
	}
	

	@Override
	public long start()
	{
		if (size() > 0)
		{
			return _times.get(0);
		}
		return -1;
	}

	private int size()
	{
		return _times.size();
	}


	@Override
	public long finish()
	{
		if (size() > 0)
		{
			return _times.get(size() - 1);
		}
		return -1;
	}

	@Override
	public long duration()
	{
		if (size() == 1)
		{
			return 0;
		}
		else if (size() > 1)
		{
			return _times.get(size() - 1) - _times.get(0);
		}
		return -1;
	}

	@Override
	public double rate()
	{
		if (size() > 1)
			return size() / duration();
		else
			return -1;
	}


	public Collection<Long> getTimes()
	{
		return _times;
	}
}