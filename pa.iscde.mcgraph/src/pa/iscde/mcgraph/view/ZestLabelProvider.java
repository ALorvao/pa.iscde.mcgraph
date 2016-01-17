package pa.iscde.mcgraph.view;

import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.viewers.internal.IStylingGraphModelFactory;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

import pa.iscde.mcgraph.internal.McGraph;
import pa.iscde.mcgraph.model.MethodRep;
import pa.iscde.mcgraph.service.McGraphLayout;

class ZestLabelProvider extends LabelProvider implements IFigureProvider {


	@Override
	public String getText(Object obj) {
		if (obj instanceof MethodRep) {
			MethodRep rep = (MethodRep) obj;
			return rep.toString();
		}

		// Ligações
		if (obj instanceof EntityConnectionData) {
			return "";
		}
		throw new RuntimeException("Wrong type: " + obj.getClass().toString());
	}

	@Override
	public IFigure getFigure(Object element) {

		return null;
	}
}
