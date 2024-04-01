package org.example.fileReader;

public class ExamReaderFactory {
    private JsonExamReader jsonExamReader;

    private XmlExamReader xmlExamReader;

    public ExamReader createExamReader(String fileExtension) {
        if ("json".equals(fileExtension)) {
            jsonExamReader = new JsonExamReader();
            return jsonExamReader;
        } else if ("xml".equals(fileExtension)) {
            xmlExamReader = new XmlExamReader();
            return xmlExamReader;
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileExtension);
        }
    }
}
