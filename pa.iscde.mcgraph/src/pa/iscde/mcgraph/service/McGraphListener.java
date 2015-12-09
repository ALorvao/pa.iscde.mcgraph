package pa.iscde.mcgraph.service;

import pa.iscde.mcgraph.model.MethodRep;

/**
 * Represents a listener for events in the McGraph
 */

public interface McGraphListener {

	/**
	 * DoubleClick Event Listener.
	 */
	void doubleClick(MethodRep rep);

	/**
	 * SelectionChanged Event Listener
	 */

	void selectionChanged(MethodRep rep);

	/**
	 * Listener adapter that for each event does nothing.
	 */
	public class Adapter implements McGraphListener {

		@Override
		public void doubleClick(MethodRep rep) {
			// TODO Auto-generated method stub

		}

		@Override
		public void selectionChanged(MethodRep rep) {
			// TODO Auto-generated method stub

		}

	}

}
