package org.example.fileReader;

public class AnswerReaderFactory {
    private JsonAnswerReader jsonAnswerReader;

    public AnswerReaderFactory() {
        this.jsonAnswerReader = new JsonAnswerReader();
    }

    public AnswerReader createAnswerReader(String fileType) {
        if ("json".equalsIgnoreCase(fileType)) {
            return jsonAnswerReader;
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }
}
