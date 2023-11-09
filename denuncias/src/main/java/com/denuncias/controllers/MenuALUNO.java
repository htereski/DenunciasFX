package com.denuncias.controllers;

import java.util.Optional;

import com.denuncias.App;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.DenunciaRepository;
import com.denuncias.models.repositories.UsuarioRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class MenuALUNO {

    private UsuarioRepository usuarioRepository;

    private DenunciaRepository denunciaRepository;

    private Usuario aluno;

    public MenuALUNO() {
    }

    public MenuALUNO(UsuarioRepository usuarioRepository, Usuario aluno, DenunciaRepository denunciaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.aluno = aluno;
        this.denunciaRepository = denunciaRepository;
    }

    @FXML
    void alterarSenha(ActionEvent event) {
        App.pushScreen("TROCARSENHA", o -> new TrocarSenha(aluno, usuarioRepository));
    }

    @FXML
    void fazerDenuncia(ActionEvent event) {
        App.pushScreen("FAZERDENUNCIA", o -> new FazerDenuncia(aluno, denunciaRepository));
    }

    @FXML
    void mostrarDenuncias(ActionEvent event) {
        App.pushScreen("SUASDENUNCIAS", o -> new SuasDenuncias(aluno, denunciaRepository));
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
