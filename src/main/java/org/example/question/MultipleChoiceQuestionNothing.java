package org.example.question;

import java.util.List;


public class MultipleChoiceQuestionNothing extends MultipleChoiceQuestion{
    public MultipleChoiceQuestionNothing() {
        this.setScoreMode("nothing");
    }


    @Override
    public int calculateQuestionScore(Object answer){
        if (!(answer instanceof List)) {
            throw new IllegalArgumentException("Invalid argument");
        }

        List<Integer> answerList = (List<Integer>) answer;

        // 如果答案完全正确，返回问题的总分
        if (this.getAnswer().equals(answerList)) {
            return this.getPoints();
        }
        // 否则，返回 0
        return 0;
    }
    @Override
    public String toString() {
        return "MultipleChoiceQuestionNothing{" +
                "options=" + getOptions() +
                ", answer=" + getAnswer() +
                ", scoreMode='" + getScoreMode() + '\'' +
                ", id=" + getId() +
                ", type=" + getType() +
                ", question='" + getQuestion() + '\'' +
                ", points=" + getPoints() +
                "}\n";
    }
}
