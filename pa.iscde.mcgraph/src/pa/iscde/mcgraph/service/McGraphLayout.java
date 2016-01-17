package pa.iscde.mcgraph.service;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.swt.graphics.Color;

import pt.iscte.pidesco.projectbrowser.model.ClassElement;

/*
 * McGraphLayout Interface
 * Used for McGraphLayout Extension Point
 * 
 */
public interface McGraphLayout {

	/*
	 * Returns the background color for a specific ClassElement or
	 * MethodDeclaration. It is used to change a graphnode color.
	 * 
	 * @param c ClassElement
	 * 
	 * @param md MethodDeclaration
	 * 
	 * @return Color
	 */

	public Color getBackgroundColor(ClassElement c, MethodDeclaration md);

	/*
	 * Condition for the nodes. If it returns true then a node can be
	 * changed/customized.
	 * 
	 * @param c ClassElement
	 * 
	 * @param md MethodDeclaration
	 * 
	 * @return boolean
	 */

	public boolean isChangeableNode(ClassElement c, MethodDeclaration md);

	/*
	 * Returns the foreground color for a specific ClassElement or
	 * MethodDeclaration. It is used to change a graphnode font color which is
	 * useful for darker or brighter background colors.
	 * 
	 * @param c ClassElement
	 * 
	 * @param md MethodDeclaration
	 * 
	 * @return Color
	 */

	public Color getForegroundColor(ClassElement c, MethodDeclaration md);

}
