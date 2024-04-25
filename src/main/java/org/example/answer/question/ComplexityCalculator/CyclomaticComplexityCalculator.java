package org.example.answer.question.ComplexityCalculator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CyclomaticComplexityCalculator {

    public int calculate(String filePath) throws FileNotFoundException {
        FileInputStream in = new FileInputStream(filePath);

        JavaParser javaParser = new JavaParser();
        CompilationUnit cu = javaParser.parse(in).getResult().orElseThrow();

        int totalComplexity = 0;
        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
            int decisionNodes = countDecisionNodes(method);
            totalComplexity += decisionNodes + 1;  // +1 for each method
        }

        return totalComplexity;
    }

    private int countDecisionNodes(Node node) {
        int count = 0;

        if (node instanceof IfStmt || node instanceof WhileStmt || node instanceof DoStmt || node instanceof ForStmt || node instanceof ConditionalExpr) {
            count++;
        }

        if (node instanceof BinaryExpr) {
            BinaryExpr.Operator operator = ((BinaryExpr) node).getOperator();
            if (operator == BinaryExpr.Operator.AND || operator == BinaryExpr.Operator.OR) {
                count++;
            }
        }

        for (Node child : node.getChildNodes()) {
            count += countDecisionNodes(child);
        }

        return count;
    }

    public static void main(String[] args) throws IOException {
        CyclomaticComplexityCalculator calculator = new CyclomaticComplexityCalculator();
        Path startPath = Paths.get("src/test/resources/cases/answers/code-answers");
        Files.walk(startPath)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        int complexity = calculator.calculate(path.toString());
                        System.out.println("Cyclomatic Complexity of " + path.getFileName() + ": " + complexity);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                });
    }
}