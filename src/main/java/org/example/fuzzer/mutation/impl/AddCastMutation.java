package org.example.fuzzer.mutation.impl;

import org.example.fuzzer.model.PlannedMutation;
import org.example.fuzzer.mutation.Mutation;
import org.example.fuzzer.util.AstUtils;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;

import java.util.List;

public class AddCastMutation implements Mutation {

    @Override
    public String id() { return "AddCast"; }

    @Override
    public List<PlannedMutation> generate(CtElement root) {
        if (AstUtils.binaryOperators(root).isEmpty()) return List.of();
        return List.of(
                new PlannedMutation("cast_int",  r -> applyAll(r, "int")),
                new PlannedMutation("cast_long", r -> applyAll(r, "long"))
        );
    }

    private void applyAll(CtElement root, String castType) {
        List<CtBinaryOperator<?>> ops = AstUtils.binaryOperators(root);
        for (CtBinaryOperator<?> op : ops) {
            String left = "((" + castType + ") (" + op.getLeftHandOperand() + "))";
            String right = "((" + castType + ") (" + op.getRightHandOperand() + "))";
            String operator = AstUtils.javaOperator(op.getKind());
            AstUtils.replaceBinaryOperatorWithSnippet(op, left + " " + operator + " " + right);
        }
    }
}