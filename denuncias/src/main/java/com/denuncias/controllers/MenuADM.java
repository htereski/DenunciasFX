package com.denuncias.controllers;

import java.util.Optional;

import com.denuncias.App;
import com.denuncias.models.entities.TipoUsuario;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.ComentarioRepository;
import com.denuncias.models.repositories.DenunciaRepository;
import com.denuncias.models.repositories.UsuarioRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class MenuADM {

    private ComentarioRepository comentarioRepository;

    private DenunciaRepository denunciaRepository;

    private UsuarioRepository usuarioRepository;

    private Usuario admin;

    public MenuADM() {
    }

    public MenuADM(ComentarioRepository comentarioRepository, DenunciaRepository denunciaRepository,
            UsuarioRepository usuarioRepository, Usuario admin) {
        this.comentarioRepository = comentarioRepository;
        this.denunciaRepository = denunciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.admin = admin;
    }

    @FXML
    void adicionarModerador(ActionEvent event) {
        App.pushScreen("CADASTRAR", o -> new Cadastrar(usuarioRepository, TipoUsuario.MODERADOR));
    }

    @FXML
    void alterarSenha(ActionEvent event) {
        App.pushScreen("TROCARSENHA", o -> new TrocarSenha(admin, usuarioRepository));
    }

    @FXML
    void mostrarDenuncias(ActionEvent event) {
        App.pushScreen("MOSTRARDENUNCIAS", o -> new MostrarDenuncias(comentarioRepository, denunciaRepository, admin));
    }

    @FXML
    void removerModerador(ActionEvent event) {
        App.pushScreen("REMOVERMODERADOR", o -> new RemoverModerador(usuarioRepository));
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
