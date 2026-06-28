package br.edu.iftm.prjweb2.categoria;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.iftm.prjweb2.model.Categoria;
import br.edu.iftm.prjweb2.repository.CategoriaRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa application-test.properties
@Transactional // Limpa o banco após cada teste
public class CategoriaIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    @WithMockUser(authorities = { "Admin" })
    void testSaveCategoriaIntegration() throws Exception {

        Categoria categoria = new Categoria();
        categoria.setDescription("Descricao");
        categoria.setName("Categoria A");

        mockMvc.perform(post("/categoria/save")
                .with(csrf())
                .flashAttr("categoria", categoria))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categoria"));

        // Verifica no banco se foi salvo
        assertTrue(categoriaRepository.findAll()
                .stream()
                .anyMatch(f -> "Categoria A".equals(f.getName())));

    }
}
