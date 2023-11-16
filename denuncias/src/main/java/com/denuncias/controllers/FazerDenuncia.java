package com.denuncias.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.denuncias.App;
import com.denuncias.models.entities.TipoDenuncia;
import com.denuncias.models.entities.TipoStatus;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.DenunciaRepository;
import com.github.hugoperlin.results.Resultado;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import jfxtras.scene.control.LocalTimePicker;

public class FazerDenuncia implements Initializable {

    @FXML
    private AnchorPane anchor;

    @FXML
    private TextField tfLocal;

    @FXML
    private ComboBox<String> cbDenuncia;

    @FXML
    private DatePicker dpData;

    @FXML
    private TextArea taDescricao;

    @FXML
    private Label lbTipo;

    @FXML
    private Label lbHora;

    @FXML
    private TextField tfTitulo;

    @FXML
    private StackPane paneIndicator;

    @FXML
    private ProgressIndicator progressIndicator;

    private LocalTimePicker localTimePicker;

    private Usuario aluno;

    private DenunciaRepository denunciaRepository;

    public FazerDenuncia(Usuario aluno, DenunciaRepository denunciaRepository) {
        this.aluno = aluno;
        this.denunciaRepository = denunciaRepository;
    }

    public FazerDenuncia() {
    }

    @FXML
    void confirmar(ActionEvent event) {

        String local = tfLocal.getText();
        String titulo = tfTitulo.getText();
        String descricao = taDescricao.getText();
        LocalDate data = dpData.getValue();
        LocalTime hora = localTimePicker.getLocalTime();

        TipoDenuncia tipo;

        if (cbDenuncia.getSelectionModel().getSelectedItem() != null) {

            String auxDenuncia = cbDenuncia.getSelectionModel().getSelectedItem().replace(' ', '_').replace('é', 'e')
                    .toUpperCase();

            tipo = TipoDenuncia.valueOf(auxDenuncia);
        } else {
            tipo = null;
        }

        Task<Resultado> taskCadastrar = new Task<Resultado>() {

            @Override
            protected Resultado call() throws Exception {
                return denunciaRepository.cadastrar(aluno, titulo, descricao, local, tipo,
                        TipoStatus.REGISTRADO, data, hora);
            }

        };

        taskCadastrar.setOnSucceeded(e -> {
            progressIndicator.setVisible(false);
            paneIndicator.setVisible(false);

            lbTipo.setVisible(true);
            taDescricao.setVisible(true);
            cbDenuncia.setVisible(true);
            tfTitulo.setVisible(true);
            tfLocal.setVisible(true);
            lbHora.setVisible(true);
            tfLocal.setVisible(true);
            dpData.setVisible(true);
            localTimePicker.setVisible(true);

            Resultado resultado = taskCadastrar.getValue();

            Alert alert;

            if (resultado.foiErro()) {
                alert = new Alert(AlertType.ERROR, resultado.getMsg());
            } else {
                tfLocal.clear();
                tfTitulo.clear();
                dpData.setValue(null);
                taDescricao.clear();
                cbDenuncia.setValue(null);
                alert = new Alert(AlertType.CONFIRMATION, resultado.getMsg());
            }
            alert.showAndWait();
        });

        cbDenuncia.setVisible(false);
        taDescricao.setVisible(false);
        lbTipo.setVisible(false);
        tfTitulo.setVisible(false);
        tfLocal.setVisible(false);
        lbHora.setVisible(false);
        tfLocal.setVisible(false);
        dpData.setVisible(false);
        localTimePicker.setVisible(false);

        paneIndicator.setVisible(true);
        paneIndicator.setDisable(false);
        progressIndicator.setVisible(true);
        progressIndicator.setDisable(false);

        Thread thread = new Thread(taskCadastrar);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    void voltar(MouseEvent event) {
        App.popScreen();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        for (TipoDenuncia denuncia : TipoDenuncia.values()) {
            String tipo = denuncia.getDenuncia().replace('_', ' ');
            cbDenuncia.getItems().add(tipo);
        }

        localTimePicker = new LocalTimePicker();
        localTimePicker.setLayoutX(100);
        localTimePicker.setLayoutY(287);
        anchor.getChildren().add(localTimePicker);
    }

}
