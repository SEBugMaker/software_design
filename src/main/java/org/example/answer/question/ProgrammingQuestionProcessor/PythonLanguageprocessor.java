package org.example.answer.question.ProgrammingQuestionProcessor;

import java.util.List;
import java.util.Map;

public class PythonLanguageprocessor implements LanguageProcessor{
    @Override
    public boolean compile(String studentAnswerPath,int id) {
        return false;
    }

    @Override
    public int calculateQuestionScore(String studentAnswerPath, List<Map<String, String>> samples, int point,int timeLimit) {
        return 0;
    }
}
