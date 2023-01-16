package unsw.calculator.model;

import unsw.calculator.model.tree.BinaryOperatorNode;
import unsw.calculator.model.tree.NumericNode;

public class PostFixPrintVisitor implements Visitor{

    @Override
    public void visitBinaryOperatorNode(BinaryOperatorNode node) {

        if (node.getLeft() != null) {
            node.acceptLeft(new PostFixPrintVisitor());
        }

        if (node.getRight() != null) {
            node.acceptRight(new PostFixPrintVisitor());
        }

        System.out.print(node.getLabel() + " ");
    }

    @Override
    public void visitNumericNode(NumericNode node) {
        System.out.print(node.getLabel() + " ");
        
    }
}