package org.example.fuzzer.mutation.impl;

import org.example.fuzzer.model.PlannedMutation;
import org.example.fuzzer.mutation.Mutation;
import org.example.fuzzer.util.AstUtils;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;

import java.util.List;

public class ComplexifyOperandMutation implements Mutation {

    @Override
    public String id() { return "ComplexifyOperand"; }

    @Override
    public List<PlannedMutation> generate(CtElement root) {
        if (AstUtils.binaryOperators(root).isEmpty()) return List.of();
        return List.of(
                new PlannedMutation("plus1", r -> applyAll(r, " + 1")),
                new PlannedMutation("mul2",  r -> applyAll(r, " * 2"))
        );
    }

    private void applyAll(CtElement root, String suffix) {
        List<CtBinaryOperator<?>> ops = AstUtils.binaryOperators(root);
        for (CtBinaryOperator<?> op : ops) {
            String left = "(" + op.getLeftHandOperand() + ")" + suffix;
            String right = "(" + op.getRightHandOperand() + ")" + suffix;
            String operator = AstUtils.javaOperator(op.getKind());
            AstUtils.replaceBinaryOperatorWithSnippet(op, left + " " + operator + " " + right);
        }
    }
}