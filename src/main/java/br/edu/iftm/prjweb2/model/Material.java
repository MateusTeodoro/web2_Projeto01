package br.edu.iftm.prjweb2.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "materiais")
public class Material {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 50, message= "Nome deve conter pelo menos 3 caracteres")
    @NotBlank(message= "Nome é um campo obrigatório!")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotBlank(message = "O tipo de uso (Cabo, Lâmina, etc.) é obrigatório.")
    private String tipoUso;

    @ManyToMany(mappedBy = "materiais")
    private List<Knife> knives = new ArrayList<>(); //Iniciando a lista para evitar erro de NullPointer
}
