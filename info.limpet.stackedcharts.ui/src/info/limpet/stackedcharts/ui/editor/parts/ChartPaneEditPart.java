package info.limpet.stackedcharts.ui.editor.parts;

import java.util.List;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import info.limpet.stackedcharts.model.Chart;
import info.limpet.stackedcharts.ui.editor.parts.ChartEditPart.ChartPanePosition;

public class ChartPaneEditPart extends AbstractGraphicalEditPart
{

  @Override
  protected IFigure createFigure()
  {
    RectangleFigure figure = new RectangleFigure();
    figure.setOutline(false);
    GridLayout layoutManager = new GridLayout();
    // zero margin, in order to connect the dependent axes to the shared one
    layoutManager.marginHeight = 0;
    layoutManager.marginWidth = 0;
    figure.setLayoutManager(layoutManager);
    return figure;
  }

  @Override
  protected void createEditPolicies()
  {
  }

  @Override
  protected void refreshVisuals()
  {
    ChartEditPart.ChartPanePosition pos = (ChartPanePosition) getModel();
    IFigure figure = getFigure();
    if (pos == ChartPanePosition.LEFT)
    {
      ((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure,
          BorderLayout.LEFT);
    }
    else
    {
      ((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure,
          BorderLayout.RIGHT);
    }

    ((GridLayout) getFigure().getLayoutManager()).numColumns =
        getModelChildren().size();
  }

  @Override
  protected List getModelChildren()
  {

    Chart chart = (Chart) getParent().getModel();
    ChartEditPart.ChartPanePosition pos = (ChartPanePosition) getModel();
    return pos == ChartPanePosition.LEFT ? chart.getMinAxes() : chart
        .getMaxAxes();
  }
}