package com.patskevich.gpproject.configuration;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageMessage {

    private final static ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale(""));

    public static String getText(final String text){
        return bundle.getString(text);
    }
}
