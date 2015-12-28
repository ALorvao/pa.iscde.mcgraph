package pa.iscde.mcgraph.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import pa.iscde.speedtext.SpeedTextSortList;

public class McGraphOutPut_Sort implements SpeedTextSortList, Comparator<String> {

	public McGraphOutPut_Sort() {
	}

	@Override
	public Collection<String> sortList(Collection<String> list) {
		ArrayList<String> sortedList = (ArrayList<String>) list;
		sortedList.sort(this);
		return sortedList;
	}

	@Override
	public int compare(String o1, String o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		return o1.compareTo(o2);
	}

}
