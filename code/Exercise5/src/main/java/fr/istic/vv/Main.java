package fr.istic.vv;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: java Main <source-path> <output-file>");
            System.exit(1);
        }

        File sourceDir = new File(args[0]);
        if (!sourceDir.exists() || !sourceDir.isDirectory() || !sourceDir.canRead()) {
            System.err.println("Invalid source directory provided.");
            System.exit(2);
        }

        String outputFile = args[1];

        // Initialisation de SourceRoot pour parser les fichiers
        SourceRoot sourceRoot = new SourceRoot(sourceDir.toPath());
        PublicElementsPrinter printer = new PublicElementsPrinter();

        // Liste pour stocker les méthodes publiques collectées
        List<MethodDeclaration> publicMethods = new ArrayList<>();

        // Parcourir et analyser les fichiers source
        sourceRoot.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, publicMethods));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        // Analyse de la complexité cyclomatique
        List<CyclomaticComplexityAnalyzer.MethodCC> ccList =
                CyclomaticComplexityAnalyzer.analyzeMethods(publicMethods);

        // Écriture du rapport
        writeReport(ccList, outputFile);

        // Génération de l'histogramme
        CyclomaticComplexityAnalyzer.generateHistogram(ccList, "Cyclomatic Complexity Distribution");
    }

    private static void writeReport(List<CyclomaticComplexityAnalyzer.MethodCC> ccList, String outputFile) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write("Package,Class,Method,Parameters,Cyclomatic Complexity\n");
            for (CyclomaticComplexityAnalyzer.MethodCC methodCC : ccList) {
                writer.write(methodCC.toString() + "\n");
            }
            System.out.println("Report written to: " + outputFile);
        }
    }
}
