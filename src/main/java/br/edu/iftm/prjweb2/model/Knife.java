package br.edu.iftm.prjweb2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "knives")
public class Knife {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 50, message= "Nome deve conter pelo menos 3 caracteres")
    @NotBlank(message= "Nome é um campo obrigatório!")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull(message= "Tamanho é um campo obrigatório!")
    @Min(value = 0, message = "O tamanho não pode ser negativo")
    @Column(name = "size", nullable = false)
    private Float size;

    @NotNull(message= "Ano é um campo obrigatório!")
    @Column(name = "year", nullable = false)
    private Integer year;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getSize(){
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
