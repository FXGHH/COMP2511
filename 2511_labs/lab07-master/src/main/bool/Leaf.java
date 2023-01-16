package bool;

public class Leaf implements BooleanNode{

    public boolean result;
    public Leaf(boolean result) {
        super();
        this.result = result;
    }

    @Override
    public boolean evaluate() {
        return result;
    }

    @Override
    public String getString() {
        return result ? "true" : "false";
    }
    
}
