package com.denuncias.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.denuncias.models.entities.TipoUsuario;
import com.denuncias.models.entities.Usuario;
import com.denuncias.utils.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class JDBCUsuarioDAO implements UsuarioDAO {

    private static final String INSERT = "INSERT INTO usuarios(nome, email, senha, tipo) VALUES(?, ?, ?, ?)";

    private static final String GET_MODERADORES = "SELECT id, nome FROM usuarios WHERE tipo=MODERADOR AND ativo=1";

    private static final String GET_BY_ID = "SELECT * FROM usuarios WHERE id=? AND ativo=1";

    private static final String EXCLUIR = "UPDATE usuarios SET ativo=0 WHERE id=?";

    private FabricaConexoes fabricaConexoes;

    public JDBCUsuarioDAO(FabricaConexoes fabricaConexoes) {
        this.fabricaConexoes = fabricaConexoes;
    }

    @Override
    public Resultado criar(Usuario usuario) {
        try (Connection connection = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement(INSERT);

            pstm.setString(1, usuario.getNome());
            pstm.setString(2, usuario.getEmail());
            pstm.setString(3, usuario.getSenha());
            pstm.setString(4, usuario.getTipo().getUsuario().toUpperCase());

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                int id = DBUtils.getLastId(pstm);
                usuario.setId(id);
                return Resultado.sucesso("Usuário cadastrado!", usuario);
            }

            return Resultado.erro("Falha ao inserir usuário!");
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado getModeradores() {
        try (Connection connection = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement(GET_MODERADORES);

            ResultSet rs = pstm.executeQuery();

            List<Usuario> moderadores = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");

                Usuario moderador = new Usuario(id, nome, null, null, TipoUsuario.MODERADOR);
                moderadores.add(moderador);
            }

            return Resultado.sucesso("Moderadores", moderadores);
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
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("senha");
                TipoUsuario tipoUsuario = TipoUsuario.valueOf(rs.getString("tipo"));

                Usuario usuario = new Usuario(id, nome, email, senha, tipoUsuario);
                return Resultado.sucesso("Usuário encontrado!", usuario);
            }

            return Resultado.erro("Falha ao encontrar usuário!");
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado excluir(int id) {
        try (Connection connection = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement(EXCLUIR);

            pstm.setInt(1, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Usuário excluído!", null);
            }

            return Resultado.erro("Falha ao encontrar usuário!");
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

}