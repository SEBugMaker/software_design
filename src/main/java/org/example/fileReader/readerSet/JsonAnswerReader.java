package org.example.fileReader.readerSet;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.answer.Answer;
import org.example.fileReader.readerSet.AnswerReader;

import java.io.IOException;
import java.nio.file.Paths;

public class JsonAnswerReader implements AnswerReader {
    @Override
    public Answer readAnswer(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(filePath).toFile(), Answer.class);
    }
}
