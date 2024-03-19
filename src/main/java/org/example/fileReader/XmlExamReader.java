package org.example.fileReader;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.exam.Exam;
import org.example.question.MultipleChoiceQuestionFix;
import org.example.question.MultipleChoiceQuestionNothing;
import org.example.question.MultipleChoiceQuestionPartial;
import org.example.question.ProgrammingQuestion;
import org.example.question.Question;
import org.example.question.SingleChoiceQuestion;
import org.example.question.MultipleChoiceQuestion;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class XmlExamReader implements ExamReader {
    @Override
    public Exam readExam(String filePath) {
        try {
            File inputFile = new File(filePath);
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputFile);

            Element classElement = document.getRootElement();

            // 创建 Exam 对象
            Exam exam = new Exam();
            exam.setId(Integer.parseInt(classElement.elementText("id")));
            exam.setTitle(classElement.elementText("title"));
            exam.setStartTime(Long.parseLong(classElement.elementText("startTime")));
            exam.setEndTime(Long.parseLong(classElement.elementText("endTime")));

            // 处理 questions
            List<Question> questions = new ArrayList<>();
            Iterator<Element> questionIterator = classElement.element("questions").elementIterator();
            while (questionIterator.hasNext()) {
                Element questionElement = questionIterator.next();
                // 根据 question 的类型创建相应的 Question 对象，并添加到 exam 的 questions 列表中
                int type = Integer.parseInt(questionElement.elementText("type"));
                if (type == 1) {
                    SingleChoiceQuestion question = new SingleChoiceQuestion();
                    question.setId(Integer.parseInt(questionElement.elementText("id")));
                    question.setQuestion(questionElement.elementText("question"));
                    question.setPoints(Integer.parseInt(questionElement.elementText("points")));
                    question.setAnswer(Integer.parseInt(questionElement.elementText("answer")));
                    List<String> options = new ArrayList<>();
                    Iterator<Element> optionIterator = questionElement.element("options").elementIterator();
                    while (optionIterator.hasNext()) {
                        options.add(optionIterator.next().getText());
                    }
                    question.setOptions(options);
                    questions.add(question);
                } else if (type == 2) {
                    // 这里需要你自己实现 MultipleChoiceQuestion 对象的创建和设置属性的逻辑
                    String scoreMode = questionElement.elementText("scoreMode");
                    MultipleChoiceQuestion question;
                    if ("fix".equals(scoreMode)) {
                        question = new MultipleChoiceQuestionFix();
                        ((MultipleChoiceQuestionFix) question).setFixScore(Integer.parseInt(questionElement.elementText("fixScore")));
                    } else if ("partial".equals(scoreMode)) {
                        question = new MultipleChoiceQuestionPartial();
                        List<Integer> partialScores = new ArrayList<>();
                        Iterator<Element> partialScoreIterator = questionElement.element("partialScores").elementIterator();
                        while (partialScoreIterator.hasNext()) {
                            partialScores.add(Integer.parseInt(partialScoreIterator.next().getText()));
                        }
                        ((MultipleChoiceQuestionPartial) question).setPartialScore(partialScores);
                    } else {
                        question = new MultipleChoiceQuestionNothing();
                    }
                    question.setId(Integer.parseInt(questionElement.elementText("id")));
                    question.setQuestion(questionElement.elementText("question"));
                    question.setPoints(Integer.parseInt(questionElement.elementText("points")));
                    List<Integer> answer = new ArrayList<>();
                    Iterator<Element> answerIterator = questionElement.element("answers").elementIterator();
                    while (answerIterator.hasNext()) {
                        answer.add(Integer.parseInt(answerIterator.next().getText()));
                    }
                    question.setAnswer(answer);
                    List<String> options = new ArrayList<>();
                    Iterator<Element> optionIterator = questionElement.element("options").elementIterator();
                    while (optionIterator.hasNext()) {
                        options.add(optionIterator.next().getText());
                    }
                    question.setOptions(options);
                    questions.add(question);
                } else if (type == 3) {
                    ProgrammingQuestion question = new ProgrammingQuestion();
                    question.setId(Integer.parseInt(questionElement.elementText("id")));
                    question.setQuestion(questionElement.elementText("question"));
                    question.setPoints(Integer.parseInt(questionElement.elementText("points")));
                    question.setTimeLimit(Integer.parseInt(questionElement.elementText("timeLimit")));
                    List<Map<String, String>> samples = new ArrayList<>();
                    Iterator<Element> sampleIterator = questionElement.element("samples").elementIterator();
                    while (sampleIterator.hasNext()) {
                        Element sampleElement = sampleIterator.next();
                        Map<String, String> sample = new HashMap<>();
                        sample.put("input", sampleElement.elementText("input"));
                        sample.put("output", sampleElement.elementText("output"));
                        samples.add(sample);
                    }
                    question.setSamples(samples);
                    questions.add(question);
                }
            }
            exam.setQuestions(questions);

            return exam;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
