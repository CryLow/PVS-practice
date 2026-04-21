package org.example.fuzzer.model;

import spoon.reflect.declaration.CtElement;

public record PlannedMutation(
        String fileSuffix,
        MutationAction action
) {
    @FunctionalInterface
    public interface MutationAction {
        void apply(CtElement root);
    }
}