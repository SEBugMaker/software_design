package org.example.question;

import java.util.List;
import java.util.Map;

public class ProgrammingQuestion extends Question{
    private List<Map<String, String>> samples;
    private int timeLimit;
    private int points;
    public ProgrammingQuestion(){
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
    public int calculateQuestionScore(String studentAnswer){
        return points;
    }

    @Override
    public String toString() {
        return "ProgrammingQuestion{" +
                "samples=" + samples +
                ", timeLimit=" + timeLimit +
                ", points=" + points +
                ", id=" + id +
                ", type=" + type +
                ", question='" + question + '\'' +
                ", points=" + points +
                "}\n";
    }
}
