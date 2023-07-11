package org.nico.quoted.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sources")
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long id;

    @Column(name = "name")
    @NonNull
    private String name;

    @OneToMany(mappedBy = "source", cascade = CascadeType.MERGE)
    @JsonIgnore
    private Set<Quote> quotes;
}
