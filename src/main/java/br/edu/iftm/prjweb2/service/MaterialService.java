package br.edu.iftm.prjweb2.service;

import java.util.List;

import br.edu.iftm.prjweb2.model.Material;

public interface MaterialService {
    
    List <Material> getAllMaterials();

    void saveMaterial(Material Material);

    Material getMaterialById(long id);

    void deleteMaterialById(long id);
}
