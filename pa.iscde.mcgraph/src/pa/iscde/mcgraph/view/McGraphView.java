
package pa.iscde.mcgraph.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import pa.iscde.mcgraph.internal.McGraph;
import pa.iscde.mcgraph.model.MethodRep;
import pa.iscde.mcgraph.service.McGraphFilter;
import pa.iscde.mcgraph.service.McGraphLayout;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;

public class McGraphView implements PidescoView {

	private static McGraphView instance;

	private McGraph mcGraph;
	private GraphViewer viewer;

	private HashMap<String, McGraphFilter> filters;

	private HashMap<String, McGraphLayout> layouts;
	private ArrayList<String> activated;
	private ArrayList<String> activatedLayouts;

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
				if (graphItem != null)
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
				if (graphItem != null)
					graphItem.setVisible(true);
			}
	}

	public void activateFilter(String filterID) {
		if (filters.keySet().contains(filterID))
			activated.add(filterID);
		applyFilters();
	}

	public void deactivateFilter(String filterID) {
		if (filters.keySet().contains(filterID))
			activated.remove(filterID);
		applyFilters();
	}

	private void applyFilters() {
		mcGraph.setToolChecked(activated);
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

	public void highLight(ClassElement c, MethodDeclaration dec) {
		System.out.println("Chegou");
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

	public void applyLayout(int layout) {
		switch (layout) {
		case 0:
			viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			break;
		case 1:
			viewer.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			break;
		case 2:
			viewer.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			break;
		case 3:
			viewer.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
			break;
		}
		viewer.applyLayout();
	}

	public Map<MethodDeclaration, ClassElement> getMethodsWithText(String text_Search) {
		HashMap<MethodDeclaration, ClassElement> map = new HashMap<>();
		if (viewer != null)
			for (Object obj : viewer.getNodeElements()) {
				if (obj instanceof MethodRep) {
					MethodRep rep = (MethodRep) obj;
					if (rep.toString().contains(text_Search))
						map.put(rep.getMethodDeclaration(), rep.getClassElement());
				}
			}
		return map;
	}

	public Map<MethodDeclaration, ClassElement> getDependencies(MethodDeclaration dec, ClassElement c) {
		HashMap<MethodDeclaration, ClassElement> map = new HashMap<>();
		for (Object obj : viewer.getNodeElements()) {
			if (obj instanceof MethodRep) {
				MethodRep rep = (MethodRep) obj;
				if (rep.getClassElement().equals(c)) {
					MethodDeclaration med = rep.getMethodDeclaration();
					if (med.getBody().toString().equals(dec.getBody().toString()) && med.getFlags() == dec.getFlags()) {
						for (MethodRep dep : rep.getDependencies()) {
							map.put(dep.getMethodDeclaration(), dep.getClassElement());
						}
					}
				}
			}
		}
		return map;
	}

	public Set<String> getLayouts() {
		return layouts.keySet();
	}

	public void activateLayout(String layoutID) {
		if (layouts.keySet().contains(layoutID))
			activatedLayouts.add(layoutID);
		applyLayout();
	}

	public void deactivateLayout(String layoutID) {
		if (layouts.keySet().contains(layoutID))
			activatedLayouts.remove(layoutID);
		applyLayout();
	}

	public void applyLayout() {
		mcGraph.setLayoutToolChecked(activatedLayouts);
		for (Object obj : viewer.getNodeElements()) {
			if (obj instanceof MethodRep) {
				MethodRep rep = (MethodRep) obj;
				if (isLayoutChangeable(rep)) {
					GraphNode gn = (GraphNode) viewer.findGraphItem(rep);
					gn.setBackgroundColor(getBackgroundColor(rep));
					gn.setForegroundColor(getForegroundColor(rep));
				}
			}
		}
		viewer.applyLayout();
	}

	private Color getForegroundColor(MethodRep rep) {
		// TODO Auto-generated method stub
		Color c = null;
		for (String s : activatedLayouts) {
			McGraphLayout layout = layouts.get(s);
			return layout.getForegroundColor(rep.getClassElement(), rep.getMethodDeclaration());
		}
		return c;
	}

	private Color getBackgroundColor(MethodRep rep) {
		Color c = null;
		for (String s : activatedLayouts) {
			McGraphLayout layout = layouts.get(s);
			return layout.getBackgroundColor(rep.getClassElement(), rep.getMethodDeclaration());
		}
		return c;
	}

	private boolean isLayoutChangeable(MethodRep rep) {
		for (String s : activatedLayouts) {
			McGraphLayout layout = layouts.get(s);
			if (layout.isChangeableNode(rep.getClassElement(), rep.getMethodDeclaration())) {
				return true;
			}
		}
		return false;
	}

	public void resetLayouts() {
		activatedLayouts.clear();
		if (viewer != null)
			for (Object obj : viewer.getNodeElements()) {
				GraphNode graphNode = (GraphNode) viewer.findGraphItem(obj);
				if (graphNode != null) {
					graphNode.setBackgroundColor(new Color(null, 255, 255, 255));
					graphNode.setForegroundColor(new Color(null, 0, 0, 0));
				}
			}
	}

}
