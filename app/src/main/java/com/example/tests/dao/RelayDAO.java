package com.example.tests.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tests.model.RelayModel;

import java.util.ArrayList;
import java.util.List;

public class RelayDAO {

    private SQLiteDatabase writer;
    private SQLiteDatabase reader;

    public RelayDAO(Context context) {

        DBHelper dbHelper = new DBHelper( context );
        writer = dbHelper.getWritableDatabase();
        reader = dbHelper.getReadableDatabase();
    }

    public void salvar(RelayModel relay) {
        ContentValues cv = new ContentValues();
        cv.put("nome", relay.getNome());
        cv.put("id", relay.getId());

        try {
            writer.insert(DBHelper.TABLE_RELAYS, null, cv);
            Log.i("INFO DB", "Sucesso ao salvar item. ");
        } catch (Exception e) {
            Log.e("INFO DB", "Erro ao salvar item. " + e.getMessage());
        }
    }

    public boolean atualizar(RelayModel relay) {

        ContentValues cv = new ContentValues();
        cv.put("nome", relay.getNome());
        String[] relayID = {String.valueOf(relay.getId())};
        try {
            writer.update(DBHelper.TABLE_RELAYS, cv, "id=?", relayID);
            Log.i("INFO DB", "Sucesso ao salvar item. ");
        } catch (Exception e) {
            Log.e("INFO DB", "Erro ao salvar item. " + e.getMessage());
        }
        return true;
    }
    public boolean deletar(RelayModel relay) {
        try {
            String[] args = {String.valueOf(relay.getId())};
            writer.delete(DBHelper.TABLE_RELAYS, "id = ?", args);
            Log.i("INFO", "Sucesso ao excluir dado na tabela");

        } catch (Exception e) {
            Log.e("INFO", "Erro ao excluir dado na tabela: " + e.getMessage());
            return false;
        }

        return true;
    }
    public List<RelayModel> listar() {
        List<RelayModel> relays = new ArrayList<RelayModel>();

        String sql = "SELECT * FROM " + DBHelper.TABLE_RELAYS + ";";
        Cursor cursor = reader.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String nome = cursor.getString(cursor.getColumnIndex("nome"));
            @SuppressLint("Range") Long relayID = cursor.getLong(cursor.getColumnIndex("id"));

            RelayModel relay = new RelayModel(relayID, nome);
            relays.add(relay);
        }

        return relays;
    }
}
