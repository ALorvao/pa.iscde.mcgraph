package pa.iscde.mcgraph.view;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import pa.iscde.mcgraph.model.MethodRep;


public class ZestNodeContentProvider extends ArrayContentProvider  implements IGraphEntityContentProvider {

  @Override
  public Object[] getConnectedTo(Object entity) {
    if (entity instanceof MethodRep) {
      MethodRep node = (MethodRep) entity;
      return node.getDependencies().toArray();
    }
    throw new RuntimeException("Type not supported");
  }
} 