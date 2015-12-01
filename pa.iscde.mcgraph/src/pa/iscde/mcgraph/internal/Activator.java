package pa.iscde.mcgraph.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private JavaEditorServices editorService;
	private ProjectBrowserServices browserService;
	public static Activator activator;

	static BundleContext getContext() {
		return context;
	}

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
		activator = this;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	public JavaEditorServices getEditorService() {
		return editorService;
	}
	
	public ProjectBrowserServices getBrowserService() {
		return browserService;
	}
	
	
	public static Activator getActivator() {
		return activator;
	}
}
