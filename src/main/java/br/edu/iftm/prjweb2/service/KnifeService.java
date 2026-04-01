package br.edu.iftm.prjweb2.service;

import java.util.List;
import br.edu.iftm.prjweb2.model.Knife;

public interface KnifeService {
    List<Knife> getAllKnifes();

    void saveKnife(Knife knife);

    Knife getKnifeById(long id);

    void deleteKnifeById(long id);
}
