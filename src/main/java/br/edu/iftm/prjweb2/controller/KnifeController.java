package br.edu.iftm.prjweb2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
