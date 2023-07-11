package org.nico.quoted.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long id;

    @Column(name = "text", columnDefinition = "TEXT")
    @NonNull
    private String text;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Source source;

}
