package com.denuncias.models.repositories;

import com.denuncias.models.daos.UsuarioDAO;
import com.denuncias.models.entities.TipoUsuario;
import com.denuncias.models.entities.Usuario;
import com.github.hugoperlin.results.Resultado;

public class UsuarioRepository {

    private UsuarioDAO usuarioDAO;

    public UsuarioRepository(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Resultado cadastrar(String nome, String email, String senha, TipoUsuario tipo) {

        if (nome.isEmpty() || nome.isBlank()) {
            return Resultado.erro("Nome inválido!");
        }

        if (email.isEmpty() || email.isBlank()) {
            return Resultado.erro("Email inválido!");
        }

        if (senha.isEmpty() || senha.isBlank() || senha.length() < 8 || senha.length() > 20) {
            return Resultado.erro("Senha inválida!");
        }

        if (tipo == null) {
            return Resultado.erro("Tipo inválido!");
        }

        Usuario usuario = new Usuario(nome, email, senha, tipo);

        return usuarioDAO.criar(usuario);
    }

}
