package com.denuncias;

import com.denuncias.controllers.Cadastrar;
import com.denuncias.controllers.DenunciaDetalhada;
import com.denuncias.controllers.FazerDenuncia;
import com.denuncias.controllers.FazerLogin;
import com.denuncias.controllers.MenuADM;
import com.denuncias.controllers.MenuALUNO;
import com.denuncias.controllers.MenuMODERADOR;
import com.denuncias.controllers.RecuperarSenha;
import com.denuncias.controllers.SuasDenuncias;
import com.denuncias.controllers.TrocarSenha;
import com.denuncias.models.daos.ComentarioDAO;
import com.denuncias.models.daos.DenunciaDAO;
import com.denuncias.models.daos.FabricaConexoes;
import com.denuncias.models.daos.JDBCComentarioDAO;
import com.denuncias.models.daos.JDBCDenunciaDAO;
import com.denuncias.models.daos.JDBCUsuarioDAO;
import com.denuncias.models.daos.UsuarioDAO;
import com.denuncias.models.repositories.ComentarioRepository;
import com.denuncias.models.repositories.ComentarioRepositoryImpl;
import com.denuncias.models.repositories.DenunciaRepository;
import com.denuncias.models.repositories.DenunciaRepositoryImpl;
import com.denuncias.models.repositories.UsuarioRepository;
import com.denuncias.models.repositories.UsuarioRepositoryImpl;
import com.denuncias.utils.navigator.BaseAppNavigator;
import com.denuncias.utils.navigator.ScreenRegistryFXML;

/**
 * JavaFX App
 */
public class App extends BaseAppNavigator {

    private UsuarioDAO usuarioDAO = new JDBCUsuarioDAO(FabricaConexoes.getInstance());

    private UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl(usuarioDAO);

    private ComentarioDAO comentarioDAO = new JDBCComentarioDAO(FabricaConexoes.getInstance());

    private ComentarioRepository comentarioRepository = new ComentarioRepositoryImpl(comentarioDAO);

    private DenunciaDAO denunciaDAO = new JDBCDenunciaDAO(FabricaConexoes.getInstance());

    private DenunciaRepository denunciaRepository = new DenunciaRepositoryImpl(denunciaDAO, usuarioDAO, comentarioDAO);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public String getHome() {
        return "FAZERLOGIN";
    }

    @Override
    public String getAppTitle() {
        return "Denúncias";
    }

    @Override
    public void registrarTelas() {
        registraTela("FAZERLOGIN", new ScreenRegistryFXML(App.class, "fazer_login.fxml", o -> new FazerLogin(usuarioRepository, denunciaRepository)));
        registraTela("CADASTRAR", new ScreenRegistryFXML(App.class, "cadastrar.fxml", o -> new Cadastrar()));
        registraTela("RECUPERARSENHA", new ScreenRegistryFXML(App.class, "recuperar_senha.fxml", o -> new RecuperarSenha()));
        registraTela("MENUADM", new ScreenRegistryFXML(App.class, "menuADM.fxml", o -> new MenuADM()));
        registraTela("MENUALUNO", new ScreenRegistryFXML(App.class, "menuALUNO.fxml", o -> new MenuALUNO()));
        registraTela("MENUAMODERADOR", new ScreenRegistryFXML(App.class, "menuMODERADOR.fxml", o -> new MenuMODERADOR()));
        registraTela("FAZERDENUNCIA", new ScreenRegistryFXML(App.class, "fazer_denuncia.fxml", o -> new FazerDenuncia()));
        registraTela("SUASDENUNCIAS", new ScreenRegistryFXML(App.class, "suas_denuncias.fxml", o -> new SuasDenuncias()));
        registraTela("DENUNCIADETALHADA", new ScreenRegistryFXML(App.class, "denuncia_detalhada.fxml", o -> new DenunciaDetalhada()));
        registraTela("TROCARSENHA", new ScreenRegistryFXML(App.class, "trocar_senha.fxml", o -> new TrocarSenha()));
    }

}