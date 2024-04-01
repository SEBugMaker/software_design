package org.example.question;

import java.util.Collections;
import java.util.List;

public class MultipleChoiceQuestionFix extends MultipleChoiceQuestion{
    private int fixScore;

    public MultipleChoiceQuestionFix() {
        this.setScoreMode("fix");
    }
    public int getFixScore() {
        return fixScore;
    }

    public void setFixScore(int fixScore) {
        this.fixScore = fixScore;
    }


    @Override
    public int calculateQuestionScore(Object studentAnswer){
        if (!(studentAnswer instanceof List)) {
            throw new IllegalArgumentException("Invalid argument");
        }

        List<Integer> answerList = (List<Integer>) studentAnswer;
        Collections.sort(answerList);


        // 如果答案完全正确，返回问题的总分
        if (this.getAnswer().equals(answerList)) {
            return this.getPoints();
        }
        // 如果答案部分正确并且没有多余的答案，返回固定的分数
        else if (this.getAnswer().containsAll(answerList)) {
            return this.getFixScore();
        }
        // 否则，返回 0
        else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "MultipleChoiceQuestionFix{" +
                "id=" + getId() +
                " type=" + getType() +
                " scoreMode='" + getScoreMode() + '\'' +
                " question='" + getQuestion() + '\'' +
                " points=" + getPoints() +
                " options=" + getOptions() +
                " answer=" + getAnswer() +
                " fixScore=" + fixScore +
                "}\n";
    }
}
