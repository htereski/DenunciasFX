package com.denuncias.models.entities;

public enum TipoStatus {
    
    REGISTRADO ("Registrado"),
    INVESTIGANDO ("Investigando"),
    ENCERRADO ("Encerrado");

    private String status;

    private TipoStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
