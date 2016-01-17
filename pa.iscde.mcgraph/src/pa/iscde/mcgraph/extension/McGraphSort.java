package pa.iscde.mcgraph.extension;

import pa.iscde.speedtext.SpeedTextSortList;

public class McGraphSort implements SpeedTextSortList {

	public McGraphSort() {
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Sort by length";
	}

	@Override
	public boolean compare(String a, String b) {
		// TODO Auto-generated method stub
		if (a.length() > b.length())
			return true;
		else
			return false;
	}

	public static void main(String[] args) {
		String a = "maior!";
		String b = "menor";
		McGraphSort test = new McGraphSort();
		System.out.println(test.getName());
		if (test.compare(a, b)) {
			System.out.println("Esta String é " + a);
		}
	}

}