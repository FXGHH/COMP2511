package bool;

import java.util.ArrayList;

public class AndComposite implements BooleanNode{

    public ArrayList<BooleanNode> children = new ArrayList<BooleanNode>();
    public boolean result;

    

    public AndComposite() {
        super();
    }

    @Override
    public boolean evaluate() {
        // result && ()
        return children.get(0).evaluate() && children.get(1).evaluate();
    }

    @Override
    public String getString() {
        return "(AND " + children.get(0).getString() + ' ' + children.get(1).getString() + ')';
    }

    public void add(BooleanNode node) {
        children.add(node);
    }
    
}
