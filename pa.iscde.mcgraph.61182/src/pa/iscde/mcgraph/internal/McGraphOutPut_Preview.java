package pa.iscde.mcgraph.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import extensionpoints.Item;
import extensionpoints.OutputPreview;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;

public class McGraphOutPut_Preview implements OutputPreview {

	Map<MethodDeclaration, ClassElement> methods;
	String text;

	@Override
	public void searchEvent(String text_Search, String text_SearchInCombo, String specificText_SearchInCombo,
			String text_SearchForCombo, Collection<String> buttonsSelected_SearchForCombo) {
		methods = Activator.getActivator().getServices().getMethodsWithText(text_Search);// McGraphView.getInstance().getMethod(text_Search);
		text = text_Search;
	}

	@Override
	public Collection<Item> getParents() {
		LinkedList<Item> l = new LinkedList<>();
		if (methods != null) {
			McGraphOutPutPreviewItem item = new McGraphOutPutPreviewItem();
			item.setItem("Dependencias", "", "");
			URL fullPathString = FileLocator.find(Activator.getBundle(), new Path("images/mcg.png"), null);
			ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
			Image image = imageDesc.createImage();
			item.setImg(image);
			item.setSpecialData(methods);
			l.add(item);
		}
		return l;
	}

	@Override
	public Collection<Item> getChildren(String parent) {
		LinkedList<Item> l = new LinkedList<>();
		if (methods != null) {
			for (MethodDeclaration rep : methods.keySet()) {
				Map<MethodDeclaration, ClassElement> dependencies = Activator.getActivator().getServices().getDependencies(rep,
						methods.get(rep));
				for (MethodDeclaration dep : dependencies.keySet()) {
					methods.put(dep, dependencies.get(dep));
					McGraphOutPutPreviewItem item = new McGraphOutPutPreviewItem();
					item.setItem(dep.toString(), rep.toString(), text);
					item.setSpecialData(dep);
					l.add(item);
				}
			}
		}
		return l;
	}

	@Override
	public void doubleClick(Item e) {
		if (e.getSpecialData() instanceof MethodDeclaration) {
			MethodDeclaration dec = (MethodDeclaration) e.getSpecialData();
			ClassElement c = methods.get(dec);
			Activator.getActivator().getServices().unhighlightAll();
			Activator.getActivator().getServices().highLight(c, dec);
		}
	}

}
