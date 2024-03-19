package org.example;

import org.example.answer.Answer;
import org.example.exam.Exam;
import org.example.fileReader.AnswerReader;
import org.example.fileReader.ExamReader;
import org.example.fileReader.JsonAnswerReader;
import org.example.fileReader.JsonExamReader;
import org.example.fileReader.XmlExamReader;
import org.example.onlineJudge.OJ;
import org.example.question.Question;

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
import java.util.stream.Stream;

import javax.xml.bind.JAXBException;

public class Main {
    public static void main(String[] args) {
        String casePath = args[0];
        // 题目文件夹路径
        String examsPath = casePath + System.getProperty("file.separator") + "exams";
        // 答案文件夹路径
        String answersPath = casePath + System.getProperty("file.separator") + "answers";
        // 输出文件路径
        String output = args[1];
        // TODO:在下面调用你实现的功能

        // 获取文件的扩展名
        Map<Integer, Exam> exams = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get(examsPath))) {
            paths.filter(Files::isRegularFile).forEach(file -> {
                // 获取文件扩展名
                String fileExtension = com.google.common.io.Files.getFileExtension(file.toString());
                ExamReader reader;
                // 根据文件的扩展名选择合适的 ExamReader
                if ("json".equals(fileExtension)) {
                    reader = new JsonExamReader();
                } else if ("xml".equals(fileExtension)) {
                    reader = new XmlExamReader();
                } else {
                    throw new IllegalArgumentException("Unsupported file type: " + fileExtension);
                }

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


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
            // 写入 CSV 文件的表头
            writer.write("examId,stuId,score\n");

            AnswerReader answerReader = new JsonAnswerReader();
            try (Stream<Path> paths = Files.walk(Paths.get(answersPath))) {
                paths.filter(Files::isRegularFile).forEach(file -> {
                    try {
                        // 使用 AnswerReader 读取文件
                        Answer answer = answerReader.readAnswer(file.toString());
                        OJ onlineJudge = new OJ(exams.get(answer.getExamId()), answer);
                        int score = onlineJudge.calculateExamScore();

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


        // 读取 CSV 文件并排序
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
                writer.write("examId,stuId,score\n");
                for (String[] row : rows) {
                    writer.write(String.join(",", row) + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}