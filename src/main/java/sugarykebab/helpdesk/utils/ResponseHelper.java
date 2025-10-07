package sugarykebab.helpdesk.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class ResponseHelper {

    // --------------- SINGLE ENTITY ---------------
    public static <T> ResponseEntity<?> respondSingle(T entity, String notFoundMessage) {
        if (entity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", notFoundMessage));
        }
        return ResponseEntity.ok(entity);
    }

    // --------------- LIST OF ENTITIES ---------------
    public static <T> ResponseEntity<?> respondList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return ResponseEntity.ok(List.of()); // return empty array
        }
        return ResponseEntity.ok(list);
    }

    // --------------- CREATED ENTITY ---------------
    public static <T> ResponseEntity<?> respondCreated(T entity, String failureMessage) {
        if (entity == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", failureMessage));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    // --------------- UPDATED ENTITY ---------------
    public static <T> ResponseEntity<?> respondUpdated(T entity, String notFoundMessage) {
        if (entity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", notFoundMessage));
        }
        return ResponseEntity.ok(entity);
    }

    // --------------- DELETED ENTITY ---------------
    public static ResponseEntity<?> respondDeleted(boolean deleted, String notFoundMessage) {
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", notFoundMessage));
        }
        return ResponseEntity.noContent().build(); // 204
    }

    // --------------- GENERAL ERROR ---------------
    public static ResponseEntity<?> respondError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", message));
    }

    // --------------- BAD REQUEST ---------------
    public static ResponseEntity<?> respondBadRequest(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", message));
    }
}
