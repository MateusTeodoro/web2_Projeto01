package br.edu.iftm.prjweb2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;

import br.edu.iftm.prjweb2.model.Categoria;
import br.edu.iftm.prjweb2.service.CategoriaService;
import jakarta.validation.Valid;

@Controller
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/categoria")
    public String index(Model model) {
        model.addAttribute("categoriaList", categoriaService.getAllCategorias());
        return "categoria/index";
    }

    @GetMapping("/categoria/create")
    public String create(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria/create";
    }

    @PostMapping("/categoria/save")
    public String save(@ModelAttribute @Valid Categoria categoria, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categoria", categoria);
            return "categoria/create";
        }

        categoriaService.saveCategoria(categoria);
        return "redirect:/categoria";
    }

    @GetMapping("/categoria/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.categoriaService.deleteCategoriaById(id);
        return "redirect:/categoria";
    }

    @GetMapping("/categoria/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.getCategoriaById(id);
        model.addAttribute("categoria", categoria);
        return "categoria/edit";
    }
}
