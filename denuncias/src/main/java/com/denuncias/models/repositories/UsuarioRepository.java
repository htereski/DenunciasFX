package com.denuncias.models.repositories;

import com.denuncias.models.entities.TipoUsuario;
import com.github.hugoperlin.results.Resultado;

public interface UsuarioRepository {
    
    public Resultado cadastrar(String nome, String email, String senha, TipoUsuario tipo);

    public Resultado moderadores();

    public Resultado alterarSenha(int id, String senha);

    public Resultado recuperarSenha(String email);

    public Resultado excluir(int id);
}
