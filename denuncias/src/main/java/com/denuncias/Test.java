package com.denuncias; 

import java.time.LocalDate;
import java.time.LocalTime;

import com.denuncias.models.daos.ComentarioDAO;
import com.denuncias.models.daos.FabricaConexoes;
import com.denuncias.models.daos.JDBCComentarioDAO;
import com.denuncias.models.entities.TipoStatus;
import com.denuncias.models.entities.TipoUsuario;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.ComentarioRepositoryImpl;
import com.github.hugoperlin.results.Resultado;

public class Test {
    public static void main(String[] args) {
    
        ComentarioDAO comentarioDAO = new JDBCComentarioDAO(FabricaConexoes.getInstance());
      
        ComentarioRepositoryImpl comentarioRepository = new ComentarioRepositoryImpl(comentarioDAO);

        Usuario moderador = new Usuario(12, "Leticia", "leticia@gmail.com", "12345678", TipoUsuario.MODERADOR);

        Resultado r1 = comentarioRepository.cadastrar("testando o comentario", moderador, TipoStatus.INVESTIGANDO, LocalDate.now(), LocalTime.now(), 8);

        System.out.println(r1.getMsg());

    }
}
