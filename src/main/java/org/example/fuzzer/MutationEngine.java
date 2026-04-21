package org.example.fuzzer;

import org.example.fuzzer.io.ModelLoader;
import org.example.fuzzer.io.MutantWriter;
import org.example.fuzzer.model.PlannedMutation;
import org.example.fuzzer.mutation.Mutation;
import org.example.fuzzer.mutation.impl.AddCastMutation;
import org.example.fuzzer.mutation.impl.AddParenthesesMutation;
import org.example.fuzzer.mutation.impl.ComplexifyOperandMutation;
import org.example.fuzzer.mutation.impl.ReplaceComparisonOperatorMutation;
import org.example.fuzzer.mutation.impl.ReplaceVariableTypeMutation;
import org.example.fuzzer.mutation.impl.WrapInContextMutation;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtCompilationUnit;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.PrettyPrinter;

import java.util.ArrayList;
import java.util.List;

public class MutationEngine {

    private final List<Mutation> mutations = List.of(
            new ReplaceComparisonOperatorMutation(),
            new ReplaceVariableTypeMutation(),
            new AddParenthesesMutation(),
            new ComplexifyOperandMutation(),
            new AddCastMutation(),
            new WrapInContextMutation()
    );

    public void run(FuzzerConfig config) throws Exception {

        CtModel probeModel = new ModelLoader().load(config.inputFile());
        CtType<?> probeType = probeModel.getAllTypes().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No type found in " + config.inputFile()));

        String baseName = probeType.getSimpleName();
        CtPackage rootPackage = probeModel.getRootPackage();

        List<MutationPlan> plans = new ArrayList<>();
        for (Mutation mutation : mutations) {
            for (PlannedMutation planned : mutation.generate(rootPackage)) {
                plans.add(new MutationPlan(mutation.id(), planned));
            }
        }

        MutantWriter writer = new MutantWriter();
        int written = 0;

        for (MutationPlan plan : plans) {
            if (written >= config.maxFiles()) break;

            Launcher launcher = new ModelLoader().createLauncher(config.inputFile());
            CtType<?> freshType = launcher.getModel().getAllTypes().stream()
                    .findFirst()
                    .orElseThrow();

            plan.planned.action().apply(freshType);

            CtCompilationUnit cu = freshType.getPosition().getCompilationUnit();
            PrettyPrinter printer = launcher.getEnvironment().createPrettyPrinter();
            printer.calculate(cu, List.of(freshType));
            String content = printer.getResult();

            String fileName = baseName + "_" + plan.mutationId + "_" + plan.planned.fileSuffix() + ".java";
            writer.write(config.outputDir(), fileName, content);
            written++;
        }

        System.out.println("Generated " + written + " mutant files in " + config.outputDir());
    }

    private record MutationPlan(String mutationId, PlannedMutation planned) {}
}