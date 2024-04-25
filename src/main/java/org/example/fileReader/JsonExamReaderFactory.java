package org.example.fileReader;

import org.example.fileReader.readerSet.ExamReader;
import org.example.fileReader.readerSet.JsonExamReader;

public class JsonExamReaderFactory extends abstractExamReaderFactory {
    @Override
    public ExamReader createExamReader() {

        return new JsonExamReader();

    }
}
