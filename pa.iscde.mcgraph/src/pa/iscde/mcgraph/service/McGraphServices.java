package pa.iscde.mcgraph.service;

import java.util.List;

import pa.iscde.mcgraph.model.MethodRep;

/**
 * Services offered by the McGraph component.
 */

public interface McGraphServices {
		
		List<MethodRep> getHighLighted();
		
		void highLight(MethodRep rep);

		void addListener(McGraphListener listener);
		
		void removeListener(McGraphListener listener);
	
}
