package org.example.fileReader.readerSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.example.exam.Exam;
import org.example.answer.question.MultipleChoiceQuestion;

import java.io.File;
import java.io.IOException;

public class JsonExamReader implements ExamReader {
    @Override
    public Exam readExam(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MultipleChoiceQuestion.class, new MultipleChoiceQuestionDeserializer());
        mapper.registerModule(module);
        return mapper.readValue(new File(filePath), Exam.class);
    }
}
