
package pa.iscde.mcgraph.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

import pa.iscde.mcgraph.internal.McGraph;
import pa.iscde.mcgraph.model.MethodRep;
import pa.iscde.mcgraph.service.McGraphFilter;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;

public class McGraphView implements PidescoView {

	private static McGraphView instance;

	private McGraph mcGraph;
	private GraphViewer viewer;

	private HashMap<String, McGraphFilter> filters;
	private ArrayList<String> activated;

	public McGraphView() {
		this.instance = this;
		mcGraph = new McGraph();
		filters = mcGraph.getFilters();
		activated = new ArrayList<>();
	}

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		viewer = new GraphViewer(viewArea, SWT.BORDER);
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		viewer.setContentProvider(new ZestNodeContentProvider());
		viewer.setLabelProvider(new ZestLabelProvider());
		viewer.setInput(mcGraph.getMetodos());

		LayoutAlgorithm layout = new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		viewer.setLayoutAlgorithm(layout, true);
		viewer.applyLayout();

		addDoubleClickListener();
		addSelectionChangedListener();
		addEditorListeners();

	}

	private void addEditorListeners() {
		JavaEditorListener editorListener = new JavaEditorListener.Adapter() {

			@Override
			public void fileOpened(File file) {
				unhighlightAll();
				for (Object obj : viewer.getNodeElements()) {
					if (obj instanceof MethodRep) {
						MethodRep rep = (MethodRep) obj;
						if (rep.getClassElement().getFile().equals(file)) {
							GraphItem graphItem = viewer.findGraphItem(rep);
							graphItem.highlight();
						}
					}
				}
			}

			@Override
			public void fileSaved(File file) {
				unhighlightAll();
				mcGraph.refresh();
				viewer.refresh();
				viewer.applyLayout();
			}

			@Override
			public void selectionChanged(File file, String text, int offset, int length) {

				unhighlightAll();
				for (MethodRep rep : mcGraph.getMetodos()) {
					if (file.equals(rep.getClassElement().getFile())
							&& text.equals(rep.getMethodDeclaration().getName().toString())) {
						GraphItem graphItem = viewer.findGraphItem(rep);
						graphItem.highlight();
					}
				}
			}

		};
		mcGraph.getEditorService().addListener(editorListener);
	}

	private void addDoubleClickListener() {

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {

				ISelection selection = event.getSelection();
				if (!selection.isEmpty()) {
					viewer.setSelection(selection);
					if (viewer.getStructuredSelection().getFirstElement() instanceof MethodRep) {
						MethodRep rep = (MethodRep) viewer.getStructuredSelection().getFirstElement();
						mcGraph.getEditorService().openFile(rep.getClassElement().getFile());
						mcGraph.notifyDoubleClick(rep.getClassElement(), rep.getMethodDeclaration());
					}
				}
				viewer.applyLayout();

			}
		});
	}

	private void addSelectionChangedListener() {

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (!selection.isEmpty()) {
					viewer.setSelection(selection);
					if (viewer.getStructuredSelection().getFirstElement() instanceof MethodRep) {
						MethodRep rep = (MethodRep) viewer.getStructuredSelection().getFirstElement();
						mcGraph.notifySelectionChanged(rep.getClassElement(), rep.getMethodDeclaration());
					}
				}
			}
		});
	}

	public void unhighlightAll() {
		if (viewer != null)
			for (Object obj : viewer.getNodeElements()) {
				GraphItem graphItem = viewer.findGraphItem(obj);
				graphItem.unhighlight();
			}
	}

	public Map<MethodDeclaration, ClassElement> getHighLighted() {
		Map<MethodDeclaration, ClassElement> l = new HashMap<MethodDeclaration, ClassElement>();
		for (Object obj : viewer.getNodeElements()) {
			if (obj instanceof MethodRep) {
				MethodRep rep = (MethodRep) obj;
				GraphItem graphItem = viewer.findGraphItem(rep);
				if (graphItem instanceof GraphNode) {
					GraphNode node = (GraphNode) graphItem;
					if (node.isSelected())
						l.put(rep.getMethodDeclaration(), rep.getClassElement());
				}
			}
		}
		return l;
	}

	public static McGraphView getInstance() {
		return instance;
	}

	public Set<String> getFilters() {
		return filters.keySet();
	}

	public void resetFilters() {
		activated.clear();
		if (viewer != null)
			for (Object obj : viewer.getNodeElements()) {
				GraphItem graphItem = viewer.findGraphItem(obj);
				graphItem.setVisible(true);
			}
	}

	public void activateFilter(String filterID) {
		activated.add(filterID);
		applyFilters();
	}

	public void deactivateFilter(String filterID) {
		activated.remove(filterID);
		applyFilters();
	}

	private void applyFilters() {
		for (Object obj : viewer.getNodeElements()) {
			if (obj instanceof MethodRep) {
				MethodRep rep = (MethodRep) obj;
				if (!allFiltersAccepted(rep)) {
					GraphItem graphItem = viewer.findGraphItem(rep);
					graphItem.setVisible(false);
				}
				ArrayList<MethodRep> dependencies = rep.getDependencies();
				for (MethodRep dep : dependencies) {
					if (allFiltersAccepted(dep) && !hasDependencieAccepted(dep)) {
						if (!isDependencieAccepted(dep)) {
							GraphItem gi = viewer.findGraphItem(dep);
							gi.setVisible(false);
						}
					}
				}

			}
		}
		viewer.applyLayout();
	}

	private boolean isDependencieAccepted(MethodRep dep) {
		for (String s : activated) {
			McGraphFilter filter = filters.get(s);
			if (!filter.acceptDependencies(dep.getClassElement(), dep.getMethodDeclaration())) {
				return false;
			}
		}
		return true;
	}

	private boolean hasDependencieAccepted(MethodRep rep) {
		for (String s : activated) {
			McGraphFilter filter = filters.get(s);
			for (MethodRep dep : rep.getDependencies()) {
				if (filter.acceptDependencies(dep.getClassElement(), dep.getMethodDeclaration())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean allFiltersAccepted(MethodRep rep) {
		for (String s : activated) {
			McGraphFilter filter = filters.get(s);
			if (!filter.accept(rep.getClassElement(), rep.getMethodDeclaration())) {
				return false;
			}
		}
		return true;
	}

	
	//Falta testar
	public void highLight(ClassElement c, MethodDeclaration dec) {
		for (Object obj : viewer.getNodeElements()) {
			if (obj instanceof MethodRep) {
				MethodRep rep = (MethodRep) obj;
				if (rep.getClassElement().equals(c)) {
					MethodDeclaration med = rep.getMethodDeclaration();
					if (med.getBody().toString().equals(dec.getBody().toString()) && med.getFlags() == dec.getFlags()) {
						GraphItem graphItem = viewer.findGraphItem(rep);
						graphItem.highlight();
					}
				}
			}
		}
		
	}

}
