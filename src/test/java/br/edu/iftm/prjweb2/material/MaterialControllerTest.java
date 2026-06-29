package br.edu.iftm.prjweb2.material;

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
import br.edu.iftm.prjweb2.controller.MaterialController;
import br.edu.iftm.prjweb2.model.Material;
import br.edu.iftm.prjweb2.service.MaterialService;

@WebMvcTest(MaterialController.class)
@Import(TestConfig.class)
public class MaterialControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MaterialService materialService;

    @AfterEach
    void resetMocks() {
        reset(materialService);
    }

    private List<Material> testCreateMaterialList(){
        Material materialA = new Material();
        materialA.setId(1L);
        materialA.setTipoUso("Uso");
        materialA.setName("Material A");
        materialA.setKnives(null);

        return List.of(materialA);
    }

    @Test
    @DisplayName("GET /material - Listar Material na tela index sem usuário autenticado")
    void testIndexNotAuthenticatedUser() throws Exception {
         mockMvc.perform(get("/material"))
            .andExpect(status().isUnauthorized()); // Correção aqui
    }

    @Test
    @WithMockUser
    @DisplayName("GET /material - Listar materials na tela index com usuário logado")
    void testIndexAuthenticatedUser() throws Exception {
        when(materialService.getAllMaterials()).thenReturn(testCreateMaterialList());

        mockMvc.perform(get("/material"))
               .andExpect(status().isOk())
               .andExpect(view().name("material/index"))
               .andExpect(model().attributeExists("materials"))
               .andExpect(content().string(containsString("Listagem de Materiais")))
               .andExpect(content().string(containsString("Material A")));
    }

    @Test
    @WithMockUser(username = "aluno@iftm.edu.br", authorities = { "Admin" })
    @DisplayName("GET /material/create - Exibe formulário de criação")
    void testCreateFormAuthorizedUser() throws Exception {
        mockMvc.perform(get("/material/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("material/create"))
                .andExpect(model().attributeExists("material"))
                .andExpect(content().string(containsString("Cadastrar Material")));
    }

    @Test
    @WithMockUser(username = "aluno2@iftm.edu.br", authorities = { "Usuario" })
    @DisplayName("GET /material - Verificar o link de cadastrar para um usuario não admin logado")
    void testCreateFormNotAuthorizedUser() throws Exception {
        when(materialService.getAllMaterials()).thenReturn(testCreateMaterialList());
       // Obter o HTML da página renderizada pelo controlador
       mockMvc.perform(get("/material/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("material/create"))
            .andExpect(model().attributeExists("material"))
            .andExpect(content().string(not(containsString("<a class=\"dropdown-item\" href=\"/material/create\">Cadastrar</a>"))));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /material/save - Falha na validação e retorna para o formulário")
    void testSaveMaterialValidationError() throws Exception {
        Material material = new Material(); // material sem nome, o que causará erro de validação

        mockMvc.perform(post("/material/save")
                        .with(csrf())
                        .flashAttr("material", material))
                .andExpect(status().isOk())
                .andExpect(view().name("material/create"))
                .andExpect(model().attributeHasErrors("material"));

        verify(materialService, never()).saveMaterial(any(Material.class));
    }

    @Test
    @WithMockUser(username = "aluno@iftm.edu.br", authorities = { "Admin" })
    @DisplayName("POST /material/save - material válido é salvo com sucesso")
    void testSaveValidMaterial() throws Exception {
        Material material = new Material();
        material.setName("Novo Material");
        material.setTipoUso("Uso");

        mockMvc.perform(post("/material/save")
                        .with(csrf())
                        .flashAttr("material", material))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/material"));

        verify(materialService).saveMaterial(any(Material.class));
    }
}
