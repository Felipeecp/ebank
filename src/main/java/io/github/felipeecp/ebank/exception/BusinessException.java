package io.github.felipeecp.ebank.exception;

public class BusinessException extends Throwable {
    public BusinessException(String error) {
        super(error);
    }
}
