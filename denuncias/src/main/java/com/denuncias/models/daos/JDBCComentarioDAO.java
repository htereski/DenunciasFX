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

import com.denuncias.models.entities.Comentario;
import com.denuncias.models.entities.TipoStatus;
import com.denuncias.utils.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class JDBCComentarioDAO implements ComentarioDAO {

    private static final String INSERT = "INSERT INTO comentarios(moderadorId, denunciaId, conteudo, status, data, hora) VALUES(?, ?, ?, ?, ?, ?)";

    private static final String GET_BY_DENUNCIA_ID = "SELECT * FROM comentarios WHERE comentarios.denunciaId=?";

    private FabricaConexoes fabricaConexoes;

    public JDBCComentarioDAO(FabricaConexoes fabricaConexoes) {
        this.fabricaConexoes = fabricaConexoes;
    }

    @Override
    public Resultado criar(Comentario comentario, int denunciaId) {
        try (Connection connection = fabricaConexoes.getConnection()) {
            
            PreparedStatement pstm = connection.prepareStatement(INSERT);

            pstm.setInt(1, comentario.getModerador().getId());
            pstm.setInt(2, denunciaId);
            pstm.setString(3, comentario.getConteudo());
            pstm.setString(4, comentario.getStatus().getStatus().toUpperCase());
            pstm.setDate(5, Date.valueOf(comentario.getData()));
            pstm.setTime(6, Time.valueOf(comentario.getHora()));

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                int id = DBUtils.getLastId(pstm);
                comentario.setId(id);
                return Resultado.sucesso("Comentário cadastrado!", comentario);
            }

            return Resultado.erro("Erro ao cadastrar comentário!");
        } catch (SQLException e) {
            return Resultado.erro("Não foi possível se conectar ao banco de dados.");
        }
    }

    @Override
    public Resultado getByDenunciaId(int denunciaId) {
        try (Connection connection = fabricaConexoes.getConnection()) {
            
            PreparedStatement pstm = connection.prepareStatement(GET_BY_DENUNCIA_ID);

            pstm.setInt(1, denunciaId);

            ResultSet rs = pstm.executeQuery();

            List<Comentario> comentarios = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String conteudo = rs.getString("conteudo");
                TipoStatus status = TipoStatus.valueOf(rs.getString("status"));
                Date dataAux = rs.getDate("data");
                Time horaAux = rs.getTime("hora");

                LocalDate data = dataAux.toLocalDate();
                LocalTime hora = horaAux.toLocalTime();

                Comentario comentario = new Comentario(id, conteudo, null, status, data, hora);
                comentarios.add(comentario);
            }

            return Resultado.sucesso("Comentários", comentarios);
        } catch (SQLException e) {
            return Resultado.erro("Não foi possível se conectar ao banco de dados.");
        }
    }

}
