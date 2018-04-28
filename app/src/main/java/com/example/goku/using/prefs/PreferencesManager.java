package com.example.goku.using.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Goku on 24/06/2017.
 */

public class PreferencesManager {

    public static final String PREF_ID = "using";

    public  void setSharedPrefs(Context contexto,
                                String nomeProjeto,
                                String chave,
                                String valor){
        SharedPreferences sharedPreferences;
        sharedPreferences = contexto.getSharedPreferences(nomeProjeto, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(chave, valor);
        editor.apply();
    }

    public String getSharedPrefs(Context contexto,
                                  String nomeProjeto,
                                  String chave) {
        SharedPreferences sharedPreferences;
        sharedPreferences = contexto.getSharedPreferences(nomeProjeto, Context.MODE_PRIVATE);
        return sharedPreferences.getString(chave, null);
    }

    public static void setBoolean(Context context, String chave, boolean on){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(chave, on);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String chave){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        boolean b = pref.getBoolean(chave, true);
        return b;
    }

    public static void setInteger(Context context, String chave, int valor){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(chave, valor);
        editor.apply();
    }

    public static int getInt(Context context, String chave){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        int b = pref.getInt(chave, 0);
        return b;
    }

    public static void setString (Context context, String chave, String valor){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(chave, valor);
        editor.apply();
    }

    public static String getString(Context context, String chave){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        String b = pref.getString(chave, "");
        return b;
    }

}
