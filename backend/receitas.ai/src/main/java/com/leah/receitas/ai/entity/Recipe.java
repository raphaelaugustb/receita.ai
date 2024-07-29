package com.leah.receitas.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "recipe_table")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private final long id;
    private final String recipeName;
    @ElementCollection
    private final List<String> ingredients;
    @ElementCollection
    private final List<String> instructions;
    private final Boolean isPrivate;

}
