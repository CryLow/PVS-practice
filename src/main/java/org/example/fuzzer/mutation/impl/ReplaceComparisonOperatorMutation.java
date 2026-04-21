package org.example.fuzzer.mutation.impl;

import org.example.fuzzer.model.PlannedMutation;
import org.example.fuzzer.mutation.Mutation;
import org.example.fuzzer.util.AstUtils;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtElement;

import java.util.ArrayList;
import java.util.List;

public class ReplaceComparisonOperatorMutation implements Mutation {

    private static final List<BinaryOperatorKind> REPLACEMENTS = List.of(
            BinaryOperatorKind.NE,
            BinaryOperatorKind.LT,
            BinaryOperatorKind.GT,
            BinaryOperatorKind.LE,
            BinaryOperatorKind.GE
    );

    @Override
    public String id() {
        return "ReplaceOperator";
    }

    @Override
    public List<PlannedMutation> generate(CtElement root) {
        boolean hasEq = AstUtils.binaryOperators(root).stream()
                .anyMatch(op -> op.getKind() == BinaryOperatorKind.EQ);
        if (!hasEq) return List.of();

        List<PlannedMutation> result = new ArrayList<>();
        for (BinaryOperatorKind replacement : REPLACEMENTS) {
            String suffix = "eq_" + replacement.name().toLowerCase();
            result.add(new PlannedMutation(suffix, clonedRoot -> replaceAll(clonedRoot, replacement)));
        }
        return result;
    }

    private void replaceAll(CtElement root, BinaryOperatorKind replacement) {
        for (CtBinaryOperator<?> op : AstUtils.binaryOperators(root)) {
            if (op.getKind() == BinaryOperatorKind.EQ) {
                op.setKind(replacement);
            }
        }
    }
}