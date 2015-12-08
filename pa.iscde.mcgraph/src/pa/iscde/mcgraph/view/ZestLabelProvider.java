package pa.iscde.mcgraph.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IFigureProvider;

import pa.iscde.mcgraph.model.MethodRep;

class ZestLabelProvider extends LabelProvider implements IFigureProvider {

	@Override
	public String getText(Object obj) {
		if (obj instanceof MethodRep) {
			MethodRep rep = (MethodRep) obj;
			return rep.toString();
		}
		
		//Ligações
		if (obj instanceof EntityConnectionData) {
		      EntityConnectionData test = (EntityConnectionData) obj;
		      return "";
		    }
		throw new RuntimeException("Wrong type: " + obj.getClass().toString());
	}

	@Override
	public IFigure getFigure(Object element) {
		RectangleFigure r = new RectangleFigure();
		r.setSize(50, 50);
		r.setBackgroundColor(ColorConstants.red);
		return null;
	}
}
