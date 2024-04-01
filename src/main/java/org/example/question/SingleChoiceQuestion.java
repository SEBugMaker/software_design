package org.example.question;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

public class SingleChoiceQuestion extends Question{
    private List<String> options;
    private int answer;

    public SingleChoiceQuestion(){
        this.setType(1);
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }


    @Override
    public int calculateQuestionScore(Integer studentAnswer) {
        if(studentAnswer == answer)
            return points;
        else
            return 0;
    }

    @Override
    public String toString() {
        return "SingleChoiceQuestion{" +
                "id=" + id +
                " type=" + type +
                " question='" + question + '\'' +
                " points=" + points +
                " options=" + options +
                " answer=" + answer +
                "}\n";
    }
}
