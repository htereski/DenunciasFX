package com.denuncias.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.denuncias.App;
import com.denuncias.models.entities.Comentario;
import com.denuncias.models.entities.Denuncia;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

public class DenunciaDetalhada implements Initializable {

    @FXML
    private Label lbData;

    @FXML
    private Label lbDetalhes;

    @FXML
    private Label lbHora;

    @FXML
    private Label lbStatus;

    @FXML
    private Label lbTitulo;

    @FXML
    private ListView<String> lstComentarios;

    @FXML
    private Tooltip toolDetalhes;

    @FXML
    private Tooltip toolTitulo;

    private Denuncia denuncia;

    public DenunciaDetalhada(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public DenunciaDetalhada() {
    }

    @FXML
    void voltar(MouseEvent event) {
        App.popScreen();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        lbTitulo.setText(denuncia.getTitulo());
        toolTitulo.setText(lbTitulo.getText());

        lbDetalhes.setText(denuncia.getDescricao());
        toolDetalhes.setText(lbDetalhes.getText());

        lbStatus.setText(denuncia.getStatus().getStatus());
        lbData.setText(String.valueOf(denuncia.getData()));
        lbHora.setText(String.valueOf(denuncia.getHora()));

        lstComentarios.setCellFactory(cell -> new ListCell<String>() {

            final Tooltip tooltip = new Tooltip();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(item);

                    tooltip.setText(item);
                    setTooltip(tooltip);
                }
            }
        });

        for (Comentario comentario : denuncia.getComentarios()) {
            String conteudo = comentario.getConteudo() + " | " + comentario.getStatus().getStatus();
            lstComentarios.getItems().add(conteudo);
        }

        if (denuncia.getComentarios().size() == 0) {
            lstComentarios.setPlaceholder(new Label("Nenhum comentário disponível ainda."));
        }
    }

}
