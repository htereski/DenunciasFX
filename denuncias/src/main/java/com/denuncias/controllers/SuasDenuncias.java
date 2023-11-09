package com.denuncias.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.denuncias.App;
import com.denuncias.models.entities.Denuncia;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.DenunciaRepository;
import com.github.hugoperlin.results.Resultado;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert.AlertType;

public class SuasDenuncias implements Initializable {

    @FXML
    private StackPane pane;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private ListView<Denuncia> lstDenuncias;

    private Usuario usuario;

    private DenunciaRepository denunciaRepository;

    public SuasDenuncias(Usuario usuario, DenunciaRepository denunciaRepository) {
        this.usuario = usuario;
        this.denunciaRepository = denunciaRepository;
    }

    public SuasDenuncias() {
    }

    @FXML
    void detalhes(ActionEvent event) {

        Denuncia denuncia = lstDenuncias.getSelectionModel().getSelectedItem();

        if (denuncia != null) {
            App.pushScreen("DENUNCIADETALHADA", o -> new DenunciaDetalhada(denuncia));
        }

    }

    @FXML
    void voltar(ActionEvent event) {
        App.popScreen();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        Task<Resultado> taskObterDenuncias = new Task<Resultado>() {

            @Override
            protected Resultado call() throws Exception {
                return denunciaRepository.porUsuario(usuario.getId());
            }

        };

        taskObterDenuncias.setOnSucceeded(e -> {
            Resultado resultado = taskObterDenuncias.getValue();

            pane.setDisable(true);
            pane.setVisible(false);
            progressIndicator.setVisible(false);
            progressIndicator.setDisable(true);
            lstDenuncias.setVisible(true);
            lstDenuncias.setDisable(false);

            if (resultado.foiErro()) {
                Alert alert = new Alert(AlertType.INFORMATION, resultado.getMsg());
                alert.showAndWait();
                return;
            }

            List<Denuncia> denuncias = (List) resultado.comoSucesso().getObj();

            if (denuncias.size() > 0) {
                for (Denuncia denuncia : denuncias) {
                    lstDenuncias.getItems().add(denuncia);
                }
            } else {
                Alert alert = new Alert(AlertType.INFORMATION, "Você não possui denúncias registradas!");
                alert.showAndWait();
            }
        });

        lstDenuncias.setVisible(false);
        lstDenuncias.setDisable(true);

        pane.setDisable(false);
        pane.setVisible(true);
        progressIndicator.setDisable(false);
        progressIndicator.setVisible(true);

        Thread thread = new Thread(taskObterDenuncias);
        thread.setDaemon(true);
        thread.start();
    }

}
