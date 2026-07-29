package com.codebyisaac.mapi.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false, length=100)
    private String email;

    @Column(nullable = false, length=40)
    private String password;

    @OneToMany(mappedBy ="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}
