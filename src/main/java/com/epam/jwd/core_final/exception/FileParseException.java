package com.epam.jwd.core_final.exception;

public class FileParseException extends RuntimeException {
    private String fileName;
    public FileParseException(String fileName) {
        super();
        this.fileName = fileName;
    }

    @Override
    public String getMessage() {
        return "An exception while parsing the file: " + fileName;
    }
}
