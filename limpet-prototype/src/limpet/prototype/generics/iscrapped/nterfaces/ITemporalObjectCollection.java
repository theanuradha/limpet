package limpet.prototype.generics.iscrapped.nterfaces;

import java.util.Iterator;


public interface ITemporalObjectCollection<T extends Object> extends ITemporalCollection{
	public void add(long time, T object);
	public Iterator<Doublet<T>> iterator();
	public interface Doublet<T extends Object>
	{
		long getTime();
		T getObject();
	}
}