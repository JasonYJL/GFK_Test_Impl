package posmy.interview.boot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import posmy.interview.boot.dto.ApiExceptionResponseDto;
import posmy.interview.boot.exception.BookNotAvailableException;
import posmy.interview.boot.exception.NonMemberRemoveException;
import posmy.interview.boot.exception.RecordNotFoundException;

@RestControllerAdvice
public class ApiExceptionAdvice {
    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ApiExceptionResponseDto> handleBookNotAvailableException(BookNotAvailableException exception) {
        return ResponseEntity
                .status(605)
                .body(generateApiExceptionResponseDto(exception.getMessage(), "ERR605"));
    }

    @ExceptionHandler(NonMemberRemoveException.class)
    public ResponseEntity<ApiExceptionResponseDto> handleNonMemberRemoveException(NonMemberRemoveException exception) {
        return ResponseEntity
                .status(606)
                .body(generateApiExceptionResponseDto(exception.getMessage(), "ERR606"));
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ApiExceptionResponseDto> handleRecordNotFoundException(RecordNotFoundException exception) {
        return ResponseEntity
                .status(607)
                .body(generateApiExceptionResponseDto(exception.getMessage(), "ERR607"));
    }

    private ApiExceptionResponseDto generateApiExceptionResponseDto(String message, String code) {
        return ApiExceptionResponseDto.builder()
                .message(message)
                .code(code)
                .build();
    }

}
