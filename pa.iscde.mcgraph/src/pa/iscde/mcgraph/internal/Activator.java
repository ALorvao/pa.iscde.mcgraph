package pa.iscde.mcgraph.internal;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pa.iscde.mcgraph.model.MethodRep;
import pa.iscde.mcgraph.service.McGraphListener;
import pa.iscde.mcgraph.service.McGraphServices;
import pa.iscde.mcgraph.view.McGraphView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private JavaEditorServices editorService;
	private ProjectBrowserServices browserService;
	private McGraphServices services;
	public static Activator activator;
	private Set<McGraphListener> mcGraphListeners;

	
	//Activator
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		ServiceReference<JavaEditorServices> eref = context.getServiceReference(JavaEditorServices.class);
		editorService = context.getService(eref);
		ServiceReference<ProjectBrowserServices> pref = context.getServiceReference(ProjectBrowserServices.class);
		browserService = context.getService(pref);
		mcGraphListeners = new HashSet<McGraphListener>();
		activator = this;
	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	


	//McGraphListeners
	
	public void addListener(McGraphListener l) {
		Assert.isNotNull(l);
		mcGraphListeners.add(l);
	}

	public void removeListener(McGraphListener l) {
		Assert.isNotNull(l);
		mcGraphListeners.remove(l);
	}
	
	void notityDoubleClik(MethodRep rep) {
		
		for(McGraphListener l : mcGraphListeners)
			l.doubleClick(rep);
	}

	void notifySelectionChanged(MethodRep rep) {
		for(McGraphListener l : mcGraphListeners)
			l.selectionChanged(rep);
	}
	
	
	//Gets

	public JavaEditorServices getEditorService() {
		return editorService;
	}
	
	public ProjectBrowserServices getBrowserService() {
		return browserService;
	}
	
	static BundleContext getContext() {
		return context;
	}
	
	public static Activator getActivator() {
		return activator;
	}

	public McGraphServices getMcGraphService() {
		return services;
	}

	public McGraphServices startService(McGraphView instance) {
		services = new McGraphServicesImpl(McGraphView.getInstance());
		return services;
	}
}
