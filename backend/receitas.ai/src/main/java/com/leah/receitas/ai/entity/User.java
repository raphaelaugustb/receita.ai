package com.leah.receitas.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private final UUID id;
    @Column(unique = true, nullable = false)
    private final String username;
    private final String password;
    @Column(unique = true, nullable = false)
    private final String email;
    @OneToMany
    private final List<Recipe> recipeList;
    @OneToMany
    private final List<Recipe> tempRecipeList;
    @OneToMany
    private final List<Recipe> savedRecipeList;
}
