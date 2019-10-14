package com.example.clearloglibrary;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;


@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("*")
public class ClearLogProcessor extends AbstractProcessor {
    private Trees trees;// AST
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        messager = env.getMessager();
        if (env instanceof JavacProcessingEnvironment) {
            trees = Trees.instance(((JavacProcessingEnvironment) env));
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver() && trees != null) {
            roundEnv.getRootElements().stream()
                    .filter((Predicate<Element>) element ->
                            element.getKind() == ElementKind.CLASS)
                    .forEach((Consumer<Element>) element ->
                            ((JCTree) trees.getTree(element)).accept(new LogClearTranslator()));
        }
        return false;
    }


    class LogClearTranslator extends TreeTranslator {
        private static final String LOG = "Log.";

        /**
         * @param jcBlock
         */
        @Override
        public void visitBlock(JCTree.JCBlock jcBlock) {
            super.visitBlock(jcBlock);
            List<JCTree.JCStatement> jcStatementList = jcBlock.getStatements();
            if (jcStatementList == null || jcStatementList.isEmpty()) {
                return;
            }
            List<JCTree.JCStatement> out = List.nil();// empty的list
            for (JCTree.JCStatement jcStatement : jcStatementList) {
                if (jcStatement.toString().contains(LOG)) {
                    messager.printMessage(Diagnostic.Kind.WARNING,
                            this.getClass().getCanonicalName()+ "LogClear: " + jcStatement.toString());
                } else {
                    out=out.append(jcStatement);
                }
            }
            jcBlock.stats = out;
        }
    }
}

