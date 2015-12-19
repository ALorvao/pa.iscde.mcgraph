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
		
		public Map<MethodDeclaration, ClassElement> getHighLighted();
		
		public void highLight(MethodRep rep);

		public void addListener(McGraphListener listener);
		
		public void removeListener(McGraphListener listener);
		
		public void activateFilter(String filterId);

		public void deactivateFilter(String filterId);
	
}
