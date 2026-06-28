package br.edu.iftm.prjweb2.categoria;

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
import br.edu.iftm.prjweb2.controller.CategoriaController;
import br.edu.iftm.prjweb2.model.Categoria;
import br.edu.iftm.prjweb2.service.CategoriaService;

@WebMvcTest(CategoriaController.class)
@Import(TestConfig.class)
public class CategoriaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaService categoriaService;

    @AfterEach
    void resetMocks() {
        reset(categoriaService);
    }

    private List<Categoria> testCreateCategoriaList(){
        Categoria categoriaA = new Categoria();
        categoriaA.setId(1L);
        categoriaA.setDescription("Descricao");
        categoriaA.setName("Categoria A");
        categoriaA.setFacas(null);

        return List.of(categoriaA);
    }

    @Test
    @DisplayName("GET /categoria - Listar categoria na tela index sem usuário autenticado")
    void testIndexNotAuthenticatedUser() throws Exception {
         mockMvc.perform(get("/categoria"))
            .andExpect(status().isUnauthorized()); // Correção aqui
    }

    @Test
    @WithMockUser
    @DisplayName("GET /categoria - Listar categorias na tela index com usuário logado")
    void testIndexAuthenticatedUser() throws Exception {
        when(categoriaService.getAllCategorias()).thenReturn(testCreateCategoriaList());

        mockMvc.perform(get("/categoria"))
               .andExpect(status().isOk())
               .andExpect(view().name("categoria/index"))
               .andExpect(model().attributeExists("categorias"))
               .andExpect(content().string(containsString("Listagem de Categorias")))
               .andExpect(content().string(containsString("Categoria A")));
    }

    @Test
    @WithMockUser(username = "aluno@iftm.edu.br", authorities = { "Admin" })
    @DisplayName("GET /categoria/create - Exibe formulário de criação")
    void testCreateFormAuthorizedUser() throws Exception {
        mockMvc.perform(get("/categoria/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("categoria/create"))
                .andExpect(model().attributeExists("categoria"))
                .andExpect(content().string(containsString("Cadastrar Categoria")));
    }

    @Test
    @WithMockUser(username = "aluno2@iftm.edu.br", authorities = { "Usuario" })
    @DisplayName("GET /categoria - Verificar o link de cadastrar para um usuario não admin logado")
    void testCreateFormNotAuthorizedUser() throws Exception {
        when(categoriaService.getAllCategorias()).thenReturn(testCreateCategoriaList());
       // Obter o HTML da página renderizada pelo controlador
       mockMvc.perform(get("/categoria/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("categoria/create"))
            .andExpect(model().attributeExists("categoria"))
            .andExpect(content().string(not(containsString("<a class=\"dropdown-item\" href=\"/categoria/create\">Cadastrar</a>"))));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /categoria/save - Falha na validação e retorna para o formulário")
    void testSaveCategoriaValidationError() throws Exception {
        Categoria categoria = new Categoria(); // Categoria sem nome, o que causará erro de validação

        mockMvc.perform(post("/categoria/save")
                        .with(csrf())
                        .flashAttr("categoria", categoria))
                .andExpect(status().isOk())
                .andExpect(view().name("categoria/create"))
                .andExpect(model().attributeHasErrors("categoria"));

        verify(categoriaService, never()).saveCategoria(any(Categoria.class));
    }

    @Test
    @WithMockUser(username = "aluno@iftm.edu.br", authorities = { "Admin" })
    @DisplayName("POST /categoria/save - Produto válido é salvo com sucesso")
    void testSaveValidCategoria() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setName("Nova Categoria");
        categoria.setDescription("Descrição");

        mockMvc.perform(post("/categoria/save")
                        .with(csrf())
                        .flashAttr("categoria", categoria))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categoria"));

        verify(categoriaService).saveCategoria(any(Categoria.class));
    }
}
