package br.edu.iftm.prjweb2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.iftm.prjweb2.model.Fabricante;

@Repository
public interface FabricanteRepository extends JpaRepository<Fabricante, Long>{
    
}
