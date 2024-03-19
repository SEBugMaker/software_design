package org.example.question;

import java.util.Collections;
import java.util.List;

public class MultipleChoiceQuestionPartial extends MultipleChoiceQuestion{
    private List<Integer> partialScore;

    public List<Integer> getPartialScore() {
        return partialScore;
    }

    public void setPartialScore(List<Integer> partialScore) {
        this.partialScore = partialScore;
    }

    @Override
    public int calculateQuestionScore(Object answer){
        if (!(answer instanceof List)) {
            throw new IllegalArgumentException("Invalid argument");
        }

        List<Integer> answerList = (List<Integer>) answer;
        Collections.sort(answerList);
        // 如果答案完全正确，返回问题的总分
        if (this.getAnswer().equals(answerList)) {
            return this.getPoints();
        }
        // 如果答案部分正确并且没有多余的答案，返回部分分数
        else if (this.getAnswer().containsAll(answerList)) {
            int score = 0;
            for (Integer ans : answerList) {
                score += this.getPartialScore().get(ans);  // 假设选项从1开始编号
            }
            return score;
        }
        // 否则，返回 0
        else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "MultipleChoiceQuestionPartial{" +
                "partialScore=" + partialScore +
                ", options=" + getOptions() +
                ", answer=" + getAnswer() +
                ", scoreMode='" + getScoreMode() + '\'' +
                ", id=" + getId() +
                ", type=" + getType() +
                ", question='" + getQuestion() + '\'' +
                ", points=" + getPoints() +
                "}\n";
    }
}
