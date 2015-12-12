package pa.iscde.mcgraph.internal;

import java.util.List;

import org.eclipse.core.runtime.Assert;

import pa.iscde.mcgraph.model.MethodRep;
import pa.iscde.mcgraph.service.McGraphListener;
import pa.iscde.mcgraph.service.McGraphServices;
import pa.iscde.mcgraph.view.McGraphView;

public class McGraphServicesImpl implements McGraphServices {


	public McGraphServicesImpl() {
		
	}

	@Override
	public List<MethodRep> getHighLighted() {
		return McGraphView.getInstance().getHighLighted();
	}
	
	@Override
	public void highLight(MethodRep rep) {
		McGraphView.getInstance().highLight(rep);		
	}

	@Override
	public void addListener(McGraphListener listener) {
		Activator.getActivator().addListener(listener);
	}

	@Override
	public void removeListener(McGraphListener listener) {
		Activator.getActivator().removeListener(listener);
	}

	

}
