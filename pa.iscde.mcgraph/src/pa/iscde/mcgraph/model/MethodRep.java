package pa.iscde.mcgraph.model;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodDeclaration;

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
	
	public void addDependencie(MethodRep rep){
		dependencies.add(rep);
	}
	
	public boolean checkDependencie(MethodRep rep){
		for(MethodRep r: dependencies)
			if(r.equals(rep))
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
		return s[0] + "." + methodDeclaration.getName() +"()";
	}

}
