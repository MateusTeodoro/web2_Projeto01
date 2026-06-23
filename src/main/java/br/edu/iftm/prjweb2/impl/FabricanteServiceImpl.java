package br.edu.iftm.prjweb2.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.iftm.prjweb2.model.Fabricante;
import br.edu.iftm.prjweb2.repository.FabricanteRepository;
import br.edu.iftm.prjweb2.service.FabricanteService;

@Service
public class FabricanteServiceImpl implements FabricanteService {
    
    @Autowired
    private FabricanteRepository fabricanteRepository;

    @Override
    public List<Fabricante> getAllFabricantes() {
        return fabricanteRepository.findAll();
    }

    @Override
    public void saveFabricante(Fabricante fabricante) {
        this.fabricanteRepository.save(fabricante);
    }

    @Override
    public Fabricante getFabricanteById(long id) {
        Optional<Fabricante> optional = fabricanteRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Fabricante not found with id: " + id);
        }
    }

    @Override
    public void deleteFabricanteById(long id) {
        this.fabricanteRepository.deleteById(id);
    }
}
