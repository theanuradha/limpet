package info.limpet.data.operations;

import info.limpet.ICollection;
import info.limpet.ICommand;
import info.limpet.IOperation;
import info.limpet.IQuantityCollection;
import info.limpet.IStore;
import info.limpet.IStore.IStoreItem;
import info.limpet.data.commands.AbstractCommand;
import info.limpet.data.impl.QuantityCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.measure.Measurable;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public class SubtractQuantityOperation<Q extends Quantity> implements
		IOperation<IStoreItem>
{
	CollectionComplianceTests aTests = new CollectionComplianceTests();

	public static final String DIFFERENCE_OF_INPUT_SERIES = "Difference of input series";

	final protected String outputName;

	public SubtractQuantityOperation(String name)
	{
		outputName = name;
	}

	public SubtractQuantityOperation()
	{
		this(DIFFERENCE_OF_INPUT_SERIES);
	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	public Collection<ICommand<IStoreItem>> actionsFor(
			List<IStoreItem> selection, IStore destination)
	{
		Collection<ICommand<IStoreItem>> res = new ArrayList<ICommand<IStoreItem>>();

		if (appliesTo(selection))
		{
			ICollection item1 = (ICollection) selection.get(0);
			ICollection item2 = (ICollection) selection.get(1);

			String oName = item2.getName() + " from " + item1.getName();
			ICommand<IStoreItem> newC = new SubtractQuantityValues("Subtract "
					+ item2.getName() + " from " + item1.getName(), oName, selection,
					item1, item2, destination);
			res.add(newC);
			oName = item1.getName() + " from " + item2.getName();
			newC = new SubtractQuantityValues("Subtract " + item1.getName()
					+ " from " + item2.getName(), oName, selection, item2, item1,
					destination);
			res.add(newC);
		}

		return res;
	}

	private boolean appliesTo(List<IStoreItem> selection)
	{
		if (aTests.exactNumber(selection, 2) && aTests.allCollections(selection))
		{
			boolean allQuantity = aTests.allQuantity(selection);
			boolean equalLength = aTests.allEqualLength(selection);
			boolean equalDimensions = aTests.allEqualDimensions(selection);
			return (allQuantity && equalLength && equalDimensions);
		}
		else
		{
			return false;
		}
	}

	public class SubtractQuantityValues<T extends Quantity> extends
			AbstractCommand<IStoreItem>
	{
		IQuantityCollection<T> _item1;
		IQuantityCollection<T> _item2;

		@SuppressWarnings("unchecked")
		public SubtractQuantityValues(String title, String outputName,
				List<IStoreItem> selection, ICollection item1, ICollection item2,
				IStore store)
		{
			super(title, "Subtract provided series", outputName, store, false, false,
					selection);
			_item1 = (IQuantityCollection<T>) item1;
			_item2 = (IQuantityCollection<T>) item2;
		}

		@Override
		public void execute()
		{
			// get the unit
			Unit<T> unit = _item1.getUnits();

			List<IStoreItem> outputs = new ArrayList<IStoreItem>();

			// ok, generate the new series
			IQuantityCollection<T> target = new QuantityCollection<T>(
					getOutputName(), this, unit);

			outputs.add(target);

			// store the output
			super.addOutput(target);

			// start adding values.
			performCalc(unit, outputs, _item1, _item2);

			// tell each series that we're a dependent
			Iterator<IStoreItem> iter = inputs.iterator();
			while (iter.hasNext())
			{
				ICollection iCollection = (ICollection) iter.next();
				iCollection.addDependent(this);
			}

			// ok, done
			List<IStoreItem> res = new ArrayList<IStoreItem>();
			res.add(target);
			getStore().addAll(res);
		}

		@SuppressWarnings(
		{ "rawtypes", "unchecked" })
		@Override
		protected void recalculate()
		{
			// get the unit
			IQuantityCollection first = (IQuantityCollection) inputs.get(0);
			Unit<T> unit = first.getUnits();

			// update the results
			performCalc(unit, outputs, _item1, _item2);
		}

		/**
		 * wrap the actual operation. We're doing this since we need to separate it
		 * from the core "execute" operation in order to support dynamic updates
		 * 
		 * @param unit
		 * @param outputs
		 */
		@SuppressWarnings("unchecked")
		private void performCalc(Unit<T> unit, List<IStoreItem> outputs,
				ICollection item1, ICollection item2)
		{
			IQuantityCollection<T> target = (IQuantityCollection<T>) outputs
					.iterator().next();

			// clear out the lists, first
			Iterator<IStoreItem> iter = outputs.iterator();
			while (iter.hasNext())
			{
				IQuantityCollection<T> qC = (IQuantityCollection<T>) iter.next();
				qC.getValues().clear();
			}

			ICollection firstItem = (ICollection) inputs.get(0);
			
			for (int j = 0; j < firstItem.size(); j++)
			{
				final Measurable<T> thisValue = _item1.getValues().get(j);
				final Measurable<T> otherValue = _item2.getValues().get(j);
				double runningTotal = thisValue.doubleValue(_item1.getUnits()) - otherValue.doubleValue(_item2.getUnits());

				target.add(runningTotal);
			}
		}
	}

}