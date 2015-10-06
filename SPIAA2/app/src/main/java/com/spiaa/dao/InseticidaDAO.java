package com.spiaa.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spiaa.base.BaseDAO;
import com.spiaa.dados.DatabaseHelper;
import com.spiaa.modelo.Inseticida;

/**
 * Created by eless on 05/10/2015.
 */
public class InseticidaDAO implements BaseDAO<Inseticida> {
    Context context;

    public InseticidaDAO(Context context) {
        this.context = context;
    }

    @Override
    public void insert(Inseticida inseticida) throws Exception {
        SQLiteDatabase sqlLite = new DatabaseHelper(context).getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(Inseticida.ID, inseticida.getId());
        content.put(Inseticida.NOME, inseticida.getNome());
        content.put(Inseticida.UNIDADE, inseticida.getUnidade());
        sqlLite.insert(Inseticida.TABLE_NAME, null, content);
    }

    @Override
    public Inseticida select(Inseticida entity) throws Exception {
        Inseticida inseticida = null;
        Cursor cursor = null;
        SQLiteDatabase sqlLite = new DatabaseHelper(context).getReadableDatabase();
        String where = Inseticida.ID + " = ?";

        String[] colunas = new String[]{Inseticida.ID, Inseticida.NOME, Inseticida.UNIDADE};
        String argumentos[] = new String[]{entity.getId().toString()};
        cursor = sqlLite.query(Inseticida.TABLE_NAME, colunas, where, argumentos, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            inseticida = new Inseticida();
            inseticida.setId(cursor.getLong(0));
            inseticida.setNome(cursor.getString(1));
            inseticida.setUnidade(cursor.getString(2));
            cursor.close();
        }
        return inseticida;
    }

    @Override
    public void update(Inseticida entity) throws Exception {

    }

    @Override
    public void delete(Inseticida entity) throws Exception {

    }
}