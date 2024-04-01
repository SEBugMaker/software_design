package org.example.question;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProgrammingQuestion extends Question {
    private List<Map<String, String>> samples;
    private int timeLimit;
    private int points;

    public ProgrammingQuestion() {
        this.setType(3);
    }

    public List<Map<String, String>> getSamples() {
        return samples;
    }

    public void setSamples(List<Map<String, String>> samples) {
        this.samples = samples;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int calculateQuestionScore(String studentAnswerPath) {
        // System.out.println(studentAnswerPath);
        try {
            // 创建一个新的 ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder();

            // 指定 .class 文件的输出目录
            String outputDirectory = "/Users/rain/Desktop/data/24-spring/软件系统设计/homework/software_design/src/test/resources/cases/classCode";

            // 创建目录
            Files.createDirectories(Paths.get(outputDirectory));

            // 设置要执行的命令
            processBuilder.command("javac", "-d", outputDirectory, studentAnswerPath);

            // 启动新进程
            Process process = processBuilder.start();

            // 等待进程结束
            int exitCode = process.waitFor();

            // 检查是否有错误
            if (exitCode != 0) {
                //System.out.println("Compilation failed.");
                return 0;
            }

            // 获取编译后的 .class 文件的路径
            String className = studentAnswerPath.substring(studentAnswerPath.lastIndexOf("/") + 1, studentAnswerPath.lastIndexOf("."));

            // 创建一个新的 ProcessBuilder 来运行 .class 文件
            ProcessBuilder runProcessBuilder = new ProcessBuilder();
            // 遍历每个样例
            for (Map<String, String> sample : this.getSamples()) {
                // 获取输入参数
                String inputArgs = sample.get("input");

                // 分割输入参数
                String[] inputArgsArray = inputArgs.split(" ");

                // 设置要执行的命令，包括 java 命令、类路径和类名
                List<String> command = new ArrayList<>();
                command.add("java");
                command.add("-cp");
                command.add("/Users/rain/Desktop/data/24-spring/软件系统设计/homework/software_design/src/test/resources/cases/classCode");
                command.add(className);
                command.addAll(Arrays.asList(inputArgsArray));
                runProcessBuilder.command(command);

                // 启动新进程
                Process runProcess = runProcessBuilder.start();

                // 获取进程的标准输出
                BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                String output = reader.readLine();

                // 获取样例的预期输出
                String expectedOutput = sample.get("output").trim();

                // 比较进程的标准输出和样例的预期输出
                if (!output.equals(expectedOutput)) {
                    return 0;
                }
            }
            return points;


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() {
        return "ProgrammingQuestion{" + "id=" + id + " type=" + type + " question='" + question + '\'' + " points=" + points + " samples=" + samples + " timeLimit=" + timeLimit + "}\n";
    }
}
