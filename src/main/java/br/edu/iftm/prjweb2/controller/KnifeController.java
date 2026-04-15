package br.edu.iftm.prjweb2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;

import br.edu.iftm.prjweb2.model.Knife;
import br.edu.iftm.prjweb2.service.KnifeService;
import jakarta.validation.Valid;

@Controller
public class KnifeController {

    @Autowired
    private KnifeService knifeService;

    @GetMapping("/knife")
    public String index(Model model) {
        model.addAttribute("knifeList", knifeService.getAllKnifes());
        return "knife/index";
    }

    @GetMapping("/knife/create")
    public String create(Model model) {
        model.addAttribute("knife", new Knife());
        return "knife/create";
    }

    @PostMapping("/knife/save")
    public String save(@ModelAttribute @Valid Knife knife, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("knife", knife);
            return "knife/create";
        }

        knifeService.saveKnife(knife);
        return "redirect:/knife";
    }

    @GetMapping("/knife/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.knifeService.deleteKnifeById(id);
        return "redirect:/knife";
    }

    @GetMapping("/knife/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Knife knife = knifeService.getKnifeById(id);
        model.addAttribute("knife", knife);
        return "knife/edit";
    }
}
