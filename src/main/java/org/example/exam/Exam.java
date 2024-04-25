package org.example.exam;

import org.example.answer.question.Question;

import java.util.List;


public class Exam {
    private int id;
    private String title;
    private long startTime;
    private long endTime;
    private List<Question> questions;

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


    @Override
    public String toString() {
        return "Exam{" +
                "\n id=" + id +
                "\n title='" + title + '\'' +
                "\n startTime=" + startTime +
                "\n endTime=" + endTime +
                "\n questions=\n" + questions +
                "\n}" +
                "\n========================================\n";
    }
}