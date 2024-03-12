package ru.wwerlosh.vktestcase.configuration.auditing;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;

    private String endpoint;

    private String status;

    private String username;

    private String ip;

    private LocalDateTime timestamp;

    public Audit(LocalDateTime timestamp, String username, String endpoint, String status) {
        this.timestamp = timestamp;
        this.username = username;
        this.endpoint = endpoint;
        this.status = status;
    }
}
