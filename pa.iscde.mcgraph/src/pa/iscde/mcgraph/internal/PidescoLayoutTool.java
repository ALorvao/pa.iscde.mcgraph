package pa.iscde.mcgraph.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import pa.iscde.mcgraph.view.McGraphView;

public class PidescoLayoutTool implements pt.iscte.pidesco.extensibility.PidescoTool {

	public PidescoLayoutTool() {

	}

	private static Map<TableItem, Integer> mapping = new HashMap<TableItem, Integer>();

	@Override
	public void run(boolean activate) {
		McGraphView view = McGraphView.getInstance();
		Display display = Display.getCurrent();
		System.out.println("Layout: " + display.getShells().length);
		Shell shell = new Shell(display);
		Table table = new Table(shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for (TableItem item : mapping.keySet()) {
					if (item.getChecked()) {
						view.applyLayout(mapping.get(item));
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		addItems(table);
		shell.setSize(200, 200);
		table.setSize(200, 200);
		shell.open();

	}

	private void addItems(Table table) {
		// TODO Auto-generated method stub
		TableItem itemLayout0 = new TableItem(table, SWT.CHECK);
		itemLayout0.setText("Tree Layout");
		mapping.put(itemLayout0, 0);
		TableItem itemLayout1 = new TableItem(table, SWT.CHECK);
		itemLayout1.setText("Spring Layout");
		mapping.put(itemLayout1, 1);
		TableItem itemLayout2 = new TableItem(table, SWT.CHECK);
		itemLayout2.setText("Radial Layout");
		mapping.put(itemLayout2, 2);
		TableItem itemLayout3 = new TableItem(table, SWT.CHECK);
		itemLayout3.setText("Grid Layout");
		mapping.put(itemLayout3, 3);

	}
}
