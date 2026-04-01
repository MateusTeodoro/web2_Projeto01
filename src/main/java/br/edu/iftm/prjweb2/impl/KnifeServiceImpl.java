package br.edu.iftm.prjweb2.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.iftm.prjweb2.model.Knife;
import br.edu.iftm.prjweb2.repository.KnifeRepository;
import br.edu.iftm.prjweb2.service.KnifeService;

@Service
public class KnifeServiceImpl implements KnifeService {

    @Autowired
    private KnifeRepository knifeRepository;

    @Override
    public List<Knife> getAllKnifes() {
        return knifeRepository.findAll();
    }

    @Override
    public void saveKnife(Knife knife) {
        this.knifeRepository.save(knife);
    }

    @Override
    public Knife getKnifeById(long id) {
        Optional<Knife> optional = knifeRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Knife not found with id: " + id);
        }
    }

    @Override
    public void deleteKnifeById(long id) {
        this.knifeRepository.deleteById(id);
    }
}
