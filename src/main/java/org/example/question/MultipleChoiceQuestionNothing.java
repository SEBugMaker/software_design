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
                "id=" + getId() +
                " type=" + getType() +
                " scoreMode='" + getScoreMode() + '\'' +
                " question='" + getQuestion() + '\'' +
                " points=" + getPoints() +
                " options=" + getOptions() +
                " answer=" + getAnswer() +
                "}\n";
    }
}
