package org.nico.quoted.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sources")
@Builder // For testing
@AllArgsConstructor // For testing
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    @NonNull
    private String name;

    @OneToMany(mappedBy = "source", cascade = CascadeType.MERGE)
    @JsonIgnore
    private Set<Quote> quotes;
}
