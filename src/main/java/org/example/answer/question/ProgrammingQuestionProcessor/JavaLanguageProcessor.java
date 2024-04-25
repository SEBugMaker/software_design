package org.example.answer.question.ProgrammingQuestionProcessor;


import org.example.answer.question.ComplexityCalculator.CyclomaticComplexityCalculator;
import org.example.threadPool.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class JavaLanguageProcessor implements LanguageProcessor {

    //private static Logger logger = LogManager.getLogger(JavaLanguageProcessor.class);
    private static final Logger log = LoggerFactory.getLogger(JavaLanguageProcessor.class);

    @Override
    public boolean compile(String studentAnswerPath,int questionID) {
        try {
            // 创建一个新的 ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder();
            String ans = studentAnswerPath.substring(studentAnswerPath.lastIndexOf('/') + 1, studentAnswerPath.lastIndexOf('.'));
            int examID = Integer.parseInt(ans.substring(ans.length()-2,ans.length()-1));
            int studentID = Integer.parseInt(ans.substring(ans.length()-1));
            //System.out.println(examID + " " + studentID);
            // 指定 .class 文件的输出目录
            String outputDirectory = "./src/test/resources/cases/compiledFile";

            // 创建目录
            Files.createDirectories(Paths.get(outputDirectory));

            // 设置要执行的命令
            processBuilder.command("javac", "-d", outputDirectory, studentAnswerPath);

            // 启动新进程
            Process process = processBuilder.start();

            // 获取错误流
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            StringBuilder err = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                err.append(line);
            }

            // 等待进程结束
            int exitCode = process.waitFor();

            // 检查是否有错误
            if (exitCode != 0) {
                //System.out.println("Compilation failed.");
                log.error("compiled failed at " + studentAnswerPath.substring(studentAnswerPath.lastIndexOf('/') + 1));
                // Set cyclomatic complexity to -1
                int complexity = -1;
                System.out.println("Cyclomatic Complexity of " + studentAnswerPath + ": " + complexity);
                writeComplexityToCSV("src/test/resources/cases/output_complexity.csv", examID, studentID, questionID, -1);
                sortCSV("src/test/resources/cases/output_complexity.csv");
                return false;
            } else {
                // Calculate cyclomatic complexity
                CyclomaticComplexityCalculator calculator = new CyclomaticComplexityCalculator();
                int complexity = calculator.calculate(studentAnswerPath);
                System.out.println("Cyclomatic Complexity of " + studentAnswerPath + ": " + complexity);
                writeComplexityToCSV("src/test/resources/cases/output_complexity.csv", examID, studentID, questionID, complexity);
                sortCSV("src/test/resources/cases/output_complexity.csv");
                return true;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            // Set cyclomatic complexity to -1
            int complexity = -1;
            System.out.println("Cyclomatic Complexity of " + studentAnswerPath + ": " + complexity);
            return false;
        }
    }

    @Override
    public int calculateQuestionScore(String studentAnswerPath, List<Map<String, String>> samples, int point, int timeLimit) {
        try {
            String className = studentAnswerPath.substring(studentAnswerPath.lastIndexOf("/") + 1, studentAnswerPath.lastIndexOf("."));
            ThreadPool threadPool = new ThreadPool(5);
            List<Future<Boolean>> futures = new ArrayList<>();
            List<String> command = new ArrayList<>();
            command.add("java");
            command.add("-cp");
            command.add("./src/test/resources/cases/compiledFile");
            command.add(className);

            List<Process> processes = new ArrayList<>(); // Use a list to hold the Process

            for (Map<String, String> sample : samples) {
                Callable<Boolean> task = () -> {
                    Process runProcess = null;
                    try {
                        log.info("Processing sample in " + className + " in thread: " + Thread.currentThread().getName() + " time limit = " + timeLimit);
                        ProcessBuilder runProcessBuilder = new ProcessBuilder();
                        String inputArgs = sample.get("input");
                        String[] inputArgsArray = inputArgs.split(" ");
                        List<String> completeCommand = new ArrayList<>(command);
                        completeCommand.addAll(Arrays.asList(inputArgsArray));
                        runProcessBuilder.command(completeCommand);
                        runProcess = runProcessBuilder.start();
                        synchronized (processes) {
                            processes.add(runProcess);
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                        String output = reader.readLine();
                        String expectedOutput = sample.get("output").trim();
                        return output.equals(expectedOutput);
                    } finally {
                        runProcess.destroy();
                        synchronized (processes) {
                            processes.remove(runProcess);
                        }
                    }
                };
                Future<Boolean> future = threadPool.submit(task);
                futures.add(future);
            }

            for (Future<Boolean> future : futures) {
                try {
                    if (!future.get(timeLimit, TimeUnit.MILLISECONDS)) {
                        synchronized (processes) {
                            processes.forEach(Process::destroy);
                        }
                        return 0;
                    }
                } catch (TimeoutException e) {
                    synchronized (processes) {
                        processes.forEach(Process::destroy);
                    }
                    threadPool.shutdownNow(); // Add this line
                    return 0;
                }
            }
            threadPool.shutdown();
            return point;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void writeComplexityToCSV(String filePath, int examId, int stuId, int qId, int complexity) {
        try {
            // 检查文件是否存在
            if (!Files.exists(Paths.get(filePath))) {
                // 如果不存在，创建新的文件
                Files.createFile(Paths.get(filePath));

                // 写入 CSV 文件的表头
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                    writer.write("examId,stuId,qId,complexity\n");
                }
            }

            // 读取CSV文件的所有行
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            Set<String> lines = new HashSet<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
            reader.close();

            // 构造新的行
            String newLine = examId + "," + stuId + "," + qId + "," + complexity;

            // 检查新的行是否已经存在
            if (!lines.contains(newLine)) {
                // 如果不存在，写入新的行
                FileWriter fileWriter = new FileWriter(filePath, true);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.println(newLine.trim());
                printWriter.close();
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortCSV(String filePath) {
        try {
            // 读取CSV文件的所有行
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            List<String[]> lines = new ArrayList<>();
            String line;

            // 读取并保存表头
            String header = reader.readLine().trim();

            while ((line = reader.readLine()) != null) {
                lines.add(line.trim().split(","));
            }
            reader.close();

            // 按照examId, stuId, qId进行排序
            lines.sort((a, b) -> {
                int cmp = a[0].compareTo(b[0]);
                if (cmp != 0) return cmp;
                cmp = a[1].compareTo(b[1]);
                if (cmp != 0) return cmp;
                return a[2].compareTo(b[2]);
            });

            // 将排序后的行写回到文件中
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            // 写入表头
            writer.write(header);
            writer.newLine();

            for (String[] row : lines) {
                writer.write(String.join(",", row).trim());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}