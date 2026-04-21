package org.example.fuzzer.mutation;

import org.example.fuzzer.model.PlannedMutation;
import spoon.reflect.declaration.CtElement;

import java.util.List;

public interface Mutation {
    String id();
    List<PlannedMutation> generate(CtElement root);
}