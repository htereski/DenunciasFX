package com.denuncias.models.repositories;

import javax.naming.spi.DirStateFactory.Result;

import com.denuncias.models.daos.UsuarioDAO;
import com.denuncias.models.entities.TipoUsuario;
import com.denuncias.models.entities.Usuario;
import com.denuncias.utils.Email;
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

    public Resultado moderadores() {
        return usuarioDAO.getModeradores();
    }

    public Resultado alterarSenha(int id, String senha) {
        
        if (senha.isBlank() || senha.isEmpty() || senha.length() < 8 || senha.length() > 20) {
            return Resultado.erro("Senha inválida!");
        }

        return usuarioDAO.alterarSenha(id, senha);
    }

    public Resultado recuperarSenha(String email) {

        if (email.isBlank() || email.isEmpty()) {
            return Resultado.erro("Email inválido!");
        }

        Resultado r1 = usuarioDAO.recuperarConta(email);

        if (r1.foiErro()) {
            return Resultado.erro(r1.getMsg());
        }

        Usuario usuario = (Usuario) r1.comoSucesso().getObj();

        return Email.send(usuario.getEmail(), usuario.getNome(), usuario.getSenha());
    }

    public Resultado excluir(int id) {
        return usuarioDAO.excluir(id);
    }
}
