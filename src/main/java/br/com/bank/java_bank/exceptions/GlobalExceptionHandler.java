package br.com.bank.java_bank.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import br.com.bank.java_bank.domain.DTO.ErrorResponse;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoFundsEnoughException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNoFunds(NoFundsEnoughException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), exchange);
    }

    @ExceptionHandler(AuthenticationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAuthentication(AuthenticationException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), exchange);
    }

    @ExceptionHandler(UnauthorizatedAccessException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUnauthorizatedAccess(UnauthorizatedAccessException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), exchange);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAccountNotFound(AccountNotFoundException ex,
            ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), exchange);
    }

    @ExceptionHandler(InvestmentNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInvestmentNotFound(InvestmentNotFoundException ex,
            ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), exchange);
    }

    @ExceptionHandler(AccountWithInvestmentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAccountWithInvestment(AccountWithInvestmentException ex,
            ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), exchange);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUserNotFound(UserNotFoundException ex,
            ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), exchange);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationError(MethodArgumentNotValidException ex,
            ServerWebExchange exchange) {
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMsg, exchange);
    }
    
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUnexpectedError(Exception ex, ServerWebExchange exchange) {
        ex.printStackTrace(); // log interno
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado", exchange);
    }

    private Mono<ResponseEntity<ErrorResponse>> buildErrorResponse(HttpStatus status, String message, ServerWebExchange exchange) {
        ErrorResponse error = new ErrorResponse(
            status.value(),
            status.getReasonPhrase(),
            message,
            exchange.getRequest().getPath().value(),
            LocalDateTime.now()
        );
        return Mono.just(ResponseEntity.status(status).body(error));
    }
}
