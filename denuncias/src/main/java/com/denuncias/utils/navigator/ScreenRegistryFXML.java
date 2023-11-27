package com.denuncias.utils.navigator;

import javafx.scene.Parent;
import javafx.util.Callback;

public class ScreenRegistryFXML implements ScreenRegistry{
    
    private final Class classe;
    private final String fxml;
    private  Callback controler;
    
    public ScreenRegistryFXML(Class classe, String fxml, Callback controler) {
        this.classe = classe;
        this.fxml = fxml;
        this.controler = controler;
    }

    public void setControler(Callback controler){
        this.controler = controler;
    }

    public Parent getRoot(){
        String resourcePath = "/com/denuncias/fxml/" + fxml;
        return LoaderFXML.loadFxml(classe.getResource(resourcePath), controler);
    }
    
}
