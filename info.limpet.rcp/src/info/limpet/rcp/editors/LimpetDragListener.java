/*******************************************************************************
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
 *******************************************************************************/
package info.limpet.rcp.editors;

import java.util.Iterator;

import info.limpet.IStore.IStoreItem;
import info.limpet.rcp.data_provider.data.LimpetWrapper;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;

public class LimpetDragListener extends DragSourceAdapter
{
	public static final String SEPARATOR = "__";
	private StructuredViewer viewer;

	public LimpetDragListener(StructuredViewer viewer)
	{
		this.viewer = viewer;
	}

	@Override
	public void dragFinished(DragSourceEvent event)
	{
		if (!event.doit)
			return;
	}

	/**
	 * Method declared on DragSourceListener
	 */
	public void dragSetData(DragSourceEvent event)
	{
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		StringBuffer items = new StringBuffer();
		Iterator<?> iter = selection.iterator();
		while (iter.hasNext())
		{
			LimpetWrapper object = (LimpetWrapper) iter.next();
			IStoreItem si = (IStoreItem) object.getSubject();
			items.append(si.getUUID().toString());
			items.append(SEPARATOR);
		}
		
		if (TextTransfer.getInstance().isSupportedType(event.dataType))
		{
			event.data = items.toString();
		}
	}

	/**
	 * Method declared on DragSourceListener
	 */
	public void dragStart(DragSourceEvent event)
	{
		event.doit = !viewer.getSelection().isEmpty();
	}
}