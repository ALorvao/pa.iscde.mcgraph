package pa.iscde.mcgraph.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import pa.iscde.mcgraph.view.McGraphView;

public class PidescoLayoutTool implements pt.iscte.pidesco.extensibility.PidescoTool {

	private HashMap<TableItem, String> tableitems;

	public PidescoLayoutTool() {
		// TODO Auto-generated constructor stub
		tool = this;
	}

	@Override
	public void run(boolean activate) {
		McGraphView view = McGraphView.getInstance();
		Display display = Display.getCurrent();
		tableitems = new HashMap<>();
		Set<String> layouts = view.getLayouts();
		Shell shell = new Shell(display);
		Table table = new Table(shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				view.resetLayouts();
				for (TableItem item : tableitems.keySet()) {
					if (item.getChecked()) {
						String layoutID = tableitems.get(item);
						view.activateLayout(layoutID);
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		shell.setSize(300, 50 * layouts.size());
		table.setSize(300, 50 * layouts.size());
		for (String name : layouts) {
			TableItem item = new TableItem(table, SWT.CHECK);
			item.setText(name);
			tableitems.put(item, name);
		}
		shell.open();
		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				view.resetLayouts();
			}
		});

	}

	public void setChecked(ArrayList<String> activated) {
		if (tableitems != null)
			for (String s : activated) {
				for (TableItem item : tableitems.keySet()) {
					if (tableitems.get(item).equals(s)) {
						item.setChecked(true);
					}
				}
			}
	}

	private static PidescoLayoutTool tool;
	protected static PidescoLayoutTool getInstance() {
		return tool;
	}

}
