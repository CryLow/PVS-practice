package org.example.fuzzer.mutation.impl;

import org.example.fuzzer.model.PlannedMutation;
import org.example.fuzzer.mutation.Mutation;
import org.example.fuzzer.util.AstUtils;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtWhile;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.CtScanner;

import java.util.ArrayList;
import java.util.List;

public class WrapInContextMutation implements Mutation {

    @Override
    public String id() {
        return "WrapInContext";
    }

    @Override
    public List<PlannedMutation> generate(CtElement root) {
        if (AstUtils.binaryOperators(root).isEmpty()) return List.of();
        return List.of(
                new PlannedMutation("if",      r -> wrapAll(r, "if")),
                new PlannedMutation("assert",  r -> wrapAll(r, "assert")),
                new PlannedMutation("ternary", r -> wrapAll(r, "ternary"))
        );
    }

    private void wrapAll(CtElement root, String contextType) {
        List<CtBinaryOperator<?>> ops = new ArrayList<>();
        root.accept(new CtScanner() {
            @Override
            public <T> void visitCtBinaryOperator(CtBinaryOperator<T> op) {
                ops.add(op);
                super.visitCtBinaryOperator(op);
            }
        });

        for (CtBinaryOperator<?> op : ops) {
            CtStatement stmt = AstUtils.nearestStatement(op);
            if (stmt == null) continue;
            if (stmt instanceof CtIf || stmt instanceof CtWhile || stmt instanceof CtFor) continue;

            String code = buildCode(op.toString(), contextType);
            CtStatement newStmt = op.getFactory().Code().createCodeSnippetStatement(code);

            for (CtComment c : stmt.getComments()) {
                newStmt.addComment(c.clone());
            }

            stmt.replace(newStmt);
        }
    }

    private String buildCode(String expr, String contextType) {
        return switch (contextType) {
            case "if"      -> "if (" + expr + ") { System.out.println(\"mut\") }";
            case "assert"  -> "assert " + expr;
            case "ternary" -> "boolean _tmp = (" + expr + ") ? true : false";
            default        -> throw new IllegalArgumentException("Unknown context: " + contextType);
        };
    }
}