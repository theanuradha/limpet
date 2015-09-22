package limpet.prototype.generics.dinko.interfaces;

import javax.measure.Quantity;

public interface ITemporalQuantityCollection<T extends Quantity<T>> extends ITemporalObjectCollection<Quantity<T>>, IQuantityCollection<T> 
{

}
