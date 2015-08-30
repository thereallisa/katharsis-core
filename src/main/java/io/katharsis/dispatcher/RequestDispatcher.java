package io.katharsis.dispatcher;

import io.katharsis.dispatcher.registry.ControllerRegistry;
import io.katharsis.errorhandling.mapper.ExceptionMapperRegistry;
import io.katharsis.errorhandling.mapper.JsonApiExceptionMapper;
import io.katharsis.queryParams.RequestParams;
import io.katharsis.request.dto.RequestBody;
import io.katharsis.request.path.JsonPath;
import io.katharsis.response.BaseResponse;

import java.util.Optional;

/**
 * A class that can be used to integrate Katharsis with external frameworks like Jersey, Spring etc. See katharsis-rs
 * and katharsis-servlet for usage.
 */
public class RequestDispatcher {

    private final ControllerRegistry controllerRegistry;
    private final ExceptionMapperRegistry exceptionMapperRegistry;

    public RequestDispatcher(ControllerRegistry controllerRegistry, ExceptionMapperRegistry exceptionMapperRegistry) {
        this.controllerRegistry = controllerRegistry;
        this.exceptionMapperRegistry = exceptionMapperRegistry;
    }

    /**
     * Dispatch the request from a client
     * @param jsonPath built {@link JsonPath} instance which represents the URI sent in the request
     * @param requestType type of the request e.g. POST, GET, PATCH
     * @param requestParams built object containing query parameters of the request
     * @param requestBody deserialized body of the client request
     * @return the response form the Katharsis
     * @throws Exception exception thrown while processing the request
     */
    public BaseResponse<?> dispatchRequest(JsonPath jsonPath, String requestType, RequestParams requestParams,
                                           @SuppressWarnings("SameParameterValue") RequestBody requestBody) throws Exception {

        try {
        return controllerRegistry
                .getController(jsonPath, requestType)
                .handle(jsonPath, requestParams, requestBody);
        } catch (Exception e) {
            Optional<JsonApiExceptionMapper> exceptionMapper = exceptionMapperRegistry.findMapperFor(e.getClass());
            if (exceptionMapper.isPresent()) {
                //noinspection unchecked
                return exceptionMapper.get().toErrorResponse(e);
            } else {
                throw e;
            }
        }
    }
}
