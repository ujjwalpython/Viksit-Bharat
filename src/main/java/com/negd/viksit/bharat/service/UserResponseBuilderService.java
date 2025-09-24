package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.enums.UserServiceErrorCodeEnum;
import com.negd.viksit.bharat.model.ErrorMessage;
import com.negd.viksit.bharat.model.FieldErrorMessage;
import com.negd.viksit.bharat.model.SuccessMessage;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Stream;


@Service
public class UserResponseBuilderService {

    private final Environment environment;


    public UserResponseBuilderService(Environment environment) {
        this.environment = environment;
    }


    public ErrorMessage.ErrorMessageBuilder baseErrorMessage(String errorCode, String message, String correlationId) {
        return ErrorMessage.builder().errorCode(errorCode).message(message).correlationId(correlationId);
    }


    public ErrorMessage detailedErrorMessage(String errorCode, String message, Throwable exception,
                                             String correlationId) {
        // Adjusts the level of detail based on the environment
        String details = isDevEnvironment() ? Arrays.toString(exception.getStackTrace())
                : UserServiceErrorCodeEnum.UNEXPECTED_ERROR.getMessage();
        return baseErrorMessage(errorCode, message, correlationId).details(details).build();
    }


    public ErrorMessage detailedErrorMessage(String errorCode, String message, String detailedMessage,
                                             String correlationId) {
        String details = isDevEnvironment() && detailedMessage != null ? detailedMessage
                : UserServiceErrorCodeEnum.UNEXPECTED_ERROR.getMessage();
        return baseErrorMessage(errorCode, message, correlationId).details(details).build();
    }


    public FieldErrorMessage.FieldErrorMessageBuilder baseFieldErrorMessage(String errorCode, String message,
                                                                            String correlationId) {
        return FieldErrorMessage.builder().errorCode(errorCode).message(message).correlationId(correlationId);
    }


    public FieldErrorMessage detailedFieldErrorMessage(String errorCode, String message, Throwable exception,
                                                       String correlationId) {
        String details = isDevEnvironment() ? Arrays.toString(exception.getStackTrace())
                : UserServiceErrorCodeEnum.UNEXPECTED_ERROR.getMessage();
        return baseFieldErrorMessage(errorCode, message, correlationId).details(details).build();
    }


    public FieldErrorMessage detailedFieldErrorMessage(String errorCode, String message, String detailedMessage,
                                                       String correlationId) {
        String details = isDevEnvironment() && detailedMessage != null ? detailedMessage
                : UserServiceErrorCodeEnum.UNEXPECTED_ERROR.getMessage();
        return baseFieldErrorMessage(errorCode, message, correlationId).details(details).build();
    }

    public <T> SuccessMessage<T> baseSuccessMessage(T data, String message, String correlationId) {
        return SuccessMessage.<T>builder().data(data).message(message).correlationId(correlationId).build();
    }

    /**
     * Constructs the basic structure of a success message.
     *
     * @param message       The success message to be displayed.
     * @param correlationId A unique identifier for the request to help correlate logs and
     *                      responses.
     * @param <T>           The type of the data included in the success message.
     * @return A fully constructed UserServiceSuccessResponse with provided details.
     */
    public <T> SuccessMessage<T> baseSuccessMessage(String message, String correlationId) {
        return SuccessMessage.<T>builder().message(message).correlationId(correlationId).build();
    }

    /**
     * Generates a detailed success message. If in the development environment and a
     * detailed message is provided, it includes that; otherwise, it includes a standard
     * success message.
     *
     * @param data            The payload associated with the success response.
     * @param message         The success message to be displayed.
     * @param detailedMessage The detailed success message for development environments.
     * @param correlationId   A unique identifier for the request to help correlate logs and
     *                        responses.
     * @param <T>             The type of the data included in the success message.
     * @return A fully constructed UserServiceSuccessResponse with detailed information
     * when applicable.
     */
    public <T> SuccessMessage<T> detailedSuccessMessage(T data, String message, String detailedMessage,
                                                        String correlationId) {
        String details = isDevEnvironment() && detailedMessage != null ? detailedMessage : message;
        return baseSuccessMessage(data, details, correlationId);
    }

    /**
     * Determines if the application is currently running in a development environment.
     *
     * @return true if in the development environment, otherwise false.
     */
    private boolean isDevEnvironment() {
        return Stream.of(environment.getActiveProfiles())
                .anyMatch(profile -> profile.equals("dev") || profile.equals("local") || profile.equals("docker"));

    }

}
