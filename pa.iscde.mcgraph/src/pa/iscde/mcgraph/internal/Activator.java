package pa.iscde.mcgraph.internal;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import pa.iscde.mcgraph.model.MethodRep;
import pa.iscde.mcgraph.service.McGraphListener;
import pa.iscde.mcgraph.service.McGraphServices;
import pa.iscde.mcgraph.view.McGraphView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private JavaEditorServices editorService;
	private ProjectBrowserServices browserService;
	private McGraphServices services;
	private static Activator activator;
	private Set<McGraphListener> mcGraphListeners;
	private ServiceRegistration<McGraphServices> registerService;

	
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
		services = new McGraphServicesImpl();
		registerService = bundleContext.registerService(McGraphServices.class, services, null);
		activator = this;
		
	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		registerService.unregister();
		mcGraphListeners.clear();
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
	
	void notityDoubleClik(ClassElement c, MethodDeclaration dec) {
		for(McGraphListener l : mcGraphListeners)
			l.doubleClick(c, dec);
	}

	void notifySelectionChanged(ClassElement c, MethodDeclaration dec) {
		for(McGraphListener l : mcGraphListeners)
			l.selectionChanged(c, dec);
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

	
}
