package br.edu.iftm.prjweb2.fabricante;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import br.edu.iftm.prjweb2.config.TestConfig;
import br.edu.iftm.prjweb2.controller.FabricanteController;
import br.edu.iftm.prjweb2.model.Fabricante;
import br.edu.iftm.prjweb2.service.FabricanteService;

@WebMvcTest(FabricanteController.class)
@Import(TestConfig.class)
public class FabricanteControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FabricanteService fabricanteService;

    @AfterEach
    void resetMocks() {
        reset(fabricanteService);
    }

    private List<Fabricante> testCreateFabricanteList(){
        Fabricante fabricanteA = new Fabricante();
        fabricanteA.setId(1L);
        fabricanteA.setOrigem("Origem");
        fabricanteA.setName("Fabricante A");
        fabricanteA.setAnoFundacao(123);
        fabricanteA.setKnives(null);

        return List.of(fabricanteA);
    }

    @Test
    @DisplayName("GET /fabricante - Listar fabricante na tela index sem usuário autenticado")
    void testIndexNotAuthenticatedUser() throws Exception {
         mockMvc.perform(get("/fabricante"))
            .andExpect(status().isUnauthorized()); // Correção aqui
    }

    @Test
    @WithMockUser
    @DisplayName("GET /fabricante - Listar fabricantes na tela index com usuário logado")
    void testIndexAuthenticatedUser() throws Exception {
        when(fabricanteService.getAllFabricantes()).thenReturn(testCreateFabricanteList());

        mockMvc.perform(get("/fabricante"))
               .andExpect(status().isOk())
               .andExpect(view().name("fabricante/index"))
               .andExpect(model().attributeExists("fabricantes"))
               .andExpect(content().string(containsString("Listagem de Fabricantes")))
               .andExpect(content().string(containsString("Fabricante A")));
    }

    @Test
    @WithMockUser(username = "aluno@iftm.edu.br", authorities = { "Admin" })
    @DisplayName("GET /fabricante/create - Exibe formulário de criação")
    void testCreateFormAuthorizedUser() throws Exception {
        mockMvc.perform(get("/fabricante/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("fabricante/create"))
                .andExpect(model().attributeExists("fabricante"))
                .andExpect(content().string(containsString("Cadastrar Fabricante")));
    }

    @Test
    @WithMockUser(username = "aluno2@iftm.edu.br", authorities = { "Usuario" })
    @DisplayName("GET /fabricante - Verificar o link de cadastrar para um usuario não admin logado")
    void testCreateFormNotAuthorizedUser() throws Exception {
        when(fabricanteService.getAllFabricantes()).thenReturn(testCreateFabricanteList());
       // Obter o HTML da página renderizada pelo controlador
       mockMvc.perform(get("/fabricante/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("fabricante/create"))
            .andExpect(model().attributeExists("fabricante"))
            .andExpect(content().string(not(containsString("<a class=\"dropdown-item\" href=\"/fabricante/create\">Cadastrar</a>"))));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /fabricante/save - Falha na validação e retorna para o formulário")
    void testSavefabricanteValidationError() throws Exception {
        Fabricante fabricante = new Fabricante(); // fabricante sem nome, o que causará erro de validação

        mockMvc.perform(post("/fabricante/save")
                        .with(csrf())
                        .flashAttr("fabricante", fabricante))
                .andExpect(status().isOk())
                .andExpect(view().name("fabricante/create"))
                .andExpect(model().attributeHasErrors("fabricante"));

        verify(fabricanteService, never()).saveFabricante(any(Fabricante.class));
    }

    @Test
    @WithMockUser(username = "aluno@iftm.edu.br", authorities = { "Admin" })
    @DisplayName("POST /fabricante/save - Fabricante válido é salvo com sucesso")
    void testSaveValidFabricante() throws Exception {
        Fabricante fabricante = new Fabricante();
        fabricante.setName("Novo Fabricante");
        fabricante.setOrigem("Origem");
        fabricante.setAnoFundacao(123);

        mockMvc.perform(post("/fabricante/save")
                        .with(csrf())
                        .flashAttr("fabricante", fabricante))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/fabricante"));

        verify(fabricanteService).saveFabricante(any(Fabricante.class));
    }
}
