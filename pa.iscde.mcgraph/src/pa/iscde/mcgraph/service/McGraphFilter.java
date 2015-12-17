package pa.iscde.mcgraph.service;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import pt.iscte.pidesco.projectbrowser.model.ClassElement;

public interface McGraphFilter {
	
	

	public boolean accept(ClassElement c, MethodDeclaration md);
	
	public boolean acceptDependencies(ClassElement c, MethodDeclaration md);
	
}
