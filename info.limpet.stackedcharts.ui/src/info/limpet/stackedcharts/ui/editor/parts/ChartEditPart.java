package info.limpet.stackedcharts.ui.editor.parts;

import info.limpet.stackedcharts.model.Chart;
import info.limpet.stackedcharts.model.ChartSet;
import info.limpet.stackedcharts.model.StackedchartsPackage;
import info.limpet.stackedcharts.ui.editor.commands.DeleteChartCommand;
import info.limpet.stackedcharts.ui.editor.figures.ChartFigure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.swt.SWT;

public class ChartEditPart extends AbstractGraphicalEditPart implements ActionListener
{

  public enum ChartPanePosition
  {
    LEFT, RIGHT
  }

  private ChartAdapter adapter = new ChartAdapter();

  @Override
  public void activate()
  {
    super.activate();
    getChart().eAdapters().add(adapter);
  }

  @Override
  public void deactivate()
  {
    getChart().eAdapters().remove(adapter);
    super.deactivate();
  }

  @Override
  protected IFigure createFigure()
  {
    return new ChartFigure(getChart(),this);
  }

  @Override
  protected void createEditPolicies()
  {
    installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,
        new NonResizableEditPolicy());
    
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy()
    {
      protected Command createDeleteCommand(GroupRequest deleteRequest)
      {
        Chart chart = getChart();
        ChartSet parent = chart.getParent();
        DeleteChartCommand deleteChartCommand = new DeleteChartCommand(parent, chart);
        return deleteChartCommand;
      }
    });
  }

  private Chart getChart()
  {
    return (Chart) getModel();
  }

  @Override
  protected List<ChartPanePosition> getModelChildren()
  {
    return Arrays.asList(ChartPanePosition.values());
  }

  @Override
  protected void refreshVisuals()
  {
    String name = getChart().getName();
    ((ChartFigure) getFigure()).setName(name);

    GridData gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.horizontalAlignment = SWT.FILL;
    gridData.verticalAlignment = SWT.FILL;

    ((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure,
        gridData);

  }

  protected void refreshChildren()
  {
    // remove all Childs
    @SuppressWarnings("unchecked")
    List<EditPart> children = getChildren();
    for (EditPart object : new ArrayList<EditPart>(children))
    {
      removeChild(object);
    }
    //add back all model elements 
    List<ChartPanePosition> modelObjects = getModelChildren();
    for (int i = 0; i < modelObjects.size(); i++)
    {

      addChild(createChild(modelObjects.get(i)), i);

    }
    
    ((ChartFigure) getFigure()).getLayoutManager().layout(getFigure());
  }
  
  
  @Override
  public void actionPerformed(ActionEvent event)
  {
    Command deleteCommand = getCommand(new GroupRequest(REQ_DELETE));
    if (deleteCommand != null)
    {
      CommandStack commandStack = getViewer().getEditDomain().getCommandStack();
      commandStack.execute(deleteCommand);
    }
    
  }

  public class ChartAdapter implements Adapter
  {

    @Override
    public void notifyChanged(Notification notification)
    {
      int featureId = notification.getFeatureID(StackedchartsPackage.class);
      switch (featureId)
      {
      case StackedchartsPackage.CHART__NAME:
        refreshVisuals();
        break;
      case StackedchartsPackage.CHART__MAX_AXES:
        refreshChildren();
        break;
      case StackedchartsPackage.CHART__MIN_AXES:
        refreshChildren();
        break;
      }
    }

    @Override
    public Notifier getTarget()
    {
      return getChart();
    }

    @Override
    public void setTarget(Notifier newTarget)
    {
      // Do nothing.
    }

    @Override
    public boolean isAdapterForType(Object type)
    {
      return type.equals(Chart.class);
    }
  }

}
