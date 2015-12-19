package pa.iscde.mcgraph.internal;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import pa.iscde.mcgraph.model.MethodRep;
import pa.iscde.mcgraph.service.McGraphListener;
import pa.iscde.mcgraph.service.McGraphServices;
import pa.iscde.mcgraph.view.McGraphView;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;

public class McGraphServicesImpl implements McGraphServices {


	public McGraphServicesImpl() {
		
	}

	@Override
	public Map<MethodDeclaration, ClassElement> getHighLighted() {
		return McGraphView.getInstance().getHighLighted();
	}
	
	@Override
	public void highLight(ClassElement c, MethodDeclaration dec) {
		McGraphView.getInstance().highLight(c, dec);
	}
	

	@Override
	public void addListener(McGraphListener listener) {
		Activator.getActivator().addListener(listener);
	}

	@Override
	public void removeListener(McGraphListener listener) {
		Activator.getActivator().removeListener(listener);
	}

	@Override
	public void activateFilter(String filterId) {
		McGraphView.getInstance().activateFilter(filterId);
	}

	@Override
	public void deactivateFilter(String filterId) {
		McGraphView.getInstance().deactivateFilter(filterId);
	}



	

}
