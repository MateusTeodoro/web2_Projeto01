package br.edu.iftm.prjweb2.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.iftm.prjweb2.model.Knife;
import br.edu.iftm.prjweb2.service.KnifeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class KnifeRestController {
    
    @Autowired
    private KnifeService knifeService;

    @GetMapping
    public ResponseEntity<List<Knife>> getAllKnifes() {
        List<Knife> knifes = knifeService.getAllKnifes();
        return ResponseEntity.ok(knifes);
    }

    /**
     * Resposta: 200 OK com o produto, ou 404 Not Found se não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Knife> getKnifeById(@PathVariable Long id) {
        try {
            Knife knife = knifeService.getKnifeById(id);
            return ResponseEntity.ok(knife);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Corpo da Requisição: JSON do produto a ser criado.
     * Resposta: 201 Created com o local do novo recurso e o produto criado.
     * 400 Bad Request se a validação falhar.
     */
    @PostMapping
    public ResponseEntity<?> createKnife(@Valid @RequestBody Knife knife, BindingResult result) {
        if (result.hasErrors()) {
            // Retorna 400 Bad Request com os erros de validação
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        
        knifeService.saveKnife(knife);

        // Cria a URI para o novo recurso (ex: /api/products/5)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(knife.getId())
                .toUri();

        // Retorna 201 Created
        return ResponseEntity.created(location).body(knife);
    }

    /**
     * Corpo da Requisição: JSON do produto com as atualizações.
     * Resposta: 200 OK com o produto atualizado.
     * 404 Not Found se o produto não existir.
     * 400 Bad Request se a validação falhar.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateKnife(@PathVariable Long id, @Valid @RequestBody Knife knifeDetails, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try {
            knifeService.saveKnife(knifeDetails);
            
            return ResponseEntity.ok(knifeDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Resposta: 204 No Content se a exclusão for bem-sucedida.
     * 404 Not Found se o produto não existir.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKnife(@PathVariable Long id) {
        try {           
            knifeService.deleteKnifeById(id);
            
            // Retorna 204 No Content (sucesso, sem corpo de resposta)
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
