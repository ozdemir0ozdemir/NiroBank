package ozdemir0ozdemir.common.response;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private T payload;
    private Instant respondAt;
    private ResponseStatus status;
    private String message;

    // Constructors
    public Response() {}

    public Response(T payload, Instant respondAt, ResponseStatus status, String message) {
        this.payload = payload;
        this.respondAt = respondAt;
        this.status = status;
        this.message = message;
    }

    // Static Factories
    public static <T> Response<T> succeeded(T payload, String message) {
        return new Response<>(payload, Instant.now(), ResponseStatus.SUCCEEDED, message);
    }

    public static <T> Response<T> succeeded(String message) {
        return new Response<>(null, Instant.now(), ResponseStatus.SUCCEEDED, message);
    }

    public static <T> Response<T> failed(T payload, String message) {
        return new Response<>(payload, Instant.now(), ResponseStatus.FAILED, message);
    }

    public static <T> Response<T> failed(String message) {
        return new Response<>(null, Instant.now(), ResponseStatus.FAILED, message);
    }

    public static <T> Response<T> of(T payload, ResponseStatus status, String message) {
        return new Response<>(payload, Instant.now(), status, message);
    }

    public static <T> Response<T> of(T payload, Instant respondAt, ResponseStatus status, String message) {
        return new Response<>(payload, respondAt, status, message);
    }

    //  Getters & Setters (Setters chained)
    public String getMessage() {
        return message;
    }

    public T getPayload() {
        return payload;
    }

    public Instant getRespondAt() {
        return respondAt;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Response<T> setPayload(T payload) {
        this.payload = payload;
        return this;
    }

    public Response<T> setRespondAt(Instant respondAt) {
        this.respondAt = respondAt;
        return this;
    }

    public Response<T> setStatus(ResponseStatus status) {
        this.status = status;
        return this;
    }
}
