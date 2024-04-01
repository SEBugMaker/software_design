package org.example.onlineJudge;

import org.example.answer.Answer;
import org.example.exam.Exam;
import org.example.fileReader.AnswerReader;
import org.example.fileReader.AnswerReaderFactory;
import org.example.fileReader.ExamReader;
import org.example.fileReader.ExamReaderFactory;
import org.example.fileReader.JsonAnswerReader;
import org.example.question.Question;
import org.example.threadPool.ThreadPool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import javax.xml.bind.JAXBException;

public class OJ {

    private Map<Integer, Exam> exams;

    private String examsPath;
    private String answersPath;
    private String output;

    public OJ(String examsPath, String answersPath, String output) {
        this.examsPath = examsPath;
        this.answersPath = answersPath;
        this.output = output;
        this.exams = new HashMap<>();
    }

    public void judgeScore() {
        UploadExamsInfo();
        JudgeStudentAnswer();
        sortCsvFile();
    }


    private void UploadExamsInfo() {
        try (Stream<Path> paths = Files.walk(Paths.get(examsPath))) {
            paths.filter(Files::isRegularFile).forEach(file -> {
                // 获取文件的扩展名
                ExamReaderFactory factory = new ExamReaderFactory();
                String fileExtension = com.google.common.io.Files.getFileExtension(file.toString());
                ExamReader reader = factory.createExamReader(fileExtension);
                try {
                    // 使用 ExamReader 读取文件
                    Exam exam = reader.readExam(file.toString());
                    exams.put(exam.getId(), exam);
                } catch (IOException | JAXBException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void JudgeStudentAnswer() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
            // 写入 CSV 文件的表头
            writer.write("examId,stuId,score\n");
            AnswerReaderFactory answerFactory = new AnswerReaderFactory();
            try (Stream<Path> paths = Files.list(Paths.get(answersPath))) {
                paths.filter(Files::isRegularFile).forEach(file -> {
                    try {
                        // 使用 AnswerFactory 创建 AnswerReader
                        String fileExtension = com.google.common.io.Files.getFileExtension(file.toString());
                        AnswerReader answerReader = answerFactory.createAnswerReader(fileExtension);
                        // 使用 AnswerReader 读取文件
                        Answer answer = answerReader.readAnswer(file.toString());
                        Exam exam = exams.get(answer.getExamId());
                        int score = calculateExamScore(exam, answer);

                        // 将 examId、stuId 和 score 写入 CSV 文件
                        writer.write(answer.getExamId() + "," + answer.getStuId() + "," + score + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sortCsvFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(output))) {
            List<String[]> rows = new ArrayList<>();
            String line;
            // 跳过表头行
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                rows.add(line.split(","));
            }

            // 按照 examId 和 stuId 排序
            rows.sort(Comparator.comparingInt((String[] a) -> Integer.parseInt(a[0])).thenComparingInt(a -> Integer.parseInt(a[1])));

            // 写回 CSV 文件
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
                // 写入 CSV 文件的表头
                writer.write("examId, stuId, score\n");
                for (String[] row : rows) {
                    writer.write(String.join(",", row) + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int calculateExamScore(Exam exam, Answer answer) {
        List<Question> questions = exam.getQuestions();
        List<Map<String, Object>> answers = answer.getAnswers();
        int totalScore = 0;
        if (answer.getSubmitTime() < exam.getStartTime() || answer.getSubmitTime() > exam.getEndTime()) {
            System.out.println("Student " + answer.getStuId() + " get total score " + totalScore + " at exam " + answer.getExamId());
            return 0;
        }
        // 创建一个线程池
        ThreadPool threadPool = new ThreadPool(5);
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            int score = 0;
            final int index = i;
            if (question.getType() == 1) {
                String temp = (String) answers.get(i).get("answer");
                score = question.calculateQuestionScore((int) (temp.charAt(0) - 'A'));
            } else if (question.getType() == 3) {

                // 创建一个任务并提交给线程池
                Future<Integer> future = threadPool.submit(() -> {
                    // System.out.println("Current thread: " + Thread.currentThread().getName());
                    return question.calculateQuestionScore(answersPath+"/"+(String) answers.get(index).get("answer"));
                });

                // 获取任务的结果，也就是分数
                try {
                    score = future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //score = question.calculateQuestionScore(answersPath+"/"+(String) answers.get(i).get("answer"));
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
        System.out.println("Student " + answer.getStuId() + " get total score " + totalScore + " at exam " + answer.getExamId());
        return totalScore;
    }

}
