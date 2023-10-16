package com.denuncias.models.daos;

import com.denuncias.models.entities.Denuncia;
import com.github.hugoperlin.results.Resultado;

public interface DenunciaDAO {
    
    public Resultado criar(Denuncia denuncia);

    public Resultado getById(int id);

    public Resultado getAbertas();

    public Resultado getByUsuarioId(int usuarioId);
}
