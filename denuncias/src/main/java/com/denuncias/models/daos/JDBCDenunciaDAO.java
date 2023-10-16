package com.denuncias.models.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.denuncias.models.entities.Denuncia;
import com.denuncias.models.entities.TipoDenuncia;
import com.denuncias.models.entities.TipoStatus;
import com.denuncias.utils.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class JDBCDenunciaDAO implements DenunciaDAO {

    private static final String INSERT = "INSERT INTO denuncias(alunoId, titulo, descricao, tipo, local, status, data, hora) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_BY_ID = "SELECT * FROM denuncias WHERE id=?";

    private static final String ABERTAS = "SELECT id FROM denuncias WHERE status NOT LIKE 'ENCERRADO'";

    private static final String GET_BY_USUARIO = "SELECT id FROM denuncias WHERE alunoId=?";

    private FabricaConexoes fabricaConexoes;

    public JDBCDenunciaDAO(FabricaConexoes fabricaConexoes) {
        this.fabricaConexoes = fabricaConexoes;
    }

    @Override
    public Resultado criar(Denuncia denuncia) {
        try (Connection connection = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement(INSERT);

            pstm.setInt(1, denuncia.getAluno().getId());
            pstm.setString(2, denuncia.getTitulo());
            pstm.setString(3, denuncia.getDescricao());
            pstm.setString(4, denuncia.getTipo().getDenuncia().toUpperCase());
            pstm.setString(5, denuncia.getLocal());
            pstm.setString(6, denuncia.getStatus().getStatus().toUpperCase());
            pstm.setDate(7, Date.valueOf(denuncia.getData()));
            pstm.setTime(8, Time.valueOf(denuncia.getHora()));

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                int id = DBUtils.getLastId(pstm);
                denuncia.setId(id);
                return Resultado.sucesso("Denúncia cadastrada!", denuncia);
            }

            return Resultado.erro("Erro ao cadastrar denúncia!");
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado getById(int id) {
        try (Connection connection = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement(GET_BY_ID);

            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                String titulo = rs.getString("titulo");
                String descricao = rs.getString("descricao");
                String local = rs.getString("local");
                TipoDenuncia tipo = TipoDenuncia.valueOf(rs.getString("tipo"));
                TipoStatus status = TipoStatus.valueOf(rs.getString("status"));
                Date dataAux = rs.getDate("data");
                Time horaAux = rs.getTime("hora");

                LocalDate data = dataAux.toLocalDate();
                LocalTime hora = horaAux.toLocalTime();

                Denuncia denuncia = new Denuncia(id, null, titulo, descricao, local, tipo, status, null, data, hora);
                return Resultado.sucesso("Denúncias abertas!", denuncia);
            }

            return Resultado.erro("Denúncia não encontrada!");
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado getAbertas() {
        try (Connection connection = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement(ABERTAS);

            ResultSet rs = pstm.executeQuery();

            List<Denuncia> denuncias = new ArrayList<>();

            while (rs.next()) {
                Resultado resultado = getById(rs.getInt("id"));

                if (resultado.foiErro()) {
                    return Resultado.erro("Erro desconhecido!");
                }

                Denuncia denuncia = (Denuncia) resultado.comoSucesso().getObj();

                denuncias.add(denuncia);
            }

            return Resultado.sucesso("Denúncias abertas!", denuncias);
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado getByUsuarioId(int usuarioId) {
        try (Connection connection = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement(GET_BY_USUARIO);

            pstm.setInt(1, usuarioId);

            ResultSet rs = pstm.executeQuery();

            List<Denuncia> denuncias = new ArrayList<>();

            while (rs.next()) {

                Resultado resultado = getById(rs.getInt("id"));

                if (resultado.foiErro()) {
                    return Resultado.erro("Erro desconhecido!");
                }

                Denuncia denuncia = (Denuncia) resultado.comoSucesso().getObj();

                denuncias.add(denuncia);
            }

            return Resultado.sucesso("Denúncias do usuário!", denuncias);
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

}
