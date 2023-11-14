package com.denuncias.controllers;

import com.denuncias.App;
import com.denuncias.models.repositories.UsuarioRepository;
import com.github.hugoperlin.results.Resultado;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class RecuperarSenha {

    @FXML
    private Label lbSenha;

    @FXML
    private StackPane pane;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private TextField tfEmail;

    private UsuarioRepository usuarioRepository;

    public RecuperarSenha(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public RecuperarSenha() {

    }

    @FXML
    void enviarEmail(ActionEvent event) {

        String email = tfEmail.getText();

        Task<Resultado> taskEmail = new Task<Resultado>() {

            @Override
            protected Resultado call() throws Exception {
                return usuarioRepository.recuperarSenha(email);
            }

        };

        taskEmail.setOnSucceeded(e -> {

            lbSenha.setVisible(true);
            progressIndicator.setVisible(false);
            tfEmail.setVisible(true);
            pane.setVisible(false);
            pane.setDisable(true);

            Alert alert;
            Resultado resultado = taskEmail.getValue();

            if (resultado.foiErro()) {
                alert = new Alert(AlertType.ERROR, resultado.getMsg());
            } else {
                alert = new Alert(AlertType.CONFIRMATION, resultado.getMsg());
                tfEmail.clear();
            }
            alert.showAndWait();
        });

        pane.setVisible(true);
        pane.setDisable(false);
        progressIndicator.setVisible(true);
        tfEmail.setVisible(false);
        lbSenha.setVisible(false);

        Thread thread = new Thread(taskEmail);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    void voltar(MouseEvent event) {
        App.popScreen();
    }

}
