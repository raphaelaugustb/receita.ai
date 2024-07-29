package com.leah.receitas.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "recipe_table")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  long id;
    private  String recipeName;
    @ElementCollection
    private  List<String> ingredients;
    @ElementCollection
    private  List<String> instructions;
    private  Boolean isPrivate;
    private  String usernameRecipe;
}
