package ozdemir0ozdemir.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"payload, respondAt, status, message, currentPage, totalPages, itemPerPage, totalItem, hasPrevious, hasNext"})
public class PagedResponse<T> {
    private List<T> payload;
    private Instant respondAt;
    private ResponseStatus status;
    private String message;

    private int currentPage;
    private int totalPages;
    private int itemPerPage;
    private long totalItem;
    private boolean hasPrevious;
    private boolean hasNext;

    // Constructors
    public PagedResponse() {}

    public PagedResponse(
            List<T> payload,
            Instant respondAt,
            ResponseStatus status,
            String message,
            int currentPage,
            int totalPages,
            int itemPerPage,
            long totalItem,
            boolean hasPrevious,
            boolean hasNext) {

        this.currentPage = currentPage;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.itemPerPage = itemPerPage;
        this.payload = payload;
        this.message = message;
        this.respondAt = respondAt;
        this.status = status;
        this.totalItem = totalItem;
        this.totalPages = totalPages;
    }


    // Static Factories
    public static <T> PagedResponse<T> succeeded(Page<T> userPage, String message) {
        return PagedResponse.of(userPage, Instant.now(), ResponseStatus.SUCCEEDED, message);
    }

    public static <T> PagedResponse<T> failed(Page<T> userPage, String message) {
        return PagedResponse.of(userPage, Instant.now(), ResponseStatus.FAILED, message);
    }

    public static <T> PagedResponse<T> of(Page<T> userPage, ResponseStatus status, String message) {
        return PagedResponse.of(userPage, Instant.now(), status, message);
    }

    public static <T> PagedResponse<T> of(Page<T> userPage, Instant respondAt, ResponseStatus status, String message) {
        return new PagedResponse<>(
                userPage.getContent(),
                respondAt,
                status,
                message,
                userPage.getNumber() + 1,
                userPage.getTotalPages(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.hasPrevious(),
                userPage.hasNext()
        );
    }

    //  Getters & Setters (Setters chained)

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getPayload() {
        return payload;
    }

    public Instant getRespondAt() {
        return respondAt;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public long getTotalItem() {
        return totalItem;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public PagedResponse<T> setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public PagedResponse<T> setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
        return this;
    }

    public PagedResponse<T> setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
        return this;
    }

    public PagedResponse<T> setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
        return this;
    }

    public PagedResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public PagedResponse<T> setPayload(List<T> payload) {
        this.payload = payload;
        return this;
    }

    public PagedResponse<T> setRespondAt(Instant respondAt) {
        this.respondAt = respondAt;
        return this;
    }

    public PagedResponse<T> setStatus(ResponseStatus status) {
        this.status = status;
        return this;
    }

    public PagedResponse<T> setTotalItem(long totalItem) {
        this.totalItem = totalItem;
        return this;
    }

    public PagedResponse<T> setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }
}
