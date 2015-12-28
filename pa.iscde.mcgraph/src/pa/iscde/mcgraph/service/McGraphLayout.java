package pa.iscde.mcgraph.service;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;

/*
 * McGraphLayout Interface
 * Used for McGraphLayout Extension Point
 * 
 */
public interface McGraphLayout {

	/**
	 * Creates a IFigure. The IFigure can be customised for a specific
	 * ClassElement or MethodDeclaration. If it returns null the node will not
	 * be changed.
	 * 
	 * @param c
	 *            ClassElement
	 * @param md
	 *            MethodDeclaration
	 * @return IFigure
	 */

	public IFigure acceptFigure(ClassElement c, MethodDeclaration md);

}
