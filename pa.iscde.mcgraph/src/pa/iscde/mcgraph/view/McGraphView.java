package pa.iscde.mcgraph.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

import pa.iscde.mcgraph.internal.McGraph;
import pa.iscde.mcgraph.model.MethodRep;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;

public class McGraphView implements PidescoView {

	private static McGraphView instance;

	private McGraph mcGraph;
	private GraphViewer viewer;

	public McGraphView() {
		this.instance = this;
		mcGraph = new McGraph();
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
						mcGraph.notifyDoubleClick(rep);
					}
				}
				viewer.refresh();
				viewer.applyLayout();

				IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
				IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pa.iscde.mcgraph.mcfilter");
				IExtension[] extensions = extensionPoint.getExtensions();
				for (IExtension e : extensions) {
					IConfigurationElement[] confElements = e.getConfigurationElements();
					for (IConfigurationElement c : confElements) {
						String s = c.getAttribute("name");
						System.out.println("Est� ligado: " + s);
						try {
							Object o = c.createExecutableExtension("class");
							System.out.println("o");
						} catch (CoreException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
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
						mcGraph.notifySelectionChanged(rep);
					}
				}
				// unhighlightAll();

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

	public List<MethodRep> getHighLighted() {
		List<MethodRep> l = new ArrayList<MethodRep>();
		for (Object obj : viewer.getNodeElements()) {
			if (obj instanceof MethodRep) {
				MethodRep rep = (MethodRep) obj;
				GraphItem graphItem = viewer.findGraphItem(rep);
				if (graphItem instanceof GraphNode) {
					GraphNode node = (GraphNode) graphItem;
					if (node.isSelected())
						l.add(rep);
				}
			}
		}
		return l;
	}

	// Equals n�o vai ser suficiente
	public void highLight(MethodRep rep) {
		for (Object obj : viewer.getNodeElements()) {
			if (obj instanceof MethodRep) {
				MethodRep node = (MethodRep) obj;
				System.out.println("Cheguei");
				if (node.equals(rep)) {
					System.out.println(rep);
					GraphItem graphItem = viewer.findGraphItem(rep);
					graphItem.highlight();
				}
			}
		}
	}

	public static McGraphView getInstance() {
		return instance;
	}

}
