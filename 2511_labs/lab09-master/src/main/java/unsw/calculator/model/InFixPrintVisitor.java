package unsw.calculator.model;

import unsw.calculator.model.tree.BinaryOperatorNode;
import unsw.calculator.model.tree.NumericNode;

public class InFixPrintVisitor implements Visitor{

    @Override
    public void visitBinaryOperatorNode(BinaryOperatorNode node) {
        System.out.print("(");

        if (node.getLeft() != null) {
            node.acceptLeft(new InFixPrintVisitor());
        }

        System.out.print(" " + node.getLabel() + " ");

        if (node.getRight() != null) {
            node.acceptRight(new InFixPrintVisitor());
        }
        
        System.out.print(")");
    }

    @Override
    public void visitNumericNode(NumericNode node) {
        System.out.print(node.getLabel());
    }

}