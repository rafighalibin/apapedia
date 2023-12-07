package com.apapedia.order.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties(value = { "password","createdAt","updatedAt" })
@Table(name = "user_apapedia")
public class User {
    @Id
    private UUID id = UUID.randomUUID();

    @NotNull
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull
    @Size(max = 100)
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Size(max = 100)
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Size(max = 100)
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "balance")
    private Long balance = (long) 0;

    @NotNull
    @Size(max = 100)
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // @Transient
    private String role;

    // @Transient
    private boolean isDeleted = false;

}
