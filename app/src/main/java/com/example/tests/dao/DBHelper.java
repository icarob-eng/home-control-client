package com.example.tests.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "relays_db";
    public static String TABLE_RELAYS = "relays";
    public static int VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        criarTabelaRelay(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String dropRelays = "DROP TABLE IF EXISTS " + TABLE_RELAYS  + ";";

        try {
            sqLiteDatabase.execSQL( dropRelays );

            onCreate(sqLiteDatabase);
            Log.i("INFO DB", "Sucesso ao atualizar app");
        } catch (Exception e) {
            Log.e("INFO DB", "Falha ao atualizar app. " + e.getMessage());
        }
    }

    private void criarTabelaRelay(SQLiteDatabase db) {

        String sql =
                "CREATE TABLE IF NOT EXISTS " + TABLE_RELAYS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL UNIQUE);";

        criarTabela(db, TABLE_RELAYS, sql);

    }

    private void criarTabela(SQLiteDatabase db, String nome, String sql) {

        try {
            db.execSQL( sql );
            Log.i("INFO DB", "Sucesso ao criar tabela " + nome);
        } catch(Exception e) {
            Log.e("INFO DB", "Erro ao criar tabela. " + e.getMessage());
        }
    }
}
