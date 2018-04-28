package com.example.goku.using.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Goku on 26/06/2017.
 */

public class UsingDB extends SQLiteOpenHelper {

    private static final String TAG = "LOG";

    //Nome do banco
    private static final String NOME_BANCO = "banco_using.sqlite";
    private static final int VERSAO_BANCO = 1;

    public UsingDB(Context context) {
        super(context, NOME_BANCO,null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists location (id integer primary key autoincrement," +
                " latitude double, longitude double);");
        db.execSQL("create table if not exists produto (id integer primary key autoincrement," +
                " desc_produto text, title text," +
                " preco text, produtologo text);");
        db.execSQL("create table if not exists maps_json (id integer primary key autoincrement, " +
                "json text, json_espec)");
        db.execSQL("create table if not exists loja_frag (id integer primary key autoincrement, " +
                "json text text)");
    }


    // ------------------- LOJA FRAGMENT ----------------------------
    public void updateLojaFrag(String json){
        SQLiteDatabase db = getWritableDatabase();

        try {
            Cursor c = db.query("loja_frag", null,null,null,null,null,null,null);

            if (c.moveToNext()){
                ContentValues values = new ContentValues();
                values.put("json", json);

                String id = "1";
                String[] whereArgs = new String[]{id};
                db.update("loja_frag", values, "id=?", whereArgs);

                Log.d(TAG, "Update LojaFragment: dados atualizados com sucesso!");
            }else {
                ContentValues values = new ContentValues();
                values.put("json", json);

                db.insert("loja_frag", "", values);

                Log.d(TAG, "Insert LojaFragment: Dados inseridos com sucesso!");
            }
        }finally {
            db.close();
        }
    }

    public String selectJsonLojaFrag(){
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query("loja_frag", null,null,null,null,null,null,null);
            c.moveToPosition(0);
            return c.getString(c.getColumnIndex("json"));
        }finally {
            db.close();
        }
    }
    // ==============================================================

    // ------------------- MAPS ACTIVITYY ---------------------------
    public void updateMaps(String json){
        SQLiteDatabase db = getWritableDatabase();

        try {
            Cursor c = db.query("maps_json", null,null,null,null,null,null,null);

            if (c.moveToNext()){
                ContentValues values = new ContentValues();
                values.put("json", json);

                String id = "1";
                String[] whereArgs = new String[]{id};
                db.update("maps_json", values, "id=?", whereArgs);

                Log.d(TAG, "Update MapsActivity: dados atualizados com sucesso!");
            }else {
                ContentValues values = new ContentValues();
                values.put("json", json);

                db.insert("maps_json", "", values);

                Log.d(TAG, "Insert MapsActivity: Dados inseridos com sucesso!");
            }
        }finally {
            db.close();
        }
    }

    public String selectJsonMaps(){
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query("maps_json", null,null,null,null,null,null,null);
            if(c.moveToFirst()) {
                return c.getString(c.getColumnIndex("json"));
            }else{
                Log.d(TAG, "selectJsonMaps: banco vazio.");
                return null;
            }
        }finally {
            db.close();
        }
    }

    public void updateEspecMaps(String json){
        SQLiteDatabase db = getWritableDatabase();

        try {
            Cursor c = db.query("maps_json", null,null,null,null,null,null,null);

            if (c.moveToNext()){
                ContentValues values = new ContentValues();
                values.put("json_espec", json);

                String id = "1";
                String[] whereArgs = new String[]{id};
                db.update("maps_json", values, "id=?", whereArgs);

                Log.d(TAG, "updateEspecMaps MapsActivity: dados atualizados com sucesso!");
            }else {
                ContentValues values = new ContentValues();
                values.put("json_espec", json);

                db.insert("maps_json", "", values);

                Log.d(TAG, "updateEspecMaps: Insert MapsActivity: Dados inseridos com sucesso!");
            }
        }finally {
            db.close();
        }
    }

    public String selectEspecMaps(){
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query("maps_json", null,null,null,null,null,null,null);
           if (c.moveToFirst()) {
               return c.getString(c.getColumnIndex("json_espec"));
           }else{
               Log.d(TAG, "selectEspecMaps: banco vazio.");
               return null;
           }
        }finally {
            db.close();
        }
    }
    // ==============================================================

    //  ----------------- PRODUTO ACTIVITY --------------------------
    public void updateProduto(String title, String preco, String logo, String desc){
        SQLiteDatabase db = getWritableDatabase();

        try {
            Cursor c = db.query("produto", null,null,null,null,null,null,null);

            if (c.moveToNext()){
                ContentValues values = new ContentValues();
                values.put("desc_produto", desc);
                values.put("title", title);
                values.put("preco", preco);
                values.put("produtologo", logo);

                String id = "1";
                String[] whereArgs = new String[]{id};
                db.update("produto", values, "id=?", whereArgs);

                Log.d(TAG, "update ProdutoActivity: dados atualizados com sucesso!");
            }else {
                ContentValues values = new ContentValues();
                values.put("desc_produto", "a");
                values.put("title", "a");
                values.put("preco", "a");
                values.put("produtologo", "a");

                db.insert("produto", "", values);

                Log.d(TAG, "updateProduto: Dados inseridos com sucesso!");
                updateProdutoNow(title, preco, logo, desc);
            }
        }finally {
            db.close();
        }
    }

    private void updateProdutoNow(String title, String preco, String logo, String desc){
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("desc_produto", desc);
            values.put("title", title);
            values.put("preco", preco);
            values.put("produtologo", logo);

            String id = "1";
            String[] whereArgs = new String[]{id};
            db.update("produto", values, "id=?", whereArgs);

            Log.d(TAG, "updateProdutoNow ProdutoActivity: dados atualizados com sucesso!");
        }finally {
            db.close();
        }
    }

    public CatSubFragDomain selectAllProduto(){
        SQLiteDatabase db = getWritableDatabase();

        try {
            Cursor c = db.query("produto", null,null,null,null,null,null,null);

            CatSubFragDomain ct = new CatSubFragDomain();

            while (c.moveToNext()){
                Log.d(TAG, "selectAll:\nID: "+ c.getInt(c.getColumnIndex("id"))
                        + "\nTítulo: " + c.getString(c.getColumnIndex("title"))
                        + "\nPreço: " + c.getString(c.getColumnIndex("preco"))
                        + "\nDescrição: " + c.getString(c.getColumnIndex("desc_produto"))
                        + "\nLogo: " + c.getString(c.getColumnIndex("produtologo")));

                ct.setTitle(c.getString(c.getColumnIndex("title")));
                ct.setPreco(c.getString(c.getColumnIndex("preco")));
                ct.setDesc(c.getString(c.getColumnIndex("desc_produto")));
                ct.setLogo(c.getString(c.getColumnIndex("produtologo")));

            }
            return ct;
        }finally {
            db.close();
        }
    }
    //====================================================================================

    public CatSubFragDomain select(){
        SQLiteDatabase db = getWritableDatabase();

        try {
            CatSubFragDomain ct;
            String id = "1";
            String[] whereArgs = new String[]{id};
            Cursor c = db.query("location",null,null,null,null,null,null,null);

                c.moveToPosition(0);
                Log.d(TAG, "select: Lat: " + c.getDouble(c.getColumnIndex("latitude"))
                        + " Longi: "  + c.getDouble(c.getColumnIndex("longitude")));


                 ct =  new CatSubFragDomain(c.getDouble(c.getColumnIndex("latitude")),
                        c.getDouble(c.getColumnIndex("longitude")));
            return ct;

        }catch (Exception e){
                Log.e(TAG, "select: deu ruim na seleção", e);
            return null;
        }finally {
            db.close();
        }
    }

    public void save(){
        SQLiteDatabase db = getWritableDatabase();

        try{
            ContentValues values = new ContentValues();
            values.put("latitude", 1);
            values.put("longitude", 1);

            db.insert("location", "", values);

            Log.d(TAG, "save: Dados inseridos com sucesso!");

        }catch (Exception e ){

        }
        finally {
            db.close();
        }
    }

    public void update(double latitude, double longitude){
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("latitude", latitude);
            values.put("longitude", longitude);

            String id = "1";
            String[] whereArgs = new String[]{id};
            db.update("location", values, "id=?", whereArgs);

            Log.d(TAG, "update: dados atualizados com sucesso!");

        }catch (Exception e){
            Log.e(TAG, "update: ", e );
        }finally {
            db.close();
        }
    }

    public void selectAll(){
        SQLiteDatabase db = getWritableDatabase();

        try {
            Cursor c = db.query("location", null,null,null,null,null,null,null);

            while (c.moveToNext()){
                Log.d(TAG, "selectAll: ID: "+ c.getInt(c.getColumnIndex("id"))
                        + " Lat: " + c.getDouble(c.getColumnIndex("latitude"))
                        + " Longi: " + c.getDouble(c.getColumnIndex("longitude")));
            }
        }finally {
            db.close();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
