package com.denuncias.models.repositories;

import java.time.LocalDate;
import java.time.LocalTime;

import com.denuncias.models.daos.ComentarioDAO;
import com.denuncias.models.entities.Comentario;
import com.denuncias.models.entities.TipoStatus;
import com.denuncias.models.entities.Usuario;
import com.github.hugoperlin.results.Resultado;

public class ComentarioRepositoryImpl implements ComentarioRepository {

    private ComentarioDAO comentarioDAO;

    public ComentarioRepositoryImpl(ComentarioDAO comentarioDAO) {
        this.comentarioDAO = comentarioDAO;
    }

    @Override
    public Resultado cadastrar(String conteudo, Usuario moderador, TipoStatus status, LocalDate data, LocalTime hora, int denunciaId) {

        if (status == null) {
            return Resultado.erro("Status inválido!");
        }

        if (conteudo.isBlank() || conteudo.isEmpty()) {
            return Resultado.erro("Comentário inválido!");
        }

        if (moderador == null) {
            return Resultado.erro("Moderador inválido!");
        }

        if (data == null) {
            return Resultado.erro("Data inválida!");
        }

        if (hora == null) {
            return Resultado.erro("Hora inválida!");
        }

        Comentario comentario = new Comentario(conteudo, moderador, status, data, hora);

        return comentarioDAO.criar(comentario, denunciaId);
    }

    @Override
    public Resultado getByDenuncia(int id) {
        return comentarioDAO.getByDenunciaId(id);
    }

}
