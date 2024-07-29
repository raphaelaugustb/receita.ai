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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private  UUID id;
    @Column(unique = true, nullable = false)
    private  String username;
    private  String password;
    @Column(unique = true, nullable = false)
    private  String email;
    @OneToMany
    private  List<Recipe> recipeList;
    @OneToMany
    private  List<Recipe> tempRecipeList;
    @OneToMany
    private  List<Recipe> savedRecipeList;
}
