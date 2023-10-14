package com.denuncias.models.entities;

public enum TipoUsuario {

    ALUNO("Aluno"),
    MODERADOR("Moderador"),
    ADMIN("Admin");

    private String usuario;

    private TipoUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }
}
