package org.example.fileReader;

import org.example.exam.Exam;

import java.io.IOException;

import javax.xml.bind.JAXBException;

public interface ExamReader {
    Exam readExam(String filePath) throws IOException, JAXBException;
}

