package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.List;

// Cette classe collecte les méthodes publiques dans une liste passée en argument
public class PublicElementsPrinter extends VoidVisitorWithDefaults<List<MethodDeclaration>> {

    @Override
    public void visit(CompilationUnit unit, List<MethodDeclaration> methods) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, methods);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, List<MethodDeclaration> methods) {
        if (!declaration.isPublic()) return;

        // Collecte des méthodes publiques
        for (MethodDeclaration method : declaration.getMethods()) {
            if (method.isPublic()) {
                methods.add(method);
            }
        }

        // Collecte des types imbriqués
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration) {
                member.accept(this, methods);
            }
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, List<MethodDeclaration> methods) {
        visitTypeDeclaration(declaration, methods);
    }

    @Override
    public void visit(EnumDeclaration declaration, List<MethodDeclaration> methods) {
        visitTypeDeclaration(declaration, methods);
    }
}
