package ozdemir0ozdemir.nirobank.client.userclient;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private T object;
    private Instant respondAt;
    private String status;
    private String message;

    public Response(T object, Instant respondAt, String status, String message) {
        this.object = object;
        this.respondAt = respondAt;
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public T getObject() {
        return object;
    }

    public Instant getRespondAt() {
        return respondAt;
    }

    public String getStatus() {
        return status;
    }

    public static <T> Response<T> found(T object) {
        return new Response<T>(object, Instant.now(), "succeeded", "Found");
    }

    public static <T> Response<T> notFound() {
        return new Response<T>(null, Instant.now(), "failed", "Not Found");
    }
}
