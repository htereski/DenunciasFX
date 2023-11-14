package com.denuncias.controllers;

import com.denuncias.App;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.UsuarioRepository;
import com.github.hugoperlin.results.Resultado;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;

public class TrocarSenha {

    @FXML
    private StackPane pane;

    @FXML
    private PasswordField pfSenhaAtual;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private PasswordField pfNovaSenha;

    private Usuario usuario;

    private UsuarioRepository usuarioRepository;

    public TrocarSenha(Usuario usuario, UsuarioRepository usuarioRepository) {
        this.usuario = usuario;
        this.usuarioRepository = usuarioRepository;
    }

    public TrocarSenha() {
    }

    @FXML
    void confirmar(ActionEvent event) {
        String senhaAtual = pfSenhaAtual.getText();
        String novaSenha = pfNovaSenha.getText();

        Task<Resultado> taskTrocarSenha = new Task<Resultado>() {

            @Override
            protected Resultado call() throws Exception {
                if (senhaAtual.equals(usuario.getSenha())) {
                    return usuarioRepository.alterarSenha(usuario.getId(), novaSenha);
                }
                return Resultado.erro("Senha errada!");
            }

        };

        taskTrocarSenha.setOnSucceeded(e -> {

            pfNovaSenha.setVisible(true);
            pfSenhaAtual.setVisible(true);

            pane.setVisible(false);
            pane.setDisable(true);
            progressIndicator.setVisible(false);
            progressIndicator.setDisable(true);

            Alert alert;
            Resultado resultado;

            resultado = taskTrocarSenha.getValue();

            if (resultado.foiErro()) {
                alert = new Alert(AlertType.ERROR, resultado.getMsg());
            } else {
                alert = new Alert(AlertType.CONFIRMATION, resultado.getMsg());
                pfSenhaAtual.clear();
                pfNovaSenha.clear();
            }
            alert.showAndWait();
        });

        pfNovaSenha.setVisible(false);
        pfSenhaAtual.setVisible(false);

        pane.setVisible(true);
        pane.setDisable(false);
        progressIndicator.setVisible(true);
        progressIndicator.setDisable(false);

        Thread thread = new Thread(taskTrocarSenha);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    void voltar(MouseEvent event) {
        App.popScreen();
    }

}
