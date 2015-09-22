package limpet.prototype.generics.dinko.impl;

import java.util.ArrayList;
import java.util.Collection;

import limpet.prototype.generics.dinko.interfaces.ITemporalCollection;

public class TimeHelper implements ITemporalCollection
{

	private ArrayList<Long> _times;

	public TimeHelper(ArrayList<Long> times)
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

	@Override
	public Collection<Long> getTimes()
	{
		return _times;
	}

}
