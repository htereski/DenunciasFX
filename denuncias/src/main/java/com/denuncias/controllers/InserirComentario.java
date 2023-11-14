package com.denuncias.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.denuncias.App;
import com.denuncias.models.entities.Comentario;
import com.denuncias.models.entities.Denuncia;
import com.denuncias.models.entities.TipoStatus;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.ComentarioRepository;
import com.github.hugoperlin.results.Resultado;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;

public class InserirComentario {

    @FXML
    private MenuItem menuEnc;

    @FXML
    private MenuItem menuInv;

    @FXML
    private SplitMenuButton menuComentario;

    @FXML
    private StackPane pane;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private TextArea taComentario;

    private ComentarioRepository comentarioRepository;

    private Usuario moderador;

    private Denuncia denuncia;

    private TipoStatus tipoStatus;

    public InserirComentario(ComentarioRepository comentarioRepository, Usuario moderador, Denuncia denuncia) {
        this.comentarioRepository = comentarioRepository;
        this.moderador = moderador;
        this.denuncia = denuncia;
    }

    public InserirComentario() {
    }

    @FXML
    void enviar(ActionEvent event) {

        String conteudo = taComentario.getText();
        LocalDate data = LocalDate.now();
        LocalTime hora = LocalTime.now();

        Task<Resultado> taskEnviarComentario = new Task<Resultado>() {

            @Override
            protected Resultado call() throws Exception {
                return comentarioRepository.cadastrar(conteudo, moderador, tipoStatus, data, hora, denuncia.getId());
            }

        };

        taskEnviarComentario.setOnSucceeded(e -> {

            mostrarComponentes();

            Resultado resultado = taskEnviarComentario.getValue();

            Alert alert;

            if (resultado.foiErro()) {
                alert = new Alert(AlertType.ERROR, resultado.getMsg());
            } else {
                alert = new Alert(AlertType.CONFIRMATION, resultado.getMsg());

                Comentario comentario = (Comentario) resultado.comoSucesso().getObj();

                List<Comentario> c = denuncia.getComentarios();
                c.add(comentario);

                denuncia.setComentarios(c);
                denuncia.setStatus(comentario.getStatus());

                taComentario.clear();
            }
            alert.showAndWait();

        });

        esconderComponentes();

        Thread thread = new Thread(taskEnviarComentario);
        thread.setDaemon(true);
        thread.start();
    }

    private void esconderComponentes() {
        taComentario.setVisible(false);
        pane.setDisable(false);
        pane.setVisible(true);
        progressIndicator.setDisable(false);
        progressIndicator.setVisible(true);
    }

    private void mostrarComponentes() {
        taComentario.setVisible(true);
        pane.setDisable(true);
        pane.setVisible(false);
        progressIndicator.setDisable(true);
        progressIndicator.setVisible(false);
    }

    @FXML
    void opcaoEncerramento(ActionEvent event) {
        menuComentario.setText(menuEnc.getText());
        tipoStatus = TipoStatus.ENCERRADO;
    }

    @FXML
    void opcaoInvestigativo(ActionEvent event) {
        menuComentario.setText(menuInv.getText());
        tipoStatus = TipoStatus.INVESTIGANDO;
    }

    @FXML
    void voltar(MouseEvent event) {
        App.popScreen();
    }
}
