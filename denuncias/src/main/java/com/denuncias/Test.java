package com.denuncias; 

import java.time.LocalDate;
import java.time.LocalTime;

import com.denuncias.models.daos.ComentarioDAO;
import com.denuncias.models.daos.FabricaConexoes;
import com.denuncias.models.daos.JDBCComentarioDAO;
import com.denuncias.models.daos.JDBCUsuarioDAO;
import com.denuncias.models.daos.UsuarioDAO;
import com.denuncias.models.entities.TipoStatus;
import com.denuncias.models.entities.TipoUsuario;
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.ComentarioRepositoryImpl;
import com.denuncias.models.repositories.UsuarioRepository;
import com.denuncias.models.repositories.UsuarioRepositoryImpl;
import com.github.hugoperlin.results.Resultado;

public class Test {
    public static void main(String[] args) {
    
        UsuarioDAO usuarioDAO = new JDBCUsuarioDAO(FabricaConexoes.getInstance());

        UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl(usuarioDAO);

        Resultado r1 = usuarioRepository.recuperarSenha("Mateusgoncalvesjunior@gmail.com");

        System.out.println(r1.getMsg());

    }
}
