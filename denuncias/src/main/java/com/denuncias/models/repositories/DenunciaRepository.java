package com.denuncias.models.repositories;

import java.time.LocalDate;
import java.time.LocalTime;

import com.denuncias.models.entities.TipoDenuncia;
import com.denuncias.models.entities.TipoStatus;
import com.denuncias.models.entities.Usuario;
import com.github.hugoperlin.results.Resultado;

public interface DenunciaRepository {

    public Resultado cadastrar(Usuario aluno, String titulo, String descricao, String local, TipoDenuncia tipo, TipoStatus status, LocalDate data, LocalTime hora);

    public Resultado abertas();

    public Resultado porUsuario(int usuarioId);
}
