package pa.iscde.mcgraph.service;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import pa.iscde.mcgraph.model.MethodRep;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;

/**
 * Services offered by the McGraph component.
 */

public interface McGraphServices {
	
	/**
	 * @return A map with the Methods(MethodDeclaration, ClassElement combination) that are HighLighted in the McGraph View
	 */
	public Map<MethodDeclaration, ClassElement> getHighLighted();

	/**
	 * HighLights a Method in the McGraph View
	 * 
	 * @param c ClassElement
	 * @param dec MethodDeclaration
	 */
	public void highLight(ClassElement c, MethodDeclaration dec);

	/**
	 * @param listener
	 * 		not_null
	 */
	public void addListener(McGraphListener listener);

	/**
	 * @param listener
	 * 		not_null
	 */
	public void removeListener(McGraphListener listener);

	/**
	 * 
	 * Activates a filter that already exists
	 * @param filterId
	 */
	public void activateFilter(String filterId);

	/**
	 * 
	 * Deactivates a filter that already exists
	 * @param filterId
	 */
	public void deactivateFilter(String filterId);

}
