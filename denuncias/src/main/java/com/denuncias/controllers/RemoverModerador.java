package com.denuncias.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.denuncias.App;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.UsuarioRepository;
import com.github.hugoperlin.results.Resultado;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;

public class RemoverModerador implements Initializable {

    @FXML
    private ListView<Usuario> lstModeradores;

    @FXML
    private StackPane pane;

    @FXML
    private ProgressIndicator progressIndicator;

    private UsuarioRepository usuarioRepository;

    public RemoverModerador(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public RemoverModerador() {
    }

    @FXML
    void remover(ActionEvent event) {

        Usuario moderador = lstModeradores.getSelectionModel().getSelectedItem();

        if (moderador != null) {
            removerModerador(moderador);
        }
    }

    private void removerModerador(Usuario moderador) {

        Task<Resultado> taskRemoverModerador = new Task<Resultado>() {

            @Override
            protected Resultado call() throws Exception {
                return usuarioRepository.excluir(moderador.getId());
            }

        };

        taskRemoverModerador.setOnSucceeded(e -> {

            mostrarComponentes();

            Resultado resultado = taskRemoverModerador.getValue();

            Alert alert;

            if (resultado.foiErro()) {
                alert = new Alert(AlertType.ERROR, resultado.getMsg());
            } else {
                alert = new Alert(AlertType.CONFIRMATION, resultado.getMsg());
                lstModeradores.getItems().remove(moderador);
            }
            alert.showAndWait();
        });

        esconderComponentes();

        Thread thread = new Thread(taskRemoverModerador);
        thread.setDaemon(true);
        thread.start();
    }

    private void esconderComponentes() {
        lstModeradores.setVisible(false);
        pane.setDisable(false);
        pane.setVisible(true);
        progressIndicator.setDisable(false);
        progressIndicator.setVisible(true);
    }

    private void mostrarComponentes() {
        lstModeradores.setVisible(true);
        pane.setDisable(true);
        pane.setVisible(false);
        progressIndicator.setDisable(true);
        progressIndicator.setVisible(false);
    }

    @FXML
    void voltar(ActionEvent event) {
        App.popScreen();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        Task<Resultado> taskCarregarModeradores = new Task<Resultado>() {

            @Override
            protected Resultado call() throws Exception {
                return usuarioRepository.moderadores();
            }

        };

        taskCarregarModeradores.setOnSucceeded(e -> {

            mostrarComponentes();

            Resultado resultado = taskCarregarModeradores.getValue();

            if (resultado.foiErro()) {
                Alert alert = new Alert(AlertType.ERROR, resultado.getMsg());
                alert.showAndWait();
                lstModeradores.setPlaceholder(new Label("Nenhum moderador encontrado!"));
            } else {

                lstModeradores.setCellFactory(cell -> new ListCell<Usuario>() {

                    final Tooltip tooltip = new Tooltip();

                    @Override
                    protected void updateItem(Usuario item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                            setTooltip(null);
                        } else {
                            setText(item.toString());

                            tooltip.setText(item.toString());
                            setTooltip(tooltip);
                        }
                    }
                });

                List<Usuario> moderadores = (List) resultado.comoSucesso().getObj();

                lstModeradores.getItems().addAll(moderadores);

                if (moderadores.size() == 0) {
                    lstModeradores.setPlaceholder(new Label("Nenhum moderador encontrado!"));
                }
            }
        });

        esconderComponentes();

        Thread thread = new Thread(taskCarregarModeradores);
        thread.setDaemon(true);
        thread.start();
    }

}
