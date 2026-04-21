package org.example.fuzzer.mutation.impl;

import org.example.fuzzer.model.PlannedMutation;
import org.example.fuzzer.mutation.Mutation;
import org.example.fuzzer.util.AstUtils;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtScanner;

import java.util.List;

public class ReplaceVariableTypeMutation implements Mutation {

    @Override
    public String id() { return "ReplaceVarType"; }

    @Override
    public List<PlannedMutation> generate(CtElement root) {
        boolean hasInt = AstUtils.localVariables(root).stream()
                .anyMatch(v -> "int".equals(v.getType().getSimpleName()));
        if (!hasInt) return List.of();

        return List.of(
                new PlannedMutation("int_to_long",   r -> replaceAll(r, "long")),
                new PlannedMutation("int_to_float",  r -> replaceAll(r, "float")),
                new PlannedMutation("int_to_double", r -> replaceAll(r, "double")),
                new PlannedMutation("int_to_short",  r -> replaceAll(r, "short"))
        );
    }

    @SuppressWarnings({"rawtypes"})
    private void replaceAll(CtElement root, String replacementType) {
        root.accept(new CtScanner() {
            @Override
            public <T> void visitCtLocalVariable(CtLocalVariable<T> variable) {
                if ("int".equals(variable.getType().getSimpleName())) {
                    CtTypeReference<?> newType = switch (replacementType) {
                        case "long"   -> variable.getFactory().Type().longPrimitiveType();
                        case "float"  -> variable.getFactory().Type().floatPrimitiveType();
                        case "double" -> variable.getFactory().Type().doublePrimitiveType();
                        case "short"  -> variable.getFactory().Type().shortPrimitiveType();
                        default -> throw new IllegalStateException("Unknown type: " + replacementType);
                    };
                    ((CtLocalVariable) variable).setType(newType);
                }
                super.visitCtLocalVariable(variable);
            }
        });
    }
}