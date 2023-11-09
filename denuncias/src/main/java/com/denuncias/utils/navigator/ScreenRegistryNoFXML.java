package com.denuncias.utils.navigator;
import javafx.scene.Parent;
import javafx.util.Callback;

public class ScreenRegistryNoFXML implements ScreenRegistry{
    
    private Callback construtor;
    
    public ScreenRegistryNoFXML(Callback construtor) {
        this.construtor = construtor;
    }

    public Parent getRoot(){
        return (Parent) construtor.call(null);
    }

    


}