package com.denuncias.models.entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Comentario {

    private int id;

    private String conteudo;

    private Usuario moderador;

    private TipoStatus status;

    private LocalDate data;

    private LocalTime hora;

    public Comentario(String conteudo, Usuario moderador, TipoStatus status, LocalDate data, LocalTime hora) {
        this.conteudo = conteudo;
        this.moderador = moderador;
        this.status = status;
        this.data = data;
        this.hora = hora;
    }

    public Comentario(int id, String conteudo, Usuario moderador, TipoStatus status, LocalDate data, LocalTime hora) {
        this.id = id;
        this.conteudo = conteudo;
        this.moderador = moderador;
        this.status = status;
        this.data = data;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Usuario getModerador() {
        return moderador;
    }

    public void setModerador(Usuario moderador) {
        this.moderador = moderador;
    }

    public TipoStatus getStatus() {
        return status;
    }

    public void setStatus(TipoStatus status) {
        this.status = status;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return conteudo;
    }

}
