package info.limpet.rcp.editors;

import info.limpet.ICommand;
import info.limpet.IOperation;
import info.limpet.IStore;
import info.limpet.IStore.IStoreItem;
import info.limpet.data.operations.AddLayerOperation;
import info.limpet.data.operations.AddLayerOperation.StringProvider;
import info.limpet.data.operations.GenerateDummyDataOperation;
import info.limpet.data.persistence.xml.XStreamHandler;
import info.limpet.data.store.InMemoryStore;
import info.limpet.data.store.InMemoryStore.StoreChangeListener;
import info.limpet.rcp.data_provider.data.DataModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

public class DataManagerEditor extends EditorPart implements
		StoreChangeListener
{

	private IStore _store;
	private TreeViewer viewer;
	private IMenuListener _menuListener;
	private Action action1;
	private Action refreshView;
	private Action generateData;
	private Action addLayer;
	private boolean _dirty = false;
	private DataModel _model;
	private StoreChangeListener _changeListener = new StoreChangeListener()
	{

		@Override
		public void changed()
		{
			_dirty = true;
			firePropertyChange(PROP_DIRTY);
		}
	};

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException
	{
		// FIXME we will support FileEditorInput, FileStoreEditorInput and
		// FileRevisionEditorInput
		if (input instanceof IFileEditorInput)
		{
			try
			{
				_store = new XStreamHandler()
						.load(((IFileEditorInput) input).getFile());
			}
			catch (Exception e)
			{
				// FIXME temporary workaround
				_store = new InMemoryStore();
			}
		}
		_store.addChangeListener(_changeListener);
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	@Override
	public boolean isDirty()
	{
		return _dirty;
	}

	@Override
	public boolean isSaveAsAllowed()
	{
		// TODO
		return false;
	}

	@Override
	public void createPartControl(Composite parent)
	{
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		_model = new DataModel();
		viewer.setContentProvider(_model);
		viewer.setLabelProvider(new LimpetLabelProvider());
		viewer.setInput(_store);

		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();

		IActionBars bars = getEditorSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());

		// ok, setup as listener
		IStore store = (IStore) viewer.getInput();

		if (store instanceof InMemoryStore)
		{
			IStore ms = (IStore) store;
			ms.addChangeListener(this);
		}

	}

	protected void fillLocalToolBar(IToolBarManager manager)
	{
		manager.add(refreshView);
		manager.add(generateData);
		manager.add(addLayer);
	}

	private void makeActions()
	{

		action1 = new Action()
		{
			public void run()
			{
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				showMessage("Action 1 executed on " + obj.toString());
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		generateData = new Action()
		{
			public void run()
			{
				generateData();
			}
		};
		generateData.setText("Generate data");
		generateData.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));

		addLayer = new Action()
		{
			public void run()
			{
				addLayer();
			}
		};
		addLayer.setText("Add Layer");
		addLayer.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));

		refreshView = new Action()
		{
			public void run()
			{
				viewer.refresh();
			}
		};
		refreshView.setText("Refresh");
		refreshView.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
	}

	protected void generateData()
	{
		GenerateDummyDataOperation operation = new GenerateDummyDataOperation(
				"small", 20);

		Object input = viewer.getInput();
		Collection<ICommand<IStoreItem>> commands = operation.actionsFor(
				getSuitableObjects(), (IStore) input);
		commands.iterator().next().execute();
	}

	protected void addLayer()
	{
		IOperation<IStoreItem> operation = new AddLayerOperation(
				new StringProvider()
				{

					@Override
					public String getString(String title)
					{
						String res = null;
						InputDialog dlg = new InputDialog(Display.getCurrent()
								.getActiveShell(), title, null, null,
								null);
						if (dlg.open() == Window.OK)
						{
							// User clicked OK; update the label with the input
							res = dlg.getValue();
						}

						return res;
					}
				});

		Object input = viewer.getInput();
		Collection<ICommand<IStoreItem>> commands = operation.actionsFor(
				getSuitableObjects(), (IStore) input);
		commands.iterator().next().execute();
	}

	public void showMessage(String message)
	{
		MessageDialog.openInformation(viewer.getControl().getShell(),
				"Data Manager Editor", message);
	}

	protected IMenuListener createContextMenuListener()
	{
		return new IMenuListener()
		{
			public void menuAboutToShow(IMenuManager menu)
			{
				setFocus();
				editorContextMenuAboutToShow(menu);
			}
		};
	}

	protected void editorContextMenuAboutToShow(IMenuManager menu)
	{
		// get any suitable objects from selection
		List<IStoreItem> selection = getSuitableObjects();

		// include some top level items
		showThisList(selection, menu, OperationsLibrary.getTopLevel());

		// now the tree of operations
		menu.add(new Separator());

		// get the list of operations
		HashMap<String, List<IOperation<?>>> ops = OperationsLibrary
				.getOperations();

		// did we find anything?
		Iterator<String> hIter = ops.keySet().iterator();

		while (hIter.hasNext())
		{
			// ok, we're in a menu grouping
			String name = (String) hIter.next();

			// create a new menu tier
			MenuManager newM = new MenuManager(name);
			menu.add(newM);

			// now loop through this set of operations
			List<IOperation<?>> values = ops.get(name);

			showThisList(selection, newM, values);
		}

		menu.add(new Separator());
		menu.add(refreshView);
	}

	private void showThisList(List<IStoreItem> selection, IMenuManager newM,
			List<IOperation<?>> values)
	{
		Iterator<IOperation<?>> oIter = values.iterator();
		while (oIter.hasNext())
		{
			@SuppressWarnings("unchecked")
			final IOperation<IStoreItem> op = (IOperation<IStoreItem>) oIter.next();
			final IStore theStore = _store;
			Collection<ICommand<IStoreItem>> matches = op.actionsFor(selection,
					theStore);

			Iterator<ICommand<IStoreItem>> mIter = matches.iterator();
			while (mIter.hasNext())
			{
				final ICommand<IStoreItem> thisC = (ICommand<IStoreItem>) mIter.next();
				newM.add(new Action(thisC.getTitle())
				{
					@Override
					public void run()
					{
						thisC.execute();
					}
				});
			}
		}
	}

	private List<IStoreItem> getSuitableObjects()
	{
		ArrayList<IStoreItem> matches = new ArrayList<IStoreItem>();

		// ok, find the applicable operations
		ISelection sel = viewer.getSelection();
		IStructuredSelection str = (IStructuredSelection) sel;
		Iterator<?> iter = str.iterator();
		while (iter.hasNext())
		{
			Object object = (Object) iter.next();
			if (object instanceof IStoreItem)
			{
				matches.add((IStoreItem) object);
			}
			else if (object instanceof IAdaptable)
			{
				IAdaptable ada = (IAdaptable) object;
				Object match = ada.getAdapter(IStoreItem.class);
				if (match != null)
				{
					matches.add((IStoreItem) match);
				}
			}
		}

		return matches;
	}

	protected final IMenuListener getContextMenuListener()
	{
		if (_menuListener == null)
			_menuListener = createContextMenuListener();
		return _menuListener;
	}

	private void hookContextMenu()
	{
		String id = "#DataManagerEditor";
		MenuManager menuMgr = new MenuManager(id, id);
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(getContextMenuListener());
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	@Override
	public void setFocus()
	{
		viewer.getControl().setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor)
	{
		final IEditorInput input = getEditorInput();
		// FIXME we will support FileEditorInput, FileStoreEditorInput and
		// FileRevisionEditorInput
		if (input instanceof IFileEditorInput)
		{
			IFile file = ((IFileEditorInput) input).getFile();
			try
			{
				new XStreamHandler().save(_store, file);
				_dirty = false;
				firePropertyChange(PROP_DIRTY);
			}
			catch (CoreException | IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void doSaveAs()
	{
		// TODO
	}

	@Override
	public void changed()
	{
		viewer.refresh();
	}

	@Override
	public void dispose()
	{
		super.dispose();
		_store.removeChangeListener(_changeListener);
	}

}
