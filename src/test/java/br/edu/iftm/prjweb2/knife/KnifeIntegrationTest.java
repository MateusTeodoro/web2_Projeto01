package br.edu.iftm.prjweb2.knife;

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

import br.edu.iftm.prjweb2.model.Knife;
import br.edu.iftm.prjweb2.repository.KnifeRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa application-test.properties
@Transactional // Limpa o banco após cada teste
public class KnifeIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KnifeRepository knifeRepository;

    @Test
    @WithMockUser(authorities = { "Admin" })
    void testSaveKnifeIntegration() throws Exception {

        Knife knife = new Knife();
        knife.setDescription("Descricao");
        knife.setName("Faca A");
        knife.setSize(65.24f);
        knife.setYear(121);

        mockMvc.perform(post("/knife/save")
                .with(csrf())
                .flashAttr("knife", knife))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/knife"));

        // Verifica no banco se foi salvo
        assertTrue(knifeRepository.findAll()
                .stream()
                .anyMatch(f -> "Faca A".equals(f.getName())));

    }
}
