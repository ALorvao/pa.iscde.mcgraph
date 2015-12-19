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

public class PidescoTool implements pt.iscte.pidesco.extensibility.PidescoTool {

	private static PidescoTool tool;
	private HashMap<TableItem, String> tableitems;

	public PidescoTool() {
		tool = this;
	}

	@Override
	public void run(boolean activate) {
		McGraphView view = McGraphView.getInstance();
		Set<String> filters = view.getFilters();
		tableitems = new HashMap<>();
		Display display = Display.getCurrent();
		System.out.println(display.getShells().length);
		Shell shell = new Shell(display);

		Table table = new Table(shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				view.resetFilters();
				for (TableItem item : tableitems.keySet()) {
					if (item.getChecked()) {
						String filterID = tableitems.get(item);
						System.out.println("Vou activar : " + filterID);
						view.activateFilter(filterID);
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		for (String name : filters) {

			TableItem item = new TableItem(table, SWT.CHECK);
			item.setText(name);
			if (name.equals("NoFilter")) {
				item.setChecked(true);
				item.setGrayed(true);
			}
			tableitems.put(item, name);
		}
		table.setSize(200, 50 * filters.size());
		shell.setSize(200, 50 * filters.size());

		shell.open();
		shell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				view.resetFilters();
			}
		});
	}

	public void setChecked(ArrayList<String> activated) {
		if(tableitems!=null)
		for (String s : activated) {
			for (TableItem item : tableitems.keySet()) {
				if(tableitems.get(item).equals(s)){
					item.setChecked(true);
				}
			}
		}
	}

	protected static PidescoTool getInstance() {
		return tool;
	}

}
