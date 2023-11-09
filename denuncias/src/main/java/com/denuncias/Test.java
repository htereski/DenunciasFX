package com.denuncias;

import java.util.List;

import com.denuncias.models.daos.ComentarioDAO;
import com.denuncias.models.daos.DenunciaDAO;
import com.denuncias.models.daos.FabricaConexoes;
import com.denuncias.models.daos.JDBCComentarioDAO;
import com.denuncias.models.daos.JDBCDenunciaDAO;
import com.denuncias.models.daos.JDBCUsuarioDAO;
import com.denuncias.models.daos.UsuarioDAO;
import com.denuncias.models.entities.Denuncia;
import com.denuncias.models.repositories.DenunciaRepository;
import com.denuncias.models.repositories.DenunciaRepositoryImpl;
import com.github.hugoperlin.results.Resultado;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;

public class Test {

    @FXML
    private ListView<String> lstDenuncias;

    @FXML
    private ProgressIndicator progressIndicator;

    private DenunciaDAO denunciaDAO = new JDBCDenunciaDAO(FabricaConexoes.getInstance());

    private UsuarioDAO usuarioDAO = new JDBCUsuarioDAO(FabricaConexoes.getInstance());

    private ComentarioDAO comentarioDAO = new JDBCComentarioDAO(FabricaConexoes.getInstance());

    private DenunciaRepository denunciaRepository = new DenunciaRepositoryImpl(denunciaDAO, usuarioDAO, comentarioDAO);

    @FXML
    void mostrar(ActionEvent event) {
        Task<List<Denuncia>> load = new Task<List<Denuncia>>() {
            @Override
            protected List<Denuncia> call() throws Exception {
                // Realize a operação no banco de dados aqui
                Resultado r1 = denunciaRepository.abertas();
                return (List<Denuncia>) r1.comoSucesso().getObj();
            }
        };

        load.setOnSucceeded(e -> {
            progressIndicator.setVisible(false);

            List<Denuncia> denuncias = load.getValue();
            for (Denuncia denuncia : denuncias) {
                lstDenuncias.getItems().add(denuncia.getTitulo());
            }
        });

        progressIndicator.setVisible(true);

        Thread th1 = new Thread(load);
        th1.setDaemon(true);
        th1.start();
    }

}
