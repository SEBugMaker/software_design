package org.example.fileReader.readerSet;

import org.example.exam.Exam;

import java.io.IOException;

import javax.xml.bind.JAXBException;

public interface ExamReader {
    Exam readExam(String filePath) throws IOException, JAXBException;
}

