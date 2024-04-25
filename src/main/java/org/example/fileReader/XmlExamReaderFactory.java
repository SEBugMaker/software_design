package org.example.fileReader;

import org.example.fileReader.readerSet.ExamReader;
import org.example.fileReader.readerSet.XmlExamReader;

public class XmlExamReaderFactory extends abstractExamReaderFactory{

    @Override
    public ExamReader createExamReader() {
        return new XmlExamReader();
    }
}
