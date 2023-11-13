package com.denuncias.controllers;

import java.util.Optional;

import com.denuncias.App;
import com.denuncias.models.entities.TipoUsuario;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.ComentarioRepository;
import com.denuncias.models.repositories.DenunciaRepository;
import com.denuncias.models.repositories.UsuarioRepository;
import com.github.hugoperlin.results.Resultado;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class FazerLogin {

    @FXML
    private StackPane pane;

    @FXML
    private Label lbEsqueciSenha;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private PasswordField pfSenha;

    @FXML
    private TextField tfEmail;

    private UsuarioRepository usuarioRepository;

    private DenunciaRepository denunciaRepository;

    private ComentarioRepository comentarioRepository;

    public FazerLogin(UsuarioRepository usuarioRepository, DenunciaRepository denunciaRepository,
            ComentarioRepository comentarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.denunciaRepository = denunciaRepository;
        this.comentarioRepository = comentarioRepository;
    }

    @FXML
    void autentificar(ActionEvent event) {

        String email = tfEmail.getText();
        String senha = pfSenha.getText();

        Task<Resultado> taskLogar = new Task<Resultado>() {

            @Override
            protected Resultado call() throws Exception {
                return usuarioRepository.logar(email, senha);
            }

        };

        taskLogar.setOnSucceeded(e -> {

            progressIndicator.setVisible(false);
            tfEmail.setVisible(true);
            pfSenha.setVisible(true);
            lbEsqueciSenha.setVisible(true);
            pane.setVisible(false);
            pane.setDisable(true);

            Resultado resultado = taskLogar.getValue();

            if (resultado.foiErro()) {
                Alert alert = new Alert(AlertType.ERROR, resultado.getMsg());
                alert.showAndWait();
            } else {
                tfEmail.clear();
                pfSenha.clear();

                Usuario usuario = (Usuario) resultado.comoSucesso().getObj();

                if (usuario.getTipo().getUsuario().equals("Admin")) {
                    App.pushScreen("MENUADM", o -> new MenuADM(comentarioRepository, denunciaRepository, usuarioRepository, usuario));
                } else if (usuario.getTipo().getUsuario().equals("Moderador")) {
                    App.pushScreen("MENUAMODERADOR",
                            o -> new MenuMODERADOR(comentarioRepository, usuarioRepository, denunciaRepository, usuario));
                } else {
                    App.pushScreen("MENUALUNO", o -> new MenuALUNO(usuarioRepository, usuario, denunciaRepository));
                }

            }
        });

        pane.setVisible(true);
        pane.setDisable(false);
        progressIndicator.setVisible(true);
        tfEmail.setVisible(false);
        pfSenha.setVisible(false);
        lbEsqueciSenha.setVisible(false);

        Thread thread = new Thread(taskLogar);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    void cadastrar(ActionEvent event) {
        App.pushScreen("CADASTRAR", o -> new Cadastrar(usuarioRepository, TipoUsuario.ALUNO));
    }

    @FXML
    void recuperarSenha(MouseEvent event) {
        App.pushScreen("RECUPERARSENHA", o -> new RecuperarSenha(usuarioRepository));
    }

    @FXML
    void fechar(ActionEvent event) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Você deseja mesmo sair da aplicação?");
        alert.setContentText("Escolha uma opção.");

        ButtonType simButton = new ButtonType("Sim");
        ButtonType naoButton = new ButtonType("Não");

        alert.getButtonTypes().setAll(simButton, naoButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == simButton) {
            System.exit(0);
        }

    }

}
