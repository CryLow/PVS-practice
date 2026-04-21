package org.example.fuzzer.io;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.support.sniper.SniperJavaPrettyPrinter;

import java.nio.file.Path;

public class ModelLoader {

    public CtModel load(Path inputFile) {
        return createLauncher(inputFile).getModel();
    }

    public Launcher createLauncher(Path inputFile) {
        Launcher launcher = new Launcher();
        launcher.getEnvironment().setNoClasspath(true);
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setCommentEnabled(true);
        launcher.getEnvironment().setComplianceLevel(17);
        launcher.getEnvironment().setPrettyPrinterCreator(
                () -> new SniperJavaPrettyPrinter(launcher.getEnvironment())
        );
        launcher.addInputResource(inputFile.toString());
        launcher.buildModel();
        return launcher;
    }
}