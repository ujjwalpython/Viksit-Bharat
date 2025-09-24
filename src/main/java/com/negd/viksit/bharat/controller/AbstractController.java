package com.negd.viksit.bharat.controller;



import com.negd.viksit.bharat.model.SuccessMessage;
import com.negd.viksit.bharat.service.UserResponseBuilderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Abstract base controller that provides common response handling methods for all
 * controllers extending it. Ensures consistent response structures across the
 * application.
 */
@RestController
public abstract class AbstractController {

    protected final UserResponseBuilderService userResponseService;

    /**
     * Constructor for injecting the user response service.
     * @param userResponseService Service to construct user-specific responses.
     */
    protected AbstractController(UserResponseBuilderService userResponseService) {
        this.userResponseService = userResponseService;
    }

    /**
     * Sends a standardized success response with default message and HTTP status.
     * @param data The data to include in the success response.
     * @param correlationId Unique identifier for the request to aid in tracking.
     * @param <T> The type of the data.
     * @return ResponseEntity with success message and data.
     */
    protected <T> ResponseEntity<SuccessMessage<T>> sendSuccessResponse(T data, String correlationId) {
        return sendSuccessResponse(data, HttpStatus.OK.getReasonPhrase(), correlationId, HttpStatus.OK);
    }

    /**
     * Sends a success response with customizable message and HTTP status.
     * @param data The data to include in the success response.
     * @param message The success message to be displayed.
     * @param correlationId Unique identifier for the request to aid in tracking.
     * @param status HTTP status to return.
     * @param <T> The type of the data.
     * @return ResponseEntity with success message and data.
     */
    protected <T> ResponseEntity<SuccessMessage<T>> sendSuccessResponse(T data, String message, String correlationId,
                                                                        HttpStatus status) {
        SuccessMessage<T> successResponse = userResponseService.baseSuccessMessage(data, message, correlationId);
        return new ResponseEntity<>(successResponse, status);
    }

    /**
     * Sends a standardized success response for operations without specific return data.
     * @param correlationId Unique identifier for the request to aid in tracking.
     * @return ResponseEntity with default success message.
     */
    protected ResponseEntity<SuccessMessage<Object>> sendSuccessResponse(String correlationId) {
        return sendSuccessResponse(null, HttpStatus.OK.getReasonPhrase(), correlationId, HttpStatus.OK);
    }

    /**
     * Sends a success response for operations without specific return data but with a
     * custom message and status.
     * @param message The success message to be displayed.
     * @param correlationId Unique identifier for the request to aid in tracking.
     * @param status HTTP status to return.
     * @return ResponseEntity with success message.
     */
    protected ResponseEntity<SuccessMessage<Object>> sendSuccessResponse(String message, String correlationId,
                                                                         HttpStatus status) {
        return sendSuccessResponse(null, message, correlationId, status);
    }

    /**
     * Sends a paginated response, typically used for list endpoints.
     * @param pageData The Page object containing data and pagination information.
     * @param correlationId Unique identifier for the request to aid in tracking.
     * @param <T> The type of data in the Page.
     * @return ResponseEntity with paginated data.
     */
    protected <T> ResponseEntity<PageableResponseDto<T>> sendPaginatedResponse(Page<T> pageData, String correlationId) {
        PageableResponseDto<T> paginatedResponse = new PageableResponseDto<>(pageData.getContent(),
                pageData.getNumber(), pageData.getSize(), pageData.getTotalElements(), pageData.getTotalPages(),
                correlationId);

        return ResponseEntity.ok(paginatedResponse);
    }

    /**
     * Sends a paginated response with the given page data and correlation ID.
     * @param paginatedResponse The paginated response data to send.
     * @param correlationId Unique identifier for the request to aid in tracking.
     * @param <T> The type of data in the Page.
     * @return ResponseEntity containing the paginated data.
     */
    protected <T> ResponseEntity<PageableResponseDto<T>> sendPaginatedResponse(PageableResponseDto<T> paginatedResponse,
                                                                               String correlationId) {
        paginatedResponse.setCorrelationId(correlationId);
        return ResponseEntity.ok(paginatedResponse);
    }

}
