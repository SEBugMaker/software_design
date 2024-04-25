package org.example.fileReader;

import org.example.fileReader.readerSet.AnswerReader;
import org.example.fileReader.readerSet.JsonAnswerReader;

public class JsonAnswerReaderFactory extends abstractAnswerReaderFactory{
    @Override
    public AnswerReader createAnswerReader() {
        return new JsonAnswerReader();
    }
}
