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

import br.edu.iftm.prjweb2.model.Fabricante;
import br.edu.iftm.prjweb2.service.FabricanteService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/fabricante")
public class FabricanteController {
    
    @Autowired
    private FabricanteService fabricanteService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("fabricantes", fabricanteService.getAllFabricantes());
        return "fabricante/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("fabricante", new Fabricante());
        return "fabricante/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute @Valid Fabricante fabricante, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("fabricante", fabricante);
            return "fabricante/create";
        }

        fabricanteService.saveFabricante(fabricante);
        return "redirect:/fabricante";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.fabricanteService.deleteFabricanteById(id);
        return "redirect:/fabricante";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Fabricante fabricante = fabricanteService.getFabricanteById(id);
        model.addAttribute("fabricante", fabricante);
        return "fabricante/edit";
    }
}
