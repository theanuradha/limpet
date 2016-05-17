package info.limpet.stackedcharts.core.view;

import info.limpet.stackedcharts.editor.StackedchartsEditControl;
import info.limpet.stackedcharts.model.ChartSet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.effects.stw.Transition;
import org.eclipse.nebula.effects.stw.TransitionManager;
import org.eclipse.nebula.effects.stw.Transitionable;
import org.eclipse.nebula.effects.stw.transitions.CubicRotationTransition;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

public class StackedChartsView extends ViewPart
{

  public static final int CHART_VIEW = 1;
  public static final int EDIT_VIEW = 2;

  public static final String ID = "info.limpet.StackedChartsView";

  private StackedPane stackedPane;

  // effects
  protected TransitionManager transitionManager = null;
  private Composite chartHolder;
  private StackedchartsEditControl chartEditor;

  @Override
  public void createPartControl(Composite parent)
  {

    stackedPane = new StackedPane(parent);

    stackedPane.add(CHART_VIEW, createChartView());
    stackedPane.add(EDIT_VIEW, createEditView());
    selectView(CHART_VIEW);
    contributeToActionBars();

    // Drop Support for *.stackedcharts
    connectFileDropSupport(stackedPane);

    // see https://github.com/debrief/limpet/issues/265
    if (!System.getProperty("os.name").toLowerCase().contains("mac"))
    {
      transitionManager = new TransitionManager(new Transitionable()
      {
        public void addSelectionListener(SelectionListener listener)
        {
          stackedPane.addSelectionListener(listener);
        }

        public Control getControl(int index)
        {
          return stackedPane.getControl(index);
        }

        public Composite getComposite()
        {
          return stackedPane;
        }

        public int getSelection()
        {
          return stackedPane.getActiveControlKey();
        }

        public void setSelection(int index)
        {
          stackedPane.showPane(index, false);
        }

        public double getDirection(int toIndex, int fromIndex)
        {
          return toIndex == CHART_VIEW ? Transition.DIR_RIGHT
              : Transition.DIR_LEFT;
        }
      });
      // new SlideTransition(_tm)
      transitionManager.setTransition(new CubicRotationTransition(
          transitionManager));
    }

  }

  protected void connectFileDropSupport(Control compoent)
  {
    DropTarget target =
        new DropTarget(compoent, DND.DROP_MOVE | DND.DROP_COPY
            | DND.DROP_DEFAULT);
    final FileTransfer fileTransfer = FileTransfer.getInstance();
    target.setTransfer(new Transfer[]
    {fileTransfer});
    target.addDropListener(new DropTargetListener()
    {

      @Override
      public void dropAccept(DropTargetEvent event)
      {
        // TODO Auto-generated method stub

      }

      @Override
      public void drop(DropTargetEvent event)
      {

        if (fileTransfer.isSupportedType(event.currentDataType))
        {
          String[] files = (String[]) event.data;
          
            // *.stackedcharts
            if (files.length == 1 && files[0].endsWith("stackedcharts"))
            {
              File file = new File(files[0]);
              Resource resource =
                  new ResourceSetImpl().createResource(URI.createURI(file
                      .toURI().toString()));
              try
              {
                resource.load(new HashMap<>());
                ChartSet chartsSet = (ChartSet) resource.getContents().get(0);
                setModel(chartsSet);
              }
              catch (IOException e)
              {
                e.printStackTrace();
                MessageDialog.openError(
                    Display.getCurrent().getActiveShell(), "Error", e
                        .getMessage());
                
              }
              
            }
          }
        

      }

      @Override
      public void dragOver(DropTargetEvent event)
      {
        

      }

      @Override
      public void dragOperationChanged(DropTargetEvent event)
      {
        if (event.detail == DND.DROP_DEFAULT)
        {
          if ((event.operations & DND.DROP_COPY) != 0)
          {
            event.detail = DND.DROP_COPY;
          }
          else
          {
            event.detail = DND.DROP_NONE;
          }
        }
        if (fileTransfer.isSupportedType(event.currentDataType))
        {
          if (event.detail != DND.DROP_COPY)
          {
            event.detail = DND.DROP_NONE;
          }
        }

      }

      @Override
      public void dragLeave(DropTargetEvent event)
      {

      }

      @Override
      public void dragEnter(DropTargetEvent event)
      {
        if (event.detail == DND.DROP_DEFAULT)
        {
          if ((event.operations & DND.DROP_COPY) != 0)
          {
            event.detail = DND.DROP_COPY;
          }
          else
          {
            event.detail = DND.DROP_NONE;
          }
        }
        for (int i = 0; i < event.dataTypes.length; i++)
        {
          if (fileTransfer.isSupportedType(event.dataTypes[i]))
          {
            event.currentDataType = event.dataTypes[i];
            // files should only be copied
            if (event.detail != DND.DROP_COPY)
            {
              event.detail = DND.DROP_NONE;
            }
            break;
          }
        }

      }
    });
  }

  public void selectView(int view)
  {
    if (stackedPane != null && !stackedPane.isDisposed())
    {
      stackedPane.showPane(view);
    }

  }

  public void setModel(ChartSet charts)
  {
    chartEditor.setModel(charts);
    
    // remove any existing base items
    if (chartHolder != null)
    {
      for (Control control : chartHolder.getChildren())
      {
        control.dispose();
      }
    }

    // and now repopulate
    JFreeChart chart = ChartBuilder.build(charts);
    @SuppressWarnings("unused")
    ChartComposite _chartComposite =
        new ChartComposite(chartHolder, SWT.NONE, chart, true)
        {
          @Override
          public void mouseUp(MouseEvent event)
          {
            super.mouseUp(event);
            JFreeChart c = getChart();
            if (c != null)
            {
              c.setNotify(true); // force redraw
            }
          }
        };

    chartHolder.pack(true);
    chartHolder.getParent().layout();
    selectView(CHART_VIEW);
  }

  protected Control createChartView()
  {

    chartHolder = new Composite(stackedPane, SWT.NONE);
    chartHolder.setLayout(new FillLayout());

    // JFreeChart chart = createChart();
    // @SuppressWarnings("unused")
    // ChartComposite _chartComposite = new ChartComposite(chartHolder, SWT.NONE, chart, true)
    // {
    // @Override
    // public void mouseUp(MouseEvent event)
    // {
    // super.mouseUp(event);
    // JFreeChart c = getChart();
    // if (c != null)
    // {
    // c.setNotify(true); // force redraw
    // }
    // }
    // };

    return chartHolder;
  }

  // private JFreeChart createChart()
  // {
  // return new ChartBuilder(ChartBuilder.createDummyModel()).build();
  // }

  protected Control createEditView()
  {
    chartEditor = new StackedchartsEditControl(stackedPane);
    return chartEditor;
  }

  @Override
  public void setFocus()
  {
    if (stackedPane != null && !stackedPane.isDisposed())
    {
      stackedPane.forceFocus();
    }

  }

  protected void fillLocalPullDown(IMenuManager manager)
  {

  }

  protected void fillLocalToolBar(final IToolBarManager manager)
  {
    manager.add(new Action("Edit")
    {
      @Override
      public void run()
      {
        if (stackedPane.getActiveControlKey() == CHART_VIEW)
        {
          selectView(EDIT_VIEW);
          setText("Chart");
          manager.update(true);
        }
        else
        {
          selectView(CHART_VIEW);
          setText("Edit");

          manager.update(true);
        }
      }
    });
  }

  protected void contributeToActionBars()
  {
    IActionBars bars = getViewSite().getActionBars();
    fillLocalPullDown(bars.getMenuManager());
    fillLocalToolBar(bars.getToolBarManager());
  }

}
