package com.denuncias.models.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.denuncias.models.entities.TipoUsuario;
import com.denuncias.models.entities.Usuario;
import com.denuncias.utils.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class JDBCUsuarioDAO implements UsuarioDAO {

    private static final String INSERT = "INSERT IGNORE INTO usuarios(nome, email, senha, tipo) VALUES(?, ?, ?, ?)";

    private static final String GET_MODERADORES = "SELECT id, nome, email FROM usuarios WHERE tipo=? AND ativo=1";

    private static final String GET_BY_ID = "SELECT * FROM usuarios WHERE id=?";

    private static final String GET_MODERADOR_BY_COMENTARIO = "SELECT moderadorId FROM comentarios WHERE id=?";

    private static final String GET_ALUNO_BY_DENUNCIA = "SELECT alunoId FROM denuncias WHERE id=?";

    private static final String LOGAR = "SELECT * FROM usuarios WHERE email=? AND senha=? AND ativo=1";

    private static final String ALTERAR_SENHA = "CALL trocarSenha(?, ?, ?)";

    private static final String RECUPERAR_CONTA = "CALL recuperarConta(?, ?, ?)";

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

            return Resultado.erro("Email já cadastrado!");
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado getModeradores() {
        try (Connection connection = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement(GET_MODERADORES);

            pstm.setString(1, "MODERADOR");

            ResultSet rs = pstm.executeQuery();

            List<Usuario> moderadores = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");

                Usuario moderador = new Usuario(id, nome, email, null, TipoUsuario.MODERADOR);
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
    public Resultado getByComentarioId(int comentarioId) {
        try (Connection connection = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement(GET_MODERADOR_BY_COMENTARIO);

            pstm.setInt(1, comentarioId);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                return getById(rs.getInt("moderadorId"));
            }

            return Resultado.erro("Falha ao encontrar moderador!");
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado getByDenunciaId(int denunciaId) {
        try (Connection connection = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = connection.prepareStatement(GET_ALUNO_BY_DENUNCIA);

            pstm.setInt(1, denunciaId);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                return getById(rs.getInt("alunoId"));
            }

            return Resultado.erro("Falha ao encontrar aluno!");
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado logar(String email, String senha) {
        try (Connection connection = fabricaConexoes.getConnection()) {
            
            PreparedStatement pstm = connection.prepareStatement(LOGAR);

            pstm.setString(1, email);
            pstm.setString(2, senha);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                TipoUsuario tipoUsuario = TipoUsuario.valueOf(rs.getString("tipo"));

                Usuario usuario = new Usuario(id, nome, email, senha, tipoUsuario);
                return Resultado.sucesso("Usuário encontrado!", usuario);
            }

            return Resultado.erro("Email ou senha inválidos!");
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado alterarSenha(int id, String senha) {
        try (Connection connection = fabricaConexoes.getConnection()) {

            CallableStatement call = connection.prepareCall(ALTERAR_SENHA);

            call.setInt(1, id);
            call.setString(2, senha);
            call.registerOutParameter(3, Types.TINYINT);

            call.execute();

            int res = call.getInt(3);

            if (res == 0) {
                return Resultado.sucesso("Senha alterada!", res);
            }

            return Resultado.erro("Não foi possivel trocar a senha!");
        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado recuperarConta(String email) {
        try (Connection connection = fabricaConexoes.getConnection()) {

            CallableStatement call = connection.prepareCall(RECUPERAR_CONTA);

            call.setString(1, email);
            call.registerOutParameter(2, Types.VARCHAR);
            call.registerOutParameter(3, Types.VARCHAR);

            call.execute();

            String nome = call.getString(2);
            String senha = call.getString(3);

            if (nome.equals("Inválido.")) {
                return Resultado.erro("Email não encontrado");
            }

            Usuario usuario = new Usuario(nome, email, senha, null);

            return Resultado.sucesso("Conta recuperada!", usuario);
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
