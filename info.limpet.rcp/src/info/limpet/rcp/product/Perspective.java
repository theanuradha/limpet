package info.limpet.rcp.product;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import info.limpet.rcp.analysis_view.AnalysisView;
import info.limpet.rcp.data_frequency.DataFrequencyView;
import info.limpet.rcp.xy_plot.XyPlotView;

public class Perspective implements IPerspectiveFactory
{
	public static final String ID = "info.limpet.product.perspective";

	@SuppressWarnings("deprecation")
	public void createInitialLayout(IPageLayout layout)
	{
		final String editorArea = layout.getEditorArea();

		final IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.3f, editorArea);
		topLeft.addView(IPageLayout.ID_RES_NAV);
		
		final IFolderLayout bottomLeft = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, 0.4f, "topLeft");
		bottomLeft.addView(IPageLayout.ID_PROP_SHEET);
				
		final IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.5f, editorArea);
		bottom.addView(DataFrequencyView.ID);

		final IFolderLayout bottomRight = layout.createFolder("bottomRight", IPageLayout.RIGHT, 0.5f, "bottom");
		bottomRight.addView(XyPlotView.ID);

		final IFolderLayout topRight = layout.createFolder("topRight", IPageLayout.RIGHT, 0.6f, editorArea);
		topRight.addView(AnalysisView.ID);
		
		
		// and our view shortcuts
		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		layout.addShowViewShortcut(AnalysisView.ID);
		layout.addShowViewShortcut(DataFrequencyView.ID);
		layout.addShowViewShortcut(XyPlotView.ID);
	}
}
