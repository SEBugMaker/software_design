package org.example.answer.question.ProgrammingQuestionProcessor;

import java.util.List;
import java.util.Map;

public interface LanguageProcessor {
    boolean compile(String studentAnswerPath,int questionID);
    int calculateQuestionScore(String studentAnswerPath, List<Map<String, String>> samples, int point,int timeLimit);
}
