package br.edu.iftm.prjweb2.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.iftm.prjweb2.model.Categoria;
import br.edu.iftm.prjweb2.repository.CategoriaRepository;
import br.edu.iftm.prjweb2.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public void saveCategoria(Categoria categoria) {
        this.categoriaRepository.save(categoria);
    }

    @Override
    public Categoria getCategoriaById(long id) {
        Optional<Categoria> optional = categoriaRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Category not found with id: " + id);
        }
    }

    @Override
    public void deleteCategoriaById(long id) {
        this.categoriaRepository.deleteById(id);
    }
}
