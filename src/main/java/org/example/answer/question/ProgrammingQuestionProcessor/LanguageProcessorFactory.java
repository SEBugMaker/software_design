package org.example.answer.question.ProgrammingQuestionProcessor;

public class LanguageProcessorFactory {
    public LanguageProcessor createProcessor(String language){
        if(language.equals("java")){
            return new JavaLanguageProcessor();
        }else{
            throw new IllegalArgumentException("Invalid language");
        }
    }
}
