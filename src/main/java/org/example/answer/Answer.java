package org.example.answer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Answer {
    @JsonProperty("examId")
    private int examId;

    @JsonProperty("stuId")
    private int stuId;

    @JsonProperty("submitTime")
    private long submitTime;

    @JsonProperty("answers")
    private List<Map<String, Object>> answers;

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }

    public List<Map<String, Object>> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Map<String, Object>> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "****************************************\n" +
                "Answer{" +
                "\nexamId=" + examId +
                "\nstuId=" + stuId +
                "\nsubmitTime=" + submitTime +
                "\nanswers=" + answers +
                "\n}";
    }

}
