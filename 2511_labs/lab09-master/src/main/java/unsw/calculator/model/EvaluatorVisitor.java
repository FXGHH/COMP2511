package unsw.calculator.model;

import unsw.calculator.model.tree.BinaryOperatorNode;
import unsw.calculator.model.tree.NumericNode;

public class EvaluatorVisitor implements Visitor{
    private BinaryOperatorNode opNode = null;
    private NumericNode nuNode = null;
    
    @Override
    public void visitBinaryOperatorNode(BinaryOperatorNode node) {
        this.opNode = node;
    }

    @Override
    public void visitNumericNode(NumericNode node) {
        this.nuNode = node;
    } 

    public int getValue() {
        if (nuNode != null) {
            return calculateNode(nuNode);
        }
        EvaluatorVisitor LeftevaluatorVisitor = new EvaluatorVisitor();
        EvaluatorVisitor RightevaluatorVisitor = new EvaluatorVisitor();
        opNode.acceptLeft(LeftevaluatorVisitor);
        opNode.acceptRight(RightevaluatorVisitor);
        if (opNode != null) {
            return opNode.compute(LeftevaluatorVisitor.getValue(), RightevaluatorVisitor.getValue());
        }
        return nuNode.getValue();
    }

    public int calculateNode(NumericNode node) {
        return node.getValue();
    }
}