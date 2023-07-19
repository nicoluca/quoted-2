package org.nico.quoted.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;


@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    //@GenericGenerator(name = "uuid")
    @Column(name = "id")
    private UUID id;

    @Column(name = "email")
    @NonNull
    private String email;
}
