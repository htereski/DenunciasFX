package com.denuncias.controllers;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.denuncias.App;
import com.denuncias.models.entities.Denuncia;
import com.denuncias.models.entities.TipoStatus;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.ComentarioRepository;
import com.denuncias.models.repositories.DenunciaRepository;
import com.github.hugoperlin.results.Resultado;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.StackPane;

public class MostrarDenuncias implements Initializable {

    @FXML
    private ListView<Denuncia> lstDenuncias;

    @FXML
    private StackPane pane;

    @FXML
    private ProgressIndicator progressIndicator;

    private ComentarioRepository comentarioRepository;

    private DenunciaRepository denunciaRepository;

    private Usuario moderador;

    private List<Denuncia> denuncias;

    public MostrarDenuncias(ComentarioRepository comentarioRepository, DenunciaRepository denunciaRepository,
            Usuario moderador) {
        this.comentarioRepository = comentarioRepository;
        this.denunciaRepository = denunciaRepository;
        this.moderador = moderador;
    }

    public MostrarDenuncias() {
    }

    @FXML
    void detalhes(ActionEvent event) {

        Denuncia denuncia = lstDenuncias.getSelectionModel().getSelectedItem();

        if (denuncia != null) {
            Alert alert = new Alert(AlertType.NONE);

            alert.setTitle("Detalhes");
            alert.setHeaderText("Deseja fazer um comentário sobre essa denúncia?");
            alert.setContentText("Titulo: " + denuncia.getTitulo() + "\n" +
                    "Local: " + denuncia.getLocal() + "\n" +
                    "Descrição: " + denuncia.getDescricao() + "\n" +
                    "Tipo: " + denuncia.getTipo().getDenuncia().replace('_', ' ') + "\n" +
                    "Status: " + denuncia.getStatus().getStatus() + "\n" +
                    "Data: " + denuncia.getData() + "\n" +
                    "Hora: " + denuncia.getHora());

            ButtonType simButton = new ButtonType("Sim");
            ButtonType naoButton = new ButtonType("Não");

            alert.setHeight(400);
            alert.setWidth(200);

            alert.getButtonTypes().setAll(simButton, naoButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == simButton) {
                App.pushScreen("INSERIRCOMENTARIO",
                        o -> new InserirComentario(comentarioRepository, moderador, denuncia));

                lstDenuncias.getItems().clear();

                for (Denuncia d : denuncias) {
                    if (d.getStatus().compareTo(TipoStatus.ENCERRADO) != 0) {
                        lstDenuncias.getItems().add(d);
                    }
                }

            }

        }

    }

    @FXML
    void voltar(MouseEvent event) {
        App.popScreen();
    }

    private void carregar() {
        Task<Resultado> taskCarregarDenuncias = new Task<Resultado>() {

            @Override
            protected Resultado call() throws Exception {
                return denunciaRepository.abertas();
            }

        };

        taskCarregarDenuncias.setOnSucceeded(e -> {

            lstDenuncias.setVisible(true);
            pane.setVisible(false);
            pane.setDisable(true);
            progressIndicator.setDisable(true);
            progressIndicator.setVisible(false);

            Resultado resultado = taskCarregarDenuncias.getValue();

            if (resultado.foiErro()) {
                Alert alert = new Alert(AlertType.ERROR, resultado.getMsg());
                alert.showAndWait();
            } else {

                denuncias = (List) resultado.comoSucesso().getObj();

                if (denuncias.size() > 0) {
                    lstDenuncias.setCellFactory(cell -> new ListCell<Denuncia>() {

                        final Tooltip tooltip = new Tooltip();

                        @Override
                        protected void updateItem(Denuncia item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setText(null);
                                setTooltip(null);
                            } else {
                                setText(item.getId() + " | " + item.toString());

                                tooltip.setText(item.getId() + " | " + item.toString());
                                setTooltip(tooltip);
                            }
                        }
                    });

                    lstDenuncias.getItems().addAll(denuncias);
                } else {
                    lstDenuncias.setPlaceholder(new Label("Nenhuma denúncia no momento!"));
                }

            }
        });

        lstDenuncias.setVisible(false);
        pane.setVisible(true);
        pane.setDisable(false);
        progressIndicator.setDisable(false);
        progressIndicator.setVisible(true);

        Thread thread = new Thread(taskCarregarDenuncias);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        carregar();
    }

}
