package clusterer;

import java.util.Comparator;

public class NodeIdComparator implements Comparator<PrintableNode> {


	@Override
	public int compare(PrintableNode o1, PrintableNode o2) {
		return Integer.compare(o1.getId(), o2.getId());
		//return 1;
	}

}
