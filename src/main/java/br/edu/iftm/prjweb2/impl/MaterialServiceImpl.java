package br.edu.iftm.prjweb2.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.iftm.prjweb2.model.Material;
import br.edu.iftm.prjweb2.repository.MaterialRepository;
import br.edu.iftm.prjweb2.service.MaterialService;

@Service
public class MaterialServiceImpl implements MaterialService {
    
    @Autowired
    private MaterialRepository materialRepository;

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Override
    public void saveMaterial(Material material) {
        this.materialRepository.save(material);
    }

    @Override
    public Material getMaterialById(long id) {
        Optional<Material> optional = materialRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Material not found with id: " + id);
        }
    }

    @Override
    public void deleteMaterialById(long id) {
        this.materialRepository.deleteById(id);
    }
}
