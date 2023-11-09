package com.denuncias.controllers;

import java.util.Optional;

import com.denuncias.App;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.UsuarioRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class MenuMODERADOR {

    private UsuarioRepository usuarioRepository;

    private Usuario moderador;

    public MenuMODERADOR(UsuarioRepository usuarioRepository, Usuario moderador) {
        this.usuarioRepository = usuarioRepository;
        this.moderador = moderador;
    }

    public MenuMODERADOR() {
    }

    @FXML
    void alterarSenha(ActionEvent event) {

    }

    @FXML
    void mostrarDenuncias(ActionEvent event) {

    }

    @FXML
    void fechar(ActionEvent event) {
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
