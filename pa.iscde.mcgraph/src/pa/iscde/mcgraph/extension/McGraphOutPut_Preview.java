package pa.iscde.mcgraph.extension;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import extensionpoints.Item;
import extensionpoints.OutputPreview;
import pa.iscde.mcgraph.internal.Activator;
import pa.iscde.mcgraph.internal.McGraph;
import pa.iscde.mcgraph.model.MethodRep;
import pa.iscde.mcgraph.view.McGraphView;

public class McGraphOutPut_Preview implements OutputPreview {

	MethodRep rep;

	@Override
	public void search(String text_Search, String text_SearchInCombo, String specificText_ComboSearchIn,
			String text_AdvancedCombo, ArrayList<String> buttonsSelected_AdvancedCombo) {
		rep = McGraphView.getInstance().getMethod(text_Search);
		System.out.println(text_Search);
	}

	@Override
	public Collection<Item> getParents() {
		LinkedList<Item> l = new LinkedList<>();
		if (rep != null) {
			System.out.println(rep);
			McGraphOutPutPreviewItem item = new McGraphOutPutPreviewItem();
			item.setItem("Dependencias", rep.toString(), rep.toString());
			URL fullPathString = FileLocator.find(Activator.getBundle(),
					new Path("images/mcg.png"), null);
			ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
			Image image = imageDesc.createImage();
			item.setImg(image);
			item.setSpecialData(rep);
			l.add(item);
		}
		return l;
	}

	@Override
	public Collection<Item> getChildren(String parent) {
		LinkedList<Item> l = new LinkedList<>();
		if (rep != null) {
			for (MethodRep dep : rep.getDependencies()) {
				McGraphOutPutPreviewItem item = new McGraphOutPutPreviewItem();
				item.setItem(dep.toString(), rep.toString(), rep.toString());
				item.setSpecialData(dep);
				l.add(item);
			}
		}
		return l;
	}

	@Override
	public void doubleClick(Item e) {
		System.out.println(e);
	}

}
