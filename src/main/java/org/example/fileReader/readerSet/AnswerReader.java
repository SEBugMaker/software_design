package org.example.fileReader.readerSet;

import org.example.answer.Answer;

import java.io.IOException;

public interface AnswerReader {
    Answer readAnswer(String filePath) throws IOException;
}
