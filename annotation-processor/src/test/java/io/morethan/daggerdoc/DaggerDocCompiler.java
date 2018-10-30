package io.morethan.daggerdoc;

import static com.google.testing.compile.Compiler.javac;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;

import io.morethan.daggerdoc.model.DependencyGraph;

/**
 * Helper tool to run the {@link DaggerDocProcessor} on arbitrary files for testing.
 */
public class DaggerDocCompiler {

    public DependencyGraph compileClass(Class<?> clazz) {
        File file = new File("src/test/java/" + clazz.getName().replaceAll("\\.", "/") + ".java");
        assertThat(file).exists();
        return compileGraph(fileToJavaFileObject(file));
    }

    public DependencyGraph compileFolder(String folder, Set<String> excludes) throws IOException {
        List<File> files = new ArrayList<>();
        gatherSourceFiles(new File(folder), excludes, files);
        return compileGraph(files.stream().map(DaggerDocCompiler::fileToJavaFileObject).toArray(JavaFileObject[]::new));
    }

    private DependencyGraph compileGraph(JavaFileObject... sourceFiles) {
        TestResultWriter resultWriter = new TestResultWriter();
        Compiler compiler = javac().withProcessors(new DaggerDocProcessor(resultWriter));

        Compilation compilation = compiler.compile(sourceFiles);
        assertThat(compilation.errors()).isEmpty();
        DependencyGraph dependencyGraph = resultWriter.dependencyGraph();
        assertThat(dependencyGraph).isNotNull();

        return dependencyGraph;
    }

    private void gatherSourceFiles(File rootFolder, Set<String> excludes, List<File> javaFiles) throws IOException {
        assertThat(rootFolder).exists().isDirectory();
        for (File child : rootFolder.listFiles()) {
            if (child.isDirectory()) {
                gatherSourceFiles(child, excludes, javaFiles);
            } else if (child.getName().endsWith(".java") && !excludes.contains(child.getName())) {
                javaFiles.add(child.getCanonicalFile());
            }
        }
    }

    private static class TestResultWriter implements ResultWriter {

        private DependencyGraph _dependencyGraph;

        @Override
        public String id() {
            return "test";
        }

        public DependencyGraph dependencyGraph() {
            return _dependencyGraph;
        }

        @Override
        public void write(ProcessingEnvironment processingEnv, DependencyGraph dependencyGraph) {
            _dependencyGraph = dependencyGraph;
        }

    }

    private static JavaFileObject fileToJavaFileObject(File t) {
        try {
            return JavaFileObjects.forResource(t.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
