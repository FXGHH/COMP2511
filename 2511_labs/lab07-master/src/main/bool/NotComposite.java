package bool;

import java.util.ArrayList;
import java.util.List;

public class NotComposite implements BooleanNode{

    public ArrayList<BooleanNode> children = new ArrayList<BooleanNode>();
    public boolean result;

    public NotComposite() {
        super();
    }

    @Override
    public boolean evaluate() {
        return !children.get(0).evaluate();
    }

    @Override
    public String getString() {
        return "(NOT " + children.get(0).getString() + ')';
    }

    public void add(BooleanNode node) {
        children.add(node);
    }
    
}
