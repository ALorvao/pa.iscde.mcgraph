package pa.iscde.mcgraph.internal;

import pa.iscde.mcgraph.model.MethodRep;
import pa.iscde.mcgraph.service.McGraphListener;
import pa.iscde.mcgraph.service.McGraphServices;

public class Teste {
	
	public Teste() {

		McGraphServices service = Activator.getActivator().getMcGraphService();
		McGraphListener l = new McGraphListener() {
			
			@Override
			public void selectionChanged(MethodRep rep) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void doubleClick(MethodRep rep) {
				System.out.println("Oi");
				for(MethodRep oi: service.getHighLighted())
					System.out.println(oi);
			}
		};
		
		service.addListener(l);
		
	}
	
	
}
