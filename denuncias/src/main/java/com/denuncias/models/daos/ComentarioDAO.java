package com.denuncias.models.daos;

import com.denuncias.models.entities.Comentario;
import com.github.hugoperlin.results.Resultado;

public interface ComentarioDAO {
    
    public Resultado criar(Comentario comentario, int denunciaId);

    public Resultado getByDenunciaId(int denunciaId);

}
