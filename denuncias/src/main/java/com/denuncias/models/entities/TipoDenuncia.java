package com.denuncias.models.entities;

public enum TipoDenuncia {

    BULLYING ("Bullying"),
    ASSEDIO ("Assédio"),
    PRECONCEITO ("Preconceito"),
    CONFLITO ("Conflito"),
    ROUBO ("Roubo"),
    VANDALISMO ("Vandalismo"),
    COMPORTAMENTO_INDISCIPLINADO ("Comportamento Indiscipliando");

    private final String denuncia;

    TipoDenuncia(String denuncia) {
        this.denuncia = denuncia;
    }

    public String getDenuncia() {
        return denuncia;
    }
}
