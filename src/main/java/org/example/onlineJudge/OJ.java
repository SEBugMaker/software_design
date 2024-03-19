package org.example.onlineJudge;

import org.example.answer.Answer;
import org.example.exam.Exam;
import org.example.question.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OJ {
    private Exam exam;
    private Answer answer;

    public OJ(Exam exam, Answer answer) {
        this.exam = exam;
        this.answer = answer;
    }

    public int calculateExamScore() {
        List<Question> questions = exam.getQuestions();
        List<Map<String, Object>> answers = answer.getAnswers();
        int totalScore = 0;
        if (answer.getSubmitTime() < exam.getStartTime() || answer.getSubmitTime() > exam.getEndTime()) {
            System.out.println("Student ID: " + answer.getStuId() + " at Exam " + answer.getExamId() + " Total score: " + totalScore);
            return 0;
        }
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            int score = 0;
            if (question.getType() == 1) {
                String temp = (String) answers.get(i).get("answer");
                score = question.calculateQuestionScore((int) (temp.charAt(0) - 'A'));
            } else if (question.getType() == 3) {
                score = question.calculateQuestionScore("");
            } else if (question.getType() == 2) {
                String temp = (String) answers.get(i).get("answer");
                List<Integer> ans = new ArrayList<Integer>();
                for (int j = 0; j < temp.length(); j++) {
                    ans.add((int) (temp.charAt(j) - 'A'));
                }
                score = question.calculateQuestionScore(ans);
            }
            totalScore += score;
            //System.out.println("Question " + question.getId() + " score: " + score);
        }
        System.out.println("Student ID: " + answer.getStuId() + " at Exam " + answer.getExamId() + " Total score: " + totalScore);
        return totalScore;
    }

}
