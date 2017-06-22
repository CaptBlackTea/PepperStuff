package de.lilithwittmann.pepperandroid.api.model;

/**
 * Created by Yip on 7/10/2016.
 */

public class Config {

    public static final String TAG = "Nao Assistant";
    public static final String ERROR_URL = "999";

    public static String getSimpleName(Class cls){
        return "[" + cls.getSimpleName() + "] ";
    }


}