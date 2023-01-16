package unsw.calculator.model.tree;

import unsw.calculator.model.Visitor;

/*
 * Abstract superclass of both tree elements (simple, binary)
 */
public interface TreeNode {

    public String getLabel();

    public void accept(Visitor visitor);
}