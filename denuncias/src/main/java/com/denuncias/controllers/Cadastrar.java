package com.denuncias.controllers;

import com.denuncias.App;
import com.denuncias.models.entities.TipoUsuario;
import com.denuncias.models.repositories.UsuarioRepository;
import com.github.hugoperlin.results.Resultado;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class Cadastrar {

    @FXML
    private PasswordField pfSenha;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfNome;

    private UsuarioRepository usuarioRepository;

    private TipoUsuario tipoUsuario;

    public Cadastrar(UsuarioRepository usuarioRepository, TipoUsuario tipoUsuario) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuario = tipoUsuario;
    }

    public Cadastrar() {

    }

    @FXML
    void confirmar(ActionEvent event) {
        String nome = tfNome.getText();
        String email = tfEmail.getText();
        String senha = pfSenha.getText();

        Resultado resultado = usuarioRepository.cadastrar(nome, email, senha, tipoUsuario);

        Alert alert;

        if (resultado.foiErro()) {
            alert = new Alert(AlertType.ERROR, resultado.getMsg());
        } else {
            alert = new Alert(AlertType.CONFIRMATION, resultado.getMsg());
            tfNome.clear();
            tfEmail.clear();
            pfSenha.clear();
        }
        alert.showAndWait();
    }

    @FXML
    void voltar(MouseEvent event) {
        App.popScreen();
    }

}
