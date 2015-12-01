package pa.iscde.mcgraph.internal;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import pa.iscde.mcgraph.model.MethodRep;
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
	private McGraphView view;

	public McGraph() {
		this.activator = Activator.getActivator();
		this.editorService = activator.getEditorService();
		this.browserService = activator.getBrowserService();
		this.root = browserService.getRootPackage();
		this.metodos = new ArrayList<>();
		this.view = McGraphView.getInstance();
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
					if (node.getExpression() != null) {
						ITypeBinding resolveTypeBinding = node.getExpression().resolveTypeBinding();
						for (MethodRep dep : metodos) {
							if (dep.getClassElement().getName().equals(resolveTypeBinding.getName() + ".java")) {
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
							if (resolveMethodBinding.toString()
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

}
