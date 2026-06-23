package br.edu.iftm.prjweb2.service;

import java.util.List;

import br.edu.iftm.prjweb2.model.Fabricante;

public interface FabricanteService {
    
    List <Fabricante> getAllFabricantes();

    void saveFabricante(Fabricante fabricante);

    Fabricante getFabricanteById(long id);

    void deleteFabricanteById(long id);
}
