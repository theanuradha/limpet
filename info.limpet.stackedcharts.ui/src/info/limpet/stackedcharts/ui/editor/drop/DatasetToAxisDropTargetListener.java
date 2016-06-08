package info.limpet.stackedcharts.ui.editor.drop;

import info.limpet.stackedcharts.model.Chart;
import info.limpet.stackedcharts.model.Dataset;
import info.limpet.stackedcharts.model.DependentAxis;
import info.limpet.stackedcharts.ui.editor.commands.AddDatasetsToAxisCommand;
import info.limpet.stackedcharts.ui.editor.parts.AxisEditPart;
import info.limpet.stackedcharts.ui.editor.parts.ChartEditPart;
import info.limpet.stackedcharts.ui.editor.parts.ChartPaneEditPart;
import info.limpet.stackedcharts.ui.view.adapter.AdapterRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;

public class DatasetToAxisDropTargetListener implements
    DatasetDropTargetListener
{
  private final GraphicalViewer viewer;
  private AxisEditPart feedback;

  public DatasetToAxisDropTargetListener(GraphicalViewer viewer)
  {
    this.viewer = viewer;
  }
  
  @Override
  public void reset()
  {
    removeFeedback(feedback);
    feedback = null;
  }
  
  @Override
  public boolean isValid(DropTargetEvent event)
  {
    EditPart findObjectAt = findPart(event, viewer);
    return findObjectAt instanceof AxisEditPart;
  }

  @Override
  public void dropAccept(DropTargetEvent event)
  {
  }

  public static boolean
      canDropSelection(final Chart chart, ISelection selection)
  {
    boolean canDrop = true;
    AdapterRegistry adapter = new AdapterRegistry();
    if (selection instanceof StructuredSelection)
    {

      // check the selection
      for (Object obj : ((StructuredSelection) selection).toArray())
      {
        if (adapter.canConvert(obj))
        {
          List<Dataset> convert = adapter.convert(obj);
          if (convert.size() == 0)
          {
            continue;
          }
          for (Dataset dataset : convert)
          {
            if (!canDropDataset(chart, dataset))
            {
              canDrop = false;
              break;
            }
          }
        }
      }
    }

    return canDrop;
  }

  private static boolean datasetAlreadyExistsOnTheseAxes(
      final Iterator<DependentAxis> axes, final String name)
  {
    boolean exists = false;

    while (axes.hasNext())
    {
      final DependentAxis dAxis = (DependentAxis) axes.next();
      Iterator<Dataset> dIter = dAxis.getDatasets().iterator();
      while (dIter.hasNext())
      {
        Dataset thisD = (Dataset) dIter.next();
        if (name.equals(thisD.getName()))
        {
          // ok, we can't add it
          System.err.println("Not adding dataset - duplicate name");
          exists = true;
          break;
        }
      }
    }

    return exists;
  }

  private static boolean
      canDropDataset(final Chart chart, final Dataset dataset)
  {
    boolean possible = true;

    // check the axis
    final Iterator<DependentAxis> minIter = chart.getMinAxes().iterator();
    final Iterator<DependentAxis> maxIter = chart.getMaxAxes().iterator();

    if (datasetAlreadyExistsOnTheseAxes(minIter, dataset.getName())
        || datasetAlreadyExistsOnTheseAxes(maxIter, dataset.getName()))
    {
      possible = false;
    }

    return possible;
  }

  private static List<Object> convertSelection(StructuredSelection selection)
  {
    AdapterRegistry adapter = new AdapterRegistry();
    List<Object> element = new ArrayList<Object>();
    for (Object object : selection.toArray())
    {
      if (adapter.canConvert(object))
      {
        element.add(adapter.convert(object));
      }
    }

    return element;
  }

  @Override
  public void drop(DropTargetEvent event)
  {
    if (LocalSelectionTransfer.getTransfer().isSupportedType(
        event.currentDataType))
    {
      StructuredSelection sel =
          (StructuredSelection) LocalSelectionTransfer.getTransfer()
              .getSelection();
      if (sel.isEmpty())
      {
        event.detail = DND.DROP_NONE;
        return;
      }

      List<Object> dragObjects = convertSelection(sel);

      EditPart findObjectAt = findPart(event, viewer);

      if (findObjectAt instanceof AxisEditPart)
      {
        AxisEditPart axis = (AxisEditPart) findObjectAt;

        // build up the list of data to add
        List<Dataset> datasets = new ArrayList<Dataset>();

        for (Object o : dragObjects)
        {
          if (o instanceof Dataset)
          {
            datasets.add((Dataset) o);
          }
          else if (o instanceof List<?>)
          {
            List<?> list = (List<?>) o;
            for (Iterator<?> iter = list.iterator(); iter.hasNext();)
            {
              Object item = (Object) iter.next();
              if (item instanceof Dataset)
              {
                datasets.add((Dataset) item);
              }
            }
          }
        }
        AddDatasetsToAxisCommand addDatasetsToAxisCommand =
            new AddDatasetsToAxisCommand((DependentAxis) axis.getModel(),
                datasets.toArray(new Dataset[datasets.size()]));

        final CommandStack commandStack =
            viewer.getEditDomain().getCommandStack();
        commandStack.execute(addDatasetsToAxisCommand);

      }
    }
    feedback = null;
  }

  private static EditPart
      findPart(DropTargetEvent event, GraphicalViewer viewer)
  {
    org.eclipse.swt.graphics.Point cP =
        viewer.getControl().toControl(event.x, event.y);
    EditPart findObjectAt = viewer.findObjectAt(new Point(cP.x, cP.y));
    return findObjectAt;
  }

  @Override
  public void dragOver(DropTargetEvent event)
  {
    EditPart findObjectAt = findPart(event, viewer);

    if (feedback == findObjectAt)
    {
      return;
    }

    if (findObjectAt instanceof AxisEditPart
        && LocalSelectionTransfer.getTransfer().isSupportedType(
            event.currentDataType))
    {
      // get the chart model
      AxisEditPart axis = (AxisEditPart) findObjectAt;
      final ChartPaneEditPart parent = (ChartPaneEditPart) axis.getParent();
      final ChartEditPart chartEdit = (ChartEditPart) parent.getParent();
      final Chart chart = chartEdit.getModel();

      if (canDropSelection(chart, LocalSelectionTransfer.getTransfer()
          .getSelection()))
      {

        removeFeedback(feedback);
        feedback = (AxisEditPart) findObjectAt;
        addFeedback(feedback);
        event.detail= DND.DROP_COPY;
      }
      else
      {
        removeFeedback(feedback);
        feedback = null;
        event.detail= DND.DROP_NONE;
      }
    }
    else
    {
      removeFeedback(feedback);
      feedback = null;
      event.detail= DND.DROP_NONE;
    }

  }

  private static void addFeedback(AxisEditPart figure)
  {
    if (figure != null)
    {
      figure.getFigure().setBackgroundColor(ColorConstants.lightGray);
    }
  }

  private static void removeFeedback(AxisEditPart figure)
  {
    if (figure != null)
    {
      figure.getFigure().setBackgroundColor(AxisEditPart.BACKGROUND_COLOR);
    }
  }

  @Override
  public void dragOperationChanged(DropTargetEvent event)
  {
  }

  @Override
  public void dragLeave(DropTargetEvent event)
  {
    removeFeedback(feedback);
    feedback = null;
  }

  @Override
  public void dragEnter(DropTargetEvent event)
  {
  }

  @Override
  public boolean isEnabled(DropTargetEvent event)
  {
    return LocalSelectionTransfer.getTransfer().isSupportedType(
        event.currentDataType);
  }

  @Override
  public Transfer getTransfer()
  {
    return LocalSelectionTransfer.getTransfer();
  }
}
