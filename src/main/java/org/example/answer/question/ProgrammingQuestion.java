package org.example.answer.question;

import org.example.answer.question.ProgrammingQuestionProcessor.LanguageProcessor;
import org.example.answer.question.ProgrammingQuestionProcessor.LanguageProcessorFactory;

import java.util.List;
import java.util.Map;

public class ProgrammingQuestion extends Question {
    private List<Map<String, String>> samples;
    private int timeLimit;
    private int points;

    public ProgrammingQuestion() {
        this.setType(3);
    }

    public List<Map<String, String>> getSamples() {
        return samples;
    }

    public void setSamples(List<Map<String, String>> samples) {
        this.samples = samples;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int calculateQuestionScore(Object studentAnswerPath) {

        if (!(studentAnswerPath instanceof String)) {
            throw new IllegalArgumentException("Invalid argument");
        }


        String studentAnswerPathStr = (String) ((String) studentAnswerPath).substring(0, ((String) studentAnswerPath).length()-2);
        int questionID = Integer.parseInt((String)((String) studentAnswerPath).substring(((String) studentAnswerPath).length()-1));
        LanguageProcessorFactory factory = new LanguageProcessorFactory();
        String languageType = studentAnswerPathStr.substring(studentAnswerPathStr.lastIndexOf(".")+1);
        LanguageProcessor languageProcessor = factory.createProcessor(languageType);
        boolean colpileFlag = languageProcessor.compile(studentAnswerPathStr,questionID);
        if(!colpileFlag){
            return 0;
        }
        return languageProcessor.calculateQuestionScore(studentAnswerPathStr,this.samples,this.points,this.timeLimit);
    }

    @Override
    public String toString() {
        return "ProgrammingQuestion{" + "id=" + id + " type=" + type + " question='" + question + '\'' + " points=" + points + " samples=" + samples + " timeLimit=" + timeLimit + "}\n";
    }
}
