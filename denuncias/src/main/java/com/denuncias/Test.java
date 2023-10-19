package com.denuncias;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.denuncias.models.daos.ComentarioDAO;
import com.denuncias.models.daos.DenunciaDAO;
import com.denuncias.models.daos.FabricaConexoes;
import com.denuncias.models.daos.JDBCComentarioDAO;
import com.denuncias.models.daos.JDBCDenunciaDAO;
import com.denuncias.models.daos.JDBCUsuarioDAO;
import com.denuncias.models.daos.UsuarioDAO;
import com.denuncias.models.entities.Comentario;
import com.denuncias.models.entities.Denuncia;
import com.denuncias.models.entities.TipoDenuncia;
import com.denuncias.models.entities.TipoStatus;
import com.denuncias.models.entities.TipoUsuario;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.DenunciaRepository;
import com.denuncias.models.repositories.UsuarioRepository;
import com.github.hugoperlin.results.Resultado;

public class Test {
    public static void main(String[] args) {

        DenunciaDAO denunciaDAO = new JDBCDenunciaDAO(FabricaConexoes.getInstance());
    
        UsuarioDAO usuarioDAO = new JDBCUsuarioDAO(FabricaConexoes.getInstance());

        ComentarioDAO comentarioDAO = new JDBCComentarioDAO(FabricaConexoes.getInstance());

        UsuarioRepository usuarioRepository = new UsuarioRepository(usuarioDAO);
      

        Resultado r1 = usuarioRepository.cadastrar("Betinho", "betinho@gmail.com", "12345678", TipoUsuario.MODERADOR);

        System.out.println(r1.getMsg());

    }
}
