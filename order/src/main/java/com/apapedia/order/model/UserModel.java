package com.apapedia.order.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
// @JsonIgnoreProperties(value = { "password","createdAt","updatedAt" })
@Table(name = "user_model")
public class UserModel implements Serializable{
    @Id
    private UUID id = UUID.randomUUID();

    
    @Size(max = 100)
    @Column(name = "name")
    private String name;
    
    
    @Size(max = 100)
    @Column(name = "username")
    private String username;

    
    @Size(max = 100)
    @Column(name = "password")
    private String password;

    
    @Size(max = 100)
    @Column(name = "email")
    private String email;

    @Column(name = "balance")
    private Long balance = (long) 0;

    
    @Size(max = 100)
    @Column(name = "address")
    private String address;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "isDeleted")
    private boolean isDeleted = false;

    @Column(name = "category")
    private String category;

    @Column(name = "id_cart")
    private int idCart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Role role;
}
