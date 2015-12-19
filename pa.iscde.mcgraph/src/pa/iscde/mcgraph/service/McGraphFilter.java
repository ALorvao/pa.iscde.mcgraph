package pa.iscde.mcgraph.service;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import pt.iscte.pidesco.projectbrowser.model.ClassElement;

/**
 * McGraphFilter Interface
 * Used for McGraphFilter Extension Point
 *
 */
public interface McGraphFilter {
	
	
	
	/**
	 * Every single method that is represented in McGraph View goes through this function,
	 * If it returns false then the method will NOT be visible in McGraph View
	 * 
	 * @param c ClassElement 
	 * @param md MethodDeclaration
	 * @return boolean
	 */
	public boolean accept(ClassElement c, MethodDeclaration md);
	
	/**
	 * Every single method that is called by other enters this method.
	 * If it return false the method or in the method above will NOT be visible in McGraph View
	 * The method will be visible if it has any dependencies with a method what return true in this method
	 * Ps: Alguma Duvida Apitem
	 * 
	 * @param c ClassElement 
	 * @param md MethodDeclaration
	 * @return boolean
	 */
	public boolean acceptDependencies(ClassElement c, MethodDeclaration md);
	
}
