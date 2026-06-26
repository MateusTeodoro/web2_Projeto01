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

import br.edu.iftm.prjweb2.model.Material;
import br.edu.iftm.prjweb2.service.MaterialService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/material")
public class MaterialController {
    
    @Autowired
    private MaterialService materialService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("materials", materialService.getAllMaterials());
        return "material/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("material", new Material());
        return "material/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute @Valid Material material, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("material", material);
            return "material/create";
        }

        materialService.saveMaterial(material);
        return "redirect:/material";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.materialService.deleteMaterialById(id);
        return "redirect:/material";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Material material = materialService.getMaterialById(id);
        model.addAttribute("material", material);
        return "material/edit";
    }
}
