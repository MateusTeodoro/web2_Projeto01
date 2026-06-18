package br.edu.iftm.prjweb2.service;

import java.util.List;
import br.edu.iftm.prjweb2.model.Categoria;

public interface CategoriaService {
    
    List <Categoria> getAllCategorias();

    void saveCategoria(Categoria categoria);

    Categoria getCategoriaById(long id);

    void deleteCategoriaById(long id);
}
