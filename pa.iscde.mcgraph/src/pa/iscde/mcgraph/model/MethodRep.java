package pa.iscde.mcgraph.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import pt.iscte.pidesco.projectbrowser.model.ClassElement;

public class MethodRep {

	private ClassElement classElement;
	private MethodDeclaration methodDeclaration;
	private ArrayList<MethodRep> dependencies;

	public MethodRep(ClassElement classElement, MethodDeclaration methodDeclaration) {
		this.classElement = classElement;
		this.methodDeclaration = methodDeclaration;
		dependencies = new ArrayList<>();
	}

	public void addDependencie(MethodRep rep) {
		dependencies.add(rep);
	}

	public boolean checkDependencie(MethodRep rep) {
		for (MethodRep r : dependencies)
			if (r.equals(rep))
				return true;
		return false;
	}

	public ClassElement getClassElement() {
		return classElement;
	}

	public MethodDeclaration getMethodDeclaration() {
		return methodDeclaration;
	}

	public ArrayList<MethodRep> getDependencies() {
		return dependencies;
	}

	@Override
	public String toString() {
		String[] s = classElement.getName().split("\\.");
		String method = s[0] + "." + methodDeclaration.getName() + "(";
		List parametros = methodDeclaration.parameters();
		for (int i = 0; i < parametros.size(); i++) {
			if (parametros.get(i) instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) parametros.get(i);
				System.out.println(svd);
				System.out.println(svd.getType());
				if (i != 0) {
					method += ", ";
				}
				method += svd.getType().toString();

			}
		}

		return method += ")";
	}

}
