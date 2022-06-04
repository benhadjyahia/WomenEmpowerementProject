package tn.esprit.spring.exceptions;

import tn.esprit.spring.entities.HTTPProtocolResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionProcessing implements ErrorController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An  error occurred while processing the request";
    private static final String INCORRECT_CREDENTIALS = "Username / password incorrect. Please try again";
    private static final String ACCOUNT_DISABLED = "Your account has been disabled. If this is an error, please contact administration";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file";
    private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
    public static final String ERROR_PATH = "/error";



    @ExceptionHandler(PasswordValidException.class)
    public ResponseEntity<HTTPProtocolResponse> passwordValidException(PasswordValidException exception) {
        return createHttpResponse(NOT_ACCEPTABLE, exception.getMessage());
    }
    @ExceptionHandler(PasswordMatchException.class)
    public ResponseEntity<HTTPProtocolResponse> passwordMatchException(PasswordMatchException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HTTPProtocolResponse> accountDisabledException() {
        return createHttpResponse(BAD_REQUEST, ACCOUNT_DISABLED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HTTPProtocolResponse> badCredentialsException() {
        return createHttpResponse(CONFLICT, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HTTPProtocolResponse> accessDeniedException() {
        return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HTTPProtocolResponse> lockedException() {
        return createHttpResponse(UNAUTHORIZED, ACCOUNT_LOCKED);
    }

    @ExceptionHandler(EmailExist.class)
    public ResponseEntity<HTTPProtocolResponse> emailExistException(EmailExist exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
    
    @ExceptionHandler(FriendExist.class)
    public ResponseEntity<HTTPProtocolResponse> friendRequestExistException(FriendExist exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ResetPasswordException.class)
    public ResponseEntity<HTTPProtocolResponse> resetPasswordException(ResetPasswordException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
    
    @ExceptionHandler(ResetPasswordTokenException.class)
    public ResponseEntity<HTTPProtocolResponse> resetPasswordTokenException(ResetPasswordTokenException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
   @ExceptionHandler(UsernameExist.class)
    public ResponseEntity<HTTPProtocolResponse> usernameExistException(UsernameExist exception) {
        return createHttpResponse(CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(EmailNotExist.class)
    public ResponseEntity<HTTPProtocolResponse> emailNotFoundException(EmailNotExist exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UsernameNotExist.class)
    public ResponseEntity<HTTPProtocolResponse> userNotFoundException(UsernameNotExist exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
    
    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<HTTPProtocolResponse> accountLockedException(AccountLockedException exception) {
        return createHttpResponse(LOCKED, exception.getMessage());
    }



    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HTTPProtocolResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HTTPProtocolResponse> internalServerErrorException(Exception exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }


    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HTTPProtocolResponse> notFoundException(NoResultException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(NOT_FOUND, exception.getMessage());
    }
    @ExceptionHandler(CourseNotExist.class)
    public ResponseEntity<HTTPProtocolResponse> courseNotFoundException(CourseNotExist exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(NOT_FOUND, exception.getMessage());
    }
    @ExceptionHandler(CoursesLimitReached.class)
    public ResponseEntity<HTTPProtocolResponse> CoursesLimit(CoursesLimitReached exception) {
        return createHttpResponse(FORBIDDEN, exception.getMessage());
    }
    @ExceptionHandler(CourseOwnerShip.class)
    public ResponseEntity<HTTPProtocolResponse> CourseOwnerShip(CourseOwnerShip exception) {
        return createHttpResponse(FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HTTPProtocolResponse> iOException(IOException exception) { 
        LOGGER.error(exception.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

    private ResponseEntity<HTTPProtocolResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HTTPProtocolResponse(httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<HTTPProtocolResponse> notFound404() {
        return createHttpResponse(NOT_FOUND, "There is no mapping for this URL");
    }
 

}
