package br.edu.iftm.prjweb2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;

import br.edu.iftm.prjweb2.model.Categoria;
import br.edu.iftm.prjweb2.service.CategoriaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("categorias", categoriaService.getAllCategorias());
        return "categoria/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute @Valid Categoria categoria, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categoria", categoria);
            return "categoria/create";
        }

        categoriaService.saveCategoria(categoria);
        return "redirect:/categoria";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.categoriaService.deleteCategoriaById(id);
        return "redirect:/categoria";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.getCategoriaById(id);
        model.addAttribute("categoria", categoria);
        return "categoria/edit";
    }
}
