package pa.iscde.mcgraph.extension;

import org.eclipse.swt.graphics.Image;

import extensionpoints.Item;

public class McGraphOutPutPreviewItem implements Item {
	private String name_item;
	private String text_Preview;
	private String text_Highlighed;
	private Image image;
	private Object special_data;

	@Override
	public void setItem(String name_item, String text_Preview, String text_Highlighted) {
		// TODO Auto-generated method stub
		this.name_item=name_item;
		this.text_Preview=text_Preview;
		this.text_Highlighed=text_Highlighted;
		
	}

	@Override
	public String getName() {
		return name_item;
	}

	@Override
	public String getPreviewText() {
		return text_Preview;
	}

	@Override
	public String getHighlightText() {
		return text_Highlighed;
	}

	@Override
	public void setImg(Image img) {
		this.image = img;
	}

	@Override
	public Image getImg() {
		return image;
	}

	@Override
	public void setSpecialData(Object special_data) {
		this.special_data = special_data;
	}

	@Override
	public Object getSpecialData() {
		return special_data;
	}

}
