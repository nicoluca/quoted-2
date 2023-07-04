package org.nico.quoted.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Source source;

}
