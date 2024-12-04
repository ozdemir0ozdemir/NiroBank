package ozdemir0ozdemir.userservice.bridge;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> list;
    private int currentPage;
    private int totalPages;
    private long totalItem;
    private int itemPerPage;
    private boolean hasPrevious;
    private boolean hasNext;
    private Instant respondAt;
    private String status;
    private String message;

    public static <T> PagedResponse<T> of(Page<T> userPage) {
        return PagedResponse.of(userPage, "succeeded", "request succeeded");
    }


    public static <T> PagedResponse<T> of(Page<T> userPage, String status, String message) {
        return new PagedResponse<>(
                userPage.getContent(),
                userPage.getNumber() + 1,
                userPage.getTotalPages(),
                userPage.getTotalElements(),
                userPage.getSize(),
                userPage.hasPrevious(),
                userPage.hasNext(),
                Instant.now(),
                status,
                message
        );
    }
}
