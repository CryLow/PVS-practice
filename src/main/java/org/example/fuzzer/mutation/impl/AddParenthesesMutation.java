package org.example.fuzzer.mutation.impl;

import org.example.fuzzer.model.PlannedMutation;
import org.example.fuzzer.mutation.Mutation;
import org.example.fuzzer.util.AstUtils;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;

import java.util.List;

public class AddParenthesesMutation implements Mutation {

    @Override
    public String id() { return "AddParentheses"; }

    @Override
    public List<PlannedMutation> generate(CtElement root) {
        if (AstUtils.binaryOperators(root).isEmpty()) return List.of();
        return List.of(
                new PlannedMutation("depth1", r -> wrapAll(r, 1)),
                new PlannedMutation("depth2", r -> wrapAll(r, 2))
        );
    }

    private void wrapAll(CtElement root, int depth) {
        List<CtBinaryOperator<?>> ops = AstUtils.binaryOperators(root);
        for (CtBinaryOperator<?> op : ops) {
            String left = op.getLeftHandOperand().toString();
            String right = op.getRightHandOperand().toString();
            String operator = AstUtils.javaOperator(op.getKind());
            for (int i = 0; i < depth; i++) {
                left = "(" + left + ")";
                right = "(" + right + ")";
            }
            AstUtils.replaceBinaryOperatorWithSnippet(op, left + " " + operator + " " + right);
        }
    }
}