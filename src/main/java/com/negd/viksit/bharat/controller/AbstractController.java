package com.negd.viksit.bharat.controller;



import com.negd.viksit.bharat.model.SuccessMessage;
import com.negd.viksit.bharat.service.UserResponseBuilderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public abstract class AbstractController {

    protected final UserResponseBuilderService userResponseService;


    protected AbstractController(UserResponseBuilderService userResponseService) {
        this.userResponseService = userResponseService;
    }


    protected <T> ResponseEntity<SuccessMessage<T>> sendSuccessResponse(T data, String correlationId) {
        return sendSuccessResponse(data, HttpStatus.OK.getReasonPhrase(), correlationId, HttpStatus.OK);
    }


    protected <T> ResponseEntity<SuccessMessage<T>> sendSuccessResponse(T data, String message, String correlationId,
                                                                        HttpStatus status) {
        SuccessMessage<T> successResponse = userResponseService.baseSuccessMessage(data, message, correlationId);
        return new ResponseEntity<>(successResponse, status);
    }


    protected ResponseEntity<SuccessMessage<Object>> sendSuccessResponse(String correlationId) {
        return sendSuccessResponse(null, HttpStatus.OK.getReasonPhrase(), correlationId, HttpStatus.OK);
    }


    protected ResponseEntity<SuccessMessage<Object>> sendSuccessResponse(String message, String correlationId,
                                                                         HttpStatus status) {
        return sendSuccessResponse(null, message, correlationId, status);
    }

}
