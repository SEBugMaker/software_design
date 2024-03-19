package org.example.question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "scoreMode") // 指定默认的子类类型)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MultipleChoiceQuestionFix.class, name = "fix"),
        @JsonSubTypes.Type(value = MultipleChoiceQuestionPartial.class, name = "partial"),
        @JsonSubTypes.Type(value = MultipleChoiceQuestionNothing.class, name = "nothing")
})
public class MultipleChoiceQuestion extends Question {
    private String scoreMode;
    private List<String> options;
    private List<Integer> answer;

    public MultipleChoiceQuestion() {
        this.setType(2);
    }



    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public String getScoreMode() {
        return scoreMode;
    }

    public void setScoreMode(String scoreMode) {
        this.scoreMode = scoreMode;
    }
    @Override
    public int calculateQuestionScore(Object answer){
        return 0;
    }
    @Override
    public String toString() {
        return "MultipleChoiceQuestion{" +
                "options=" + options +
                ", answer=" + answer +
                ", scoreMode='" + scoreMode + '\'' +
                ", id=" + id +
                ", type=" + type +
                ", question='" + question + '\'' +
                ", points=" + points +
                "}\n";
    }

}
