package br.edu.iftm.prjweb2.fabricante;

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

import br.edu.iftm.prjweb2.model.Fabricante;
import br.edu.iftm.prjweb2.repository.FabricanteRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa application-test.properties
@Transactional // Limpa o banco após cada teste
public class FabricanteIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FabricanteRepository fabricanteRepository;

    @Test
    @WithMockUser(authorities = { "Admin" })
    void testSaveFabricanteIntegration() throws Exception {

        Fabricante fabricante = new Fabricante();
        fabricante.setOrigem("Origem");
        fabricante.setName("Fabricante A");
        fabricante.setAnoFundacao(123);

        mockMvc.perform(post("/fabricante/save")
                .with(csrf())
                .flashAttr("fabricante", fabricante))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/fabricante"));

        // Verifica no banco se foi salvo
        assertTrue(fabricanteRepository.findAll()
                .stream()
                .anyMatch(f -> "Fabricante A".equals(f.getName())));

    }
}
