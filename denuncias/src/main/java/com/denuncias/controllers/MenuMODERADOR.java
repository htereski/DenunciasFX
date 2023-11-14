package com.denuncias.controllers;

import java.util.Optional;

import com.denuncias.App;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.ComentarioRepository;
import com.denuncias.models.repositories.DenunciaRepository;
import com.denuncias.models.repositories.UsuarioRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ButtonType;

public class MenuMODERADOR {

    private ComentarioRepository comentarioRepository;

    private UsuarioRepository usuarioRepository;

    private DenunciaRepository denunciaRepository;

    private Usuario moderador;

    public MenuMODERADOR(ComentarioRepository comentarioRepository, UsuarioRepository usuarioRepository,
            DenunciaRepository denunciaRepository, Usuario moderador) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.denunciaRepository = denunciaRepository;
        this.moderador = moderador;
    }

    public MenuMODERADOR() {
    }

    @FXML
    void alterarSenha(ActionEvent event) {
        App.pushScreen("TROCARSENHA", o -> new TrocarSenha(moderador, usuarioRepository));
    }

    @FXML
    void mostrarDenuncias(ActionEvent event) {
        App.pushScreen("MOSTRARDENUNCIAS", o -> new MostrarDenuncias(comentarioRepository, denunciaRepository, moderador));
    }

    @FXML
    void sair(MouseEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Você deseja mesmo sair da conta?");
        alert.setContentText("Escolha uma opção.");

        ButtonType simButton = new ButtonType("Sim");
        ButtonType naoButton = new ButtonType("Não");

        alert.getButtonTypes().setAll(simButton, naoButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == simButton) {
            App.popScreen();
        }
    }

}
