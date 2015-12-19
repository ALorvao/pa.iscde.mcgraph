package pa.iscde.mcgraph.internal;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import pa.iscde.mcgraph.model.MethodRep;
import pa.iscde.mcgraph.service.McGraphFilter;
import pa.iscde.mcgraph.service.McGraphServices;
import pa.iscde.mcgraph.view.McGraphView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.PackageElement.Visitor;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class McGraph {

	private Activator activator;
	private JavaEditorServices editorService;
	private ProjectBrowserServices browserService;
	private PackageElement root;
	private ArrayList<MethodRep> metodos;
	private McGraphServices mcGraphServices;

	public McGraph() {
		this.activator = Activator.getActivator();
		this.editorService = activator.getEditorService();
		this.browserService = activator.getBrowserService();
		this.root = browserService.getRootPackage();
		this.metodos = new ArrayList<>();
		this.mcGraphServices = activator.getMcGraphService();
		getContent();

	}

	private void getContent() {

		metodos.clear();

		root.traverse(new Visitor.Adapter() {
			@Override
			public void visitClass(ClassElement c) {
				editorService.parseFile(c.getFile(), new ASTVisitor() {

					@Override
					public boolean visit(MethodDeclaration node) {
						MethodRep rep = new MethodRep(c, node);
						metodos.add(rep);
						return true;
					}
				});
			}
		});

		for (MethodRep rep : metodos) {

			editorService.parseFile(rep.getClassElement().getFile(), new ASTVisitor() {

				@Override
				public boolean visit(MethodDeclaration node) {
					if (node.getName().toString().equals(rep.getMethodDeclaration().getName().toString()) && node
							.parameters().toString().equals(rep.getMethodDeclaration().parameters().toString())) {
						return true;
					} else
						return false;
				}

				@Override
				public boolean visit(ClassInstanceCreation node) {
					for (MethodRep dep : metodos) {
						if (dep.getClassElement().getName().equals(node.getType() + ".java")) {
							if (dep.getMethodDeclaration().getName().toString().equals(node.getType().toString())) {
								if (!rep.getDependencies().contains(dep)) {
									rep.addDependencie(dep);
								}
							}
						}

					}
					return true;
				}

				@Override
				public boolean visit(MethodInvocation node) {

					IMethodBinding resolveMethodBinding = node.resolveMethodBinding();
					if (resolveMethodBinding != null && node.getExpression() != null) {
						ITypeBinding resolveTypeBinding = node.getExpression().resolveTypeBinding();
						for (MethodRep dep : metodos) {
							if (resolveTypeBinding != null
									&& dep.getClassElement().getName().equals(resolveTypeBinding.getName() + ".java")) {
								if (resolveMethodBinding.toString()
										.equals(dep.getMethodDeclaration().resolveBinding().toString())) {
									if (!rep.getDependencies().contains(dep)) {
										rep.addDependencie(dep);
									}
								}
							}
						}
					} else
						for (MethodRep dep : metodos) {

							if (resolveMethodBinding != null && dep.getMethodDeclaration().resolveBinding() != null
									&& resolveMethodBinding.toString()
											.equals(dep.getMethodDeclaration().resolveBinding().toString())) {
								if (!rep.getDependencies().contains(dep)) {
									rep.addDependencie(dep);
								}
							}
						}

					return true;
				}
			});
		}

	}

	public ArrayList<MethodRep> getMetodos() {
		return metodos;
	}

	public JavaEditorServices getEditorService() {
		return editorService;
	}

	public ProjectBrowserServices getBrowserService() {
		return browserService;
	}

	public void refresh() {
		getContent();
	}

	public void notifyDoubleClick(ClassElement c, MethodDeclaration dec) {
		activator.notityDoubleClik(c, dec);
	}

	public void notifySelectionChanged(ClassElement c, MethodDeclaration dec) {
		activator.notifySelectionChanged(c, dec);
	}

	public HashMap<String, McGraphFilter> getFilters() {

		HashMap<String, McGraphFilter> filters = new HashMap<String, McGraphFilter>();
		filters.put("NoFilter", new McGraphFilter() {

			@Override
			public boolean acceptDependencies(ClassElement c, MethodDeclaration md) {
				return true;
			}

			@Override
			public boolean accept(ClassElement c, MethodDeclaration md) {
				return true;
			}
		});

		for (MethodRep rep : metodos) {
			filters.put(rep.getClassElement().getName().split("\\.")[0] + "DefaultFilter", new McGraphFilter() {

				@Override
				public boolean acceptDependencies(ClassElement c, MethodDeclaration md) {
					return true;
				}

				@Override
				public boolean accept(ClassElement c, MethodDeclaration md) {
					return c.equals(rep.getClassElement());
				}
			});
		}

		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pa.iscde.mcgraph.mcfilter");
		IExtension[] extensions = extensionPoint.getExtensions();
		for (IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for (IConfigurationElement c : confElements) {
				String s = c.getAttribute("name");
				System.out.println("Está ligado: " + s);
				try {
					Object o = c.createExecutableExtension("class");
					McGraphFilter filter = (McGraphFilter) o;
					filters.put(s, filter);
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return filters;
	}

	public void setToolChecked(ArrayList<String> activated) {
		PidescoTool.getInstance().setChecked(activated);
	}

}
