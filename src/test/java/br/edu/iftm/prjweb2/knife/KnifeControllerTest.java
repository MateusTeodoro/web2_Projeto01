package br.edu.iftm.prjweb2.knife;

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
import br.edu.iftm.prjweb2.controller.KnifeController;
import br.edu.iftm.prjweb2.model.Categoria;
import br.edu.iftm.prjweb2.model.Fabricante;
import br.edu.iftm.prjweb2.model.Knife;
import br.edu.iftm.prjweb2.model.Material;
import br.edu.iftm.prjweb2.service.KnifeService;

@WebMvcTest(KnifeController.class)
@Import(TestConfig.class)
public class KnifeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KnifeService knifeService;

    @AfterEach
    void resetMocks() {
        reset(knifeService);
    }

    private List<Knife> testCreateKnifeList(){
        
        Categoria categoriaA = new Categoria();
        categoriaA.setId(1L);
        categoriaA.setDescription("Descricao");
        categoriaA.setName("Categoria A");
        
        Fabricante fabricanteA = new Fabricante();
        fabricanteA.setId(1L);
        fabricanteA.setOrigem("Origem");
        fabricanteA.setName("Fabricante A");
        fabricanteA.setAnoFundacao(123);
        
        Material materialA = new Material();
        materialA.setId(1L);
        materialA.setTipoUso("Uso");
        materialA.setName("Material A");

        Knife knifeA = new Knife();
        knifeA.setId(1L);
        knifeA.setDescription("Descricao");
        knifeA.setName("Faca A");
        knifeA.setSize(65.24f);
        knifeA.setYear(121);
        knifeA.setCategoria(categoriaA);
        knifeA.setFabricante(fabricanteA);
        knifeA.getMateriais().add(materialA);

        return List.of(knifeA);
    }

    @Test
    @DisplayName("GET /knife - Listar faca na tela index sem usuário autenticado")
    void testIndexNotAuthenticatedUser() throws Exception {
         mockMvc.perform(get("/knife"))
            .andExpect(status().isUnauthorized()); // Correção aqui
    }

    @Test
    @WithMockUser
    @DisplayName("GET /knife - Listar facas na tela index com usuário logado")
    void testIndexAuthenticatedUser() throws Exception {
        when(knifeService.getAllKnifes()).thenReturn(testCreateKnifeList());

        mockMvc.perform(get("/knife"))
               .andExpect(status().isOk())
               .andExpect(view().name("knife/index"))
               .andExpect(model().attributeExists("knifeList"))
               .andExpect(content().string(containsString("Listagem de Facas")))
               .andExpect(content().string(containsString("Faca A")));
    }

    @Test
    @WithMockUser(username = "aluno@iftm.edu.br", authorities = { "Admin" })
    @DisplayName("GET /knife/create - Exibe formulário de criação")
    void testCreateFormAuthorizedUser() throws Exception {
        mockMvc.perform(get("/knife/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("knife/create"))
                .andExpect(model().attributeExists("knife"))
                .andExpect(content().string(containsString("Cadastrar Faca")));
    }

    @Test
    @WithMockUser(username = "aluno2@iftm.edu.br", authorities = { "Manager" })
    @DisplayName("GET /knife - Verificar o link de cadastrar para um usuario não admin logado")
    void testCreateFormNotAuthorizedUser() throws Exception {
        when(knifeService.getAllKnifes()).thenReturn(testCreateKnifeList());
       // Obter o HTML da página renderizada pelo controlador
       mockMvc.perform(get("/knife/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("knife/create"))
            .andExpect(model().attributeExists("knife"))
            .andExpect(content().string(not(containsString("<a class=\"dropdown-item\" href=\"/knife/create\">Cadastrar</a>"))));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /knife/save - Falha na validação e retorna para o formulário")
    void testSaveknifeValidationError() throws Exception {
        Knife knife = new Knife(); // Faca sem nome, o que causará erro de validação

        mockMvc.perform(post("/knife/save")
                        .with(csrf())
                        .flashAttr("knife", knife))
                .andExpect(status().isOk())
                .andExpect(view().name("knife/create"))
                .andExpect(model().attributeHasErrors("knife"));

        verify(knifeService, never()).saveKnife(any(Knife.class));
    }

    @Test
    @WithMockUser(username = "aluno@iftm.edu.br", authorities = { "Admin" })
    @DisplayName("POST /knife/save - Produto válido é salvo com sucesso")
    void testSaveValidknife() throws Exception {
        
        Categoria categoriaA = new Categoria();
        categoriaA.setId(1L);
        categoriaA.setDescription("Descricao");
        categoriaA.setName("Categoria A");
        
        Fabricante fabricanteA = new Fabricante();
        fabricanteA.setId(1L);
        fabricanteA.setOrigem("Origem");
        fabricanteA.setName("Fabricante A");
        fabricanteA.setAnoFundacao(123);
        
        Material materialA = new Material();
        materialA.setId(1L);
        materialA.setTipoUso("Uso");
        materialA.setName("Material A");
        
        Knife knife = new Knife();
        knife.setName("Nova Faca");
        knife.setDescription("Descrição");
        knife.setSize(100f);
        knife.setYear(10);
        knife.setCategoria(categoriaA);
        knife.setFabricante(fabricanteA);
        knife.getMateriais().add(materialA);

        mockMvc.perform(post("/knife/save")
                        .with(csrf())
                        .flashAttr("knife", knife))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/knife"));

        verify(knifeService).saveKnife(any(Knife.class));
    }
}
