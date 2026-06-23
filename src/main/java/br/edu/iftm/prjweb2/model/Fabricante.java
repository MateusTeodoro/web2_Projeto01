package br.edu.iftm.prjweb2.model;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "fabricante")
public class Fabricante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 50, message= "Nome deve conter pelo menos 3 caracteres")
    @NotBlank(message= "Nome é um campo obrigatório!")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "O país de origem é obrigatório.")
    private String origem;

    @NotNull(message = "O ano de fundação é obrigatório.")
    @Min(value = 1, message = "O ano deve ser no mínimo 1.")
    private Integer anoFundacao;

    @OneToMany(mappedBy = "fabricante")
    private List<Knife> knives;

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

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public Integer getAnoFundacao() {
        return anoFundacao;
    }

    public void setAnoFundacao(Integer anoFundacao) {
        this.anoFundacao = anoFundacao;
    }

    public List<Knife> getKnives() {
        return knives;
    }

    public void setKnives(List<Knife> knives) {
        this.knives = knives;
    }
}
