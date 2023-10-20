package com.denuncias.models.repositories;

import java.time.LocalDate;
import java.time.LocalTime;

import com.denuncias.models.entities.TipoStatus;
import com.denuncias.models.entities.Usuario;
import com.github.hugoperlin.results.Resultado;

public interface ComentarioRepository {
    
    public Resultado cadastrar(String conteudo, Usuario moderador, TipoStatus status, LocalDate data, LocalTime hora, int denunciaId);
}
