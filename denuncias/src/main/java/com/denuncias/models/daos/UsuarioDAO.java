package com.denuncias.models.daos;

import com.denuncias.models.entities.Usuario;
import com.github.hugoperlin.results.Resultado;

public interface UsuarioDAO {
    
    public Resultado criar(Usuario usuario);

    public Resultado getModeradores();

    public Resultado getById(int id);

    public Resultado getByComentarioId(int comentarioId);

    public Resultado getByDenunciaId(int denunciaId);

    public Resultado excluir(int id);
}
