package br.edu.iftm.prjweb2.config;

import br.edu.iftm.prjweb2.service.KnifeService;
import br.edu.iftm.prjweb2.service.CategoriaService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public KnifeService productService() {
        return Mockito.mock(KnifeService.class);
    }

    @Bean
    public CategoriaService categoriaService() {
        return Mockito.mock(CategoriaService.class);
    }

} 
