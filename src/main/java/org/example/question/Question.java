package org.example.question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

import javax.xml.bind.annotation.XmlSeeAlso;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleChoiceQuestion.class, name = "1"),
        @JsonSubTypes.Type(value = MultipleChoiceQuestion.class, name = "2"),
        @JsonSubTypes.Type(value = ProgrammingQuestion.class, name = "3")
})
public abstract class Question<T> {

    protected int id;
    protected int type;
    protected String question;
    protected int points;

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public int calculateQuestionScore(Integer studentAnswer){
        return 0;
    }
    public int calculateQuestionScore(String studentAnswer){
        return 0;
    }

    public int calculateQuestionScore(Object answer){
        return 0;
    }


    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", type=" + type +
                ", question='" + question + '\'' +
                ", points=" + points +
                '}';
    }
}