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
import com.denuncias.models.entities.Usuario;
import com.denuncias.models.repositories.DenunciaRepository;
import com.github.hugoperlin.results.Resultado;

public class Test {
    public static void main(String[] args) {

        DenunciaDAO denunciaDAO = new JDBCDenunciaDAO(FabricaConexoes.getInstance());
    
        UsuarioDAO usuarioDAO = new JDBCUsuarioDAO(FabricaConexoes.getInstance());

        ComentarioDAO comentarioDAO = new JDBCComentarioDAO(FabricaConexoes.getInstance());

        DenunciaRepository denunciaRepository = new DenunciaRepository(denunciaDAO, usuarioDAO, comentarioDAO);

        Resultado r1 = denunciaRepository.porUsuario(2);
        

        if (r1.foiErro()) {
            System.out.println(r1.getMsg());
        } else {
            List<Denuncia> denuncias = (List) r1.comoSucesso().getObj();

            for (Denuncia denuncia : denuncias) {
                System.out.println(denuncia.getTitulo() + " % " + denuncia.getDescricao() + " % " + denuncia.getStatus() + " % " + denuncia.getAluno().getNome());
                
                for (Comentario comentario : denuncia.getComentarios()) {
                    System.out.println("-----------------------------");
                    System.out.println(comentario.getConteudo() + " % " + comentario.getModerador().getNome());
                    System.out.println("-----------------------------");
                }
            }
        }

    }
}
