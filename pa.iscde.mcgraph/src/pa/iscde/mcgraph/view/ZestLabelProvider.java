package pa.iscde.mcgraph.view;

import java.util.HashMap;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IFigureProvider;
import pa.iscde.mcgraph.internal.McGraph;
import pa.iscde.mcgraph.model.MethodRep;
import pa.iscde.mcgraph.service.McGraphLayout;

class ZestLabelProvider extends LabelProvider implements IFigureProvider {

	private McGraph mc;

	public ZestLabelProvider(McGraph mc) {
		// TODO Auto-generated constructor stub
		this.mc = mc;
	}

	@Override
	public String getText(Object obj) {
		if (obj instanceof MethodRep) {
			MethodRep rep = (MethodRep) obj;
			return rep.toString();
		}

		// Ligações
		if (obj instanceof EntityConnectionData) {
			EntityConnectionData test = (EntityConnectionData) obj;
			return "";
		}
		throw new RuntimeException("Wrong type: " + obj.getClass().toString());
	}

	@Override
	public IFigure getFigure(Object element) {
		HashMap<String, McGraphLayout> hash = mc.getLayouts();
		if (element instanceof MethodRep) {
			MethodRep rep = (MethodRep) element;
			McGraphLayout l = hash.get(rep.getClassElement().getName().split("\\.")[0]);
			return l.acceptFigure(rep.getClassElement(), rep.getMethodDeclaration());

		}

		return null;
	}
}
