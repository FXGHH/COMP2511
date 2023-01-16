package unsw.calculator.model.tree.compound;

import unsw.calculator.model.tree.BinaryOperatorNode;
import unsw.calculator.model.tree.TreeNode;

public class ModNode extends BinaryOperatorNode {

    public ModNode(TreeNode left, TreeNode right) {
        super(left, right);
    }

    public String getLabel(){
        return "%";
    }

    // Apply this operator (%) to the given operands
    public int compute(int a, int b){
        return a % b;
    }
}
