package pa.iscde.mcgraph.internal;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import pa.iscde.mcgraph.service.McGraphServices;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private McGraphServices services;
	private static Activator activator;

	// Activator

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.
	 * BundleContext)
	 */
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		ServiceReference<McGraphServices> eref = context.getServiceReference(McGraphServices.class);
		services = context.getService(eref);
		activator = this;
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
	}
	
	
	public McGraphServices getServices() {
		return services;
	}
	
	
	public static Activator getActivator() {
		return activator;
	}
	
	public static Bundle getBundle(){
		return Platform.getBundle("pa.iscde.mcgraph.61182");
	}



}
