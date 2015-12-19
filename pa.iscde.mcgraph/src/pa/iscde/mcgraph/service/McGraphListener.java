package pa.iscde.mcgraph.service;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import pa.iscde.mcgraph.model.MethodRep;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;

/**
 * Represents a listener for events in the McGraph
 */

public interface McGraphListener {

	/**
	 * DoubleClick Event Listener
	 */
	void doubleClick(ClassElement c, MethodDeclaration dec);

	/**
	 * SelectionChanged Event Listener
	 * 
	 */

	void selectionChanged(ClassElement c, MethodDeclaration dec);

	/**
	 * Listener adapter that for each event
	 * The original implementation does nothing.
	 */
	public class Adapter implements McGraphListener {

		@Override
		public void doubleClick(ClassElement c, MethodDeclaration dec) {
			// TODO Auto-generated method stub

		}

		@Override
		public void selectionChanged(ClassElement c, MethodDeclaration dec) {
			// TODO Auto-generated method stub

		}

	}

}
