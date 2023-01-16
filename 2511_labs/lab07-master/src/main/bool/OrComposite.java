package bool;

import java.util.ArrayList;

public class OrComposite implements BooleanNode{

    ArrayList<BooleanNode> children = new ArrayList<BooleanNode>();
    public boolean result;

    public OrComposite() {
        super();
    }

    @Override
    public boolean evaluate() {
        return children.get(0).evaluate() || children.get(1).evaluate();
    }

    @Override
    public String getString() {
        return "(OR " + children.get(0).getString() + ' ' + children.get(1).getString() + ')';
    }

    public void add(BooleanNode node) {
        children.add(node);
    }
    
}
