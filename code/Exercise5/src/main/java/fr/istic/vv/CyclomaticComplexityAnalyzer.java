package fr.istic.vv;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CyclomaticComplexityAnalyzer {

    public static class MethodCC {
        String packageName;
        String className;
        String methodName;
        String parameters;
        int cyclomaticComplexity;

        public MethodCC(String packageName, String className, String methodName, String parameters, int cc) {
            this.packageName = packageName;
            this.className = className;
            this.methodName = methodName;
            this.parameters = parameters;
            this.cyclomaticComplexity = cc;
        }

        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s,%d",
                    packageName, className, methodName, parameters, cyclomaticComplexity);
        }
    }

    public static List<MethodCC> analyzeMethods(List<MethodDeclaration> methods) {
        List<MethodCC> ccList = new ArrayList<>();
        for (MethodDeclaration method : methods) {
            String className = method.findAncestor(com.github.javaparser.ast.body.ClassOrInterfaceDeclaration.class)
                    .map(c -> c.getNameAsString())
                    .orElse("UnknownClass");
            String packageName = method.findCompilationUnit()
                    .flatMap(cu -> cu.getPackageDeclaration().map(pd -> pd.getNameAsString()))
                    .orElse("UnknownPackage");

            int complexity = calculateCyclomaticComplexity(method);

            MethodCC methodCC = new MethodCC(
                    packageName,
                    className,
                    method.getNameAsString(),
                    method.getParameters().toString(),
                    complexity
            );

            ccList.add(methodCC);
        }
        return ccList;
    }

    private static int calculateCyclomaticComplexity(MethodDeclaration method) {
        CyclomaticCounter counter = new CyclomaticCounter();
        method.getBody().ifPresent(body -> body.accept(counter, null));
        return counter.getCyclomaticComplexity();
    }

    public static class CyclomaticCounter extends com.github.javaparser.ast.visitor.VoidVisitorAdapter<Void> {
        private int cc = 1;

        @Override
        public void visit(com.github.javaparser.ast.stmt.IfStmt stmt, Void arg) {
            cc++;
            super.visit(stmt, arg);
        }

        @Override
        public void visit(com.github.javaparser.ast.stmt.ForStmt stmt, Void arg) {
            cc++;
            super.visit(stmt, arg);
        }

        @Override
        public void visit(com.github.javaparser.ast.stmt.WhileStmt stmt, Void arg) {
            cc++;
            super.visit(stmt, arg);
        }

        @Override
        public void visit(com.github.javaparser.ast.stmt.DoStmt stmt, Void arg) {
            cc++;
            super.visit(stmt, arg);
        }

        @Override
        public void visit(com.github.javaparser.ast.stmt.SwitchEntry stmt, Void arg) {
            if (stmt.getLabels().isNonEmpty()) {
                cc++;
            }
            super.visit(stmt, arg);
        }

        @Override
        public void visit(com.github.javaparser.ast.expr.ConditionalExpr expr, Void arg) {
            cc++;
            super.visit(expr, arg);
        }

        public int getCyclomaticComplexity() {
            return cc;
        }
    }

    public static void generateHistogram(List<MethodCC> ccList, String chartTitle) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<Integer, Long> ccDistribution = new HashMap<>();
        for (MethodCC method : ccList) {
            ccDistribution.put(method.cyclomaticComplexity, ccDistribution.getOrDefault(method.cyclomaticComplexity, 0L) + 1);
        }

        ccDistribution.forEach((cc, count) -> dataset.addValue(count, "Methods", cc));

        org.jfree.chart.JFreeChart chart = ChartFactory.createBarChart(
                chartTitle,
                "Cyclomatic Complexity",
                "Number of Methods",
                dataset
        );

        File chartFile = new File("cc_distribution.png");
        ChartUtils.saveChartAsPNG(chartFile, chart, 800, 600);
        System.out.println("Histogram saved as: cc_distribution.png");
    }
}
