package com.denuncias; 

import com.denuncias.models.daos.FabricaConexoes;
import com.denuncias.models.daos.JDBCUsuarioDAO;
import com.denuncias.models.daos.UsuarioDAO;
import com.denuncias.models.repositories.UsuarioRepository;
import com.github.hugoperlin.results.Resultado;

public class Test {
    public static void main(String[] args) {
    
        UsuarioDAO usuarioDAO = new JDBCUsuarioDAO(FabricaConexoes.getInstance());

        UsuarioRepository usuarioRepository = new UsuarioRepository(usuarioDAO);
      

        Resultado r1 = usuarioRepository.recuperarSenha("tereski84@gmail.com");

        System.out.println(r1.getMsg());

    }
}
