package com.auspify_tech.online_exam_system.common.exception;

    public class ResourceAlreadyExistsException extends ConflictException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
