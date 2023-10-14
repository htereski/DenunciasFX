package com.denuncias.models.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Denuncia {

    private int id;

    private Usuario aluno;

    private String titulo;

    private String descricao;

    private String local;

    private TipoDenuncia tipo;

    private TipoStatus status;

    private boolean isVisualizada;

    private List<Comentario> comentarios;

    private LocalDate data;

    private LocalTime hora;

    public Denuncia(Usuario aluno, String titulo, String descricao, String local, TipoDenuncia tipo, TipoStatus status,
            boolean isVisualizada, List<Comentario> comentarios, LocalDate data, LocalTime hora) {
        this.aluno = aluno;
        this.titulo = titulo;
        this.descricao = descricao;
        this.local = local;
        this.tipo = tipo;
        this.status = status;
        this.isVisualizada = isVisualizada;
        this.comentarios = comentarios;
        this.data = data;
        this.hora = hora;
    }

    public Denuncia(int id, Usuario aluno, String titulo, String descricao, String local, TipoDenuncia tipo,
            TipoStatus status, boolean isVisualizada, List<Comentario> comentarios, LocalDate data, LocalTime hora) {
        this.id = id;
        this.aluno = aluno;
        this.titulo = titulo;
        this.descricao = descricao;
        this.local = local;
        this.tipo = tipo;
        this.status = status;
        this.isVisualizada = isVisualizada;
        this.comentarios = comentarios;
        this.data = data;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getAluno() {
        return aluno;
    }

    public void setAluno(Usuario aluno) {
        this.aluno = aluno;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoDenuncia getTipo() {
        return tipo;
    }

    public void setTipo(TipoDenuncia tipo) {
        this.tipo = tipo;
    }

    public TipoStatus getStatus() {
        return status;
    }

    public void setStatus(TipoStatus status) {
        this.status = status;
    }

    public boolean isVisualizada() {
        return isVisualizada;
    }

    public void setVisualizada(boolean isVisualizada) {
        this.isVisualizada = isVisualizada;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
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

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    @Override
    public String toString() {
        return "Denuncia [id=" + id + ", aluno=" + aluno + ", titulo=" + titulo + ", descricao=" + descricao + ", tipo="
                + tipo + ", status=" + status + ", isVisualizada=" + isVisualizada + ", comentarios=" + comentarios
                + ", data=" + data + ", hora=" + hora + "]";
    }

}
