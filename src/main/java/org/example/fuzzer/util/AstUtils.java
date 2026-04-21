package org.example.fuzzer.util;

import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.CtScanner;

import java.util.ArrayList;
import java.util.List;

public final class AstUtils {

    private AstUtils() {
    }

    public static List<CtBinaryOperator<?>> binaryOperators(CtElement root) {
        List<CtBinaryOperator<?>> result = new ArrayList<>();
        root.accept(new CtScanner() {
            @Override
            public <T> void visitCtBinaryOperator(CtBinaryOperator<T> operator) {
                result.add(operator);
                super.visitCtBinaryOperator(operator);
            }
        });
        return result;
    }

    public static List<CtLocalVariable<?>> localVariables(CtElement root) {
        List<CtLocalVariable<?>> result = new ArrayList<>();
        root.accept(new CtScanner() {
            @Override
            public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
                result.add(localVariable);
                super.visitCtLocalVariable(localVariable);
            }
        });
        return result;
    }

    public static String javaOperator(BinaryOperatorKind kind) {
        return switch (kind) {
            case OR -> "||";
            case AND -> "&&";
            case BITOR -> "|";
            case BITXOR -> "^";
            case BITAND -> "&";
            case EQ -> "==";
            case NE -> "!=";
            case LT -> "<";
            case GT -> ">";
            case LE -> "<=";
            case GE -> ">=";
            case SL -> "<<";
            case SR -> ">>";
            case USR -> ">>>";
            case PLUS -> "+";
            case MINUS -> "-";
            case MUL -> "*";
            case DIV -> "/";
            case MOD -> "%";
            case INSTANCEOF -> "instanceof";
        };
    }

    @SuppressWarnings("unchecked")
    public static void replaceBinaryOperatorWithSnippet(CtBinaryOperator<?> target, String expressionSource) {
        Factory factory = target.getFactory();
        CtExpression<Object> snippet = factory.Code().createCodeSnippetExpression(expressionSource);
        ((CtExpression<Object>) target).replace(snippet);
    }

    public static CtStatement nearestStatement(CtElement element) {
        return element.getParent(CtStatement.class);
    }
}