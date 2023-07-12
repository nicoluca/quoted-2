package org.nico.quoted.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "text", columnDefinition = "TEXT")
    @NonNull
    private String text;

    @Column(name = "datetime_created", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @NonNull
    @CreationTimestamp
    private Timestamp datetimeCreated;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Source source;

}
