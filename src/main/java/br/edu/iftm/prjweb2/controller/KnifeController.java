package br.edu.iftm.prjweb2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.iftm.prjweb2.model.Knife;
import br.edu.iftm.prjweb2.service.KnifeService;

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
    public String postMethodName(@ModelAttribute("knife") Knife knife) {
        knifeService.saveKnife(knife);
        return "redirect:/knife";
    }

    @GetMapping("/knife/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.knifeService.deleteKnifeById(id);
        return "redirect:/knife";
    }
}
