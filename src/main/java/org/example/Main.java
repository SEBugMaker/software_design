package org.example;

import org.example.answer.question.ComplexityCalculator.CyclomaticComplexityCalculator;
import org.example.onlineJudge.OJ;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String casePath = args[0];
        // 题目文件夹路径
        String examsPath = casePath + System.getProperty("file.separator") + "exams";
        // 答案文件夹路径
        String answersPath = casePath + System.getProperty("file.separator") + "answers";
        // 输出文件路径
        String output = args[1];
        // TODO:在下面调用你实现的功能


        OJ onlineJudge = new OJ(examsPath, answersPath, output);
        onlineJudge.judgeScore();

        //System.exit(0);
    }
}