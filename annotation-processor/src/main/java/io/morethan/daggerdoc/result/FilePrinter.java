package io.morethan.daggerdoc.result;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.tools.FileObject;

class FilePrinter implements AutoCloseable {

    private final PrintWriter _fileWriter;

    public FilePrinter(FileObject outputFile) {
        try {
            _fileWriter = new PrintWriter(new OutputStreamWriter(outputFile.openOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void println() {
        _fileWriter.println();
    }

    public void println(String string) {
        _fileWriter.println(string);
    }

    public void printf(String template, Object... values) {
        println(String.format(template, values));
    }

    @Override
    public void close() {
        _fileWriter.close();
    }

}