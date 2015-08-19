package br.com.geconp.geconp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.geconp.geconp.model.Cartao;
import br.com.geconp.geconp.model.Conta;
import br.com.geconp.geconp.model.Despesa;
import br.com.geconp.geconp.model.MovimentoDespesa;
import br.com.geconp.geconp.model.MovimentoReceita;
import br.com.geconp.geconp.model.Orcamento;
import br.com.geconp.geconp.model.Poupanca;
import br.com.geconp.geconp.model.Receita;

/**
 * Created by MARCIO on 16/01/15.
 */
public class DatabaseManager extends SQLiteOpenHelper{
    private static final String[] TABELAS={"CREATE TABLE receita(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "valor REAL, descricao TEXT, data TEXT, " +
            "tipo TEXT, conta TEXT)",
            "CREATE TABLE cartao(_id INTEGER PRIMARY KEY AUTOINCREMENT, valor REAL, descricao TEXT, bandeira TEXT, limite REAL, " +
                    "fatura TEXT, dataVenc TEXT)",
            "CREATE TABLE despesa(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "valor REAL, descricao TEXT, data TEXT, " +
            "tipo TEXT, conta TEXT)",
            "CREATE TABLE conta(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nome TEXT, tipo TEXT, saldo REAL)",
            "CREATE TABLE poupanca(_id INTEGER PRIMARY KEY AUTOINCREMENT, banco TEXT," +
            "valor REAL, data TEXT)",
            "CREATE TABLE histDeposito(_id INTEGER PRIMARY KEY, banco TEXT, valor REAL, data TEXT)",
            "CREATE TABLE histRetirado(_id INTEGER PRIMARY KEY, banco TEXT, valor REAL, data TEXT)",
            "CREATE TABLE histCompra(_id INTEGER PRIMARY KEY AUTOINCREMENT, descricao TEXT, bandeira TEXT, valor, data TEXT)",
            "CREATE TABLE histPago(_id INTEGER PRIMARY KEY AUTOINCREMENT, descricao TEXT, bandeira TEXT, valor, data TEXT)",
            "CREATE TABLE tipoReceita(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, tipo TEXT)",
            "CREATE TABLE tipoDespesa(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, tipo TEXT)",
            "CREATE TABLE lancarReceita(_id INTEGER PRIMARY KEY AUTOINCREMENT, valor REAL, descricao TEXT, data TEXT, " +
                    "tipo TEXT, conta TEXT)",
            "CREATE TABLE lancarDespesa(_id INTEGER PRIMARY KEY AUTOINCREMENT, valor REAL, descricao TEXT, data TEXT, " +
                    "tipo TEXT, conta TEXT)",
            "CREATE TABLE tipoConta(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT)",
            "INSERT INTO conta(nome, tipo, saldo) VALUES('Dinheiro', 'Dinheiro', 1000)",
            "INSERT INTO tipoReceita(nome, tipo) VALUES('Salario', 'Receita')",
            "INSERT INTO tipoDespesa(nome, tipo) VALUES('Salario', 'Despesa')",
            "INSERT INTO tipoConta(nome) VALUES('Conta Corrente')",
            "INSERT INTO tipoConta(nome) VALUES('Conta Poupança')"

    };
    private static final String DATABASE= "DataGeconpFinal";
    private static final int VERSAO= 1;

    public DatabaseManager(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(String comando: TABELAS){
            Log.i("BD", "Comando" + comando);
            db.execSQL(comando);
            Log.i("BD", "Comando executado");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==2){

        }
    }
    /*
      **
       * Método respensável por cadastrar uma nova receita
       * @param receita
       *
    * */
    public void cadastrarReceita(Receita receita){
        ContentValues values= new ContentValues();
        values.put("valor", receita.getValor());
        values.put("descricao", receita.getDescricao());
        values.put("data", receita.getData());
        values.put("tipo", receita.getTipo());
        values.put("conta", receita.getConta());
        getWritableDatabase().insert("receita", null, values);
        Log.i("Cadastrado: ", "ID: " + receita.getId());
        close();
    }
    /*
      **
       * Método responsável para listar as receitas cadastradas
       * @return List Receita
       * @throws Exception e
    * */
    public List<Receita> listarReceita(){
        List<Receita> receitas= new ArrayList<Receita>();
        String sql= "SELECT * FROM receita";
        Cursor cursor= getReadableDatabase().rawQuery(sql, null);
        try {
            while(cursor.moveToNext()) {
                Receita receita = new Receita();
                receita.setId(cursor.getLong(0));
                receita.setValor(cursor.getFloat(1));
                receita.setDescricao(cursor.getString(2));
                receita.setData(cursor.getString(3));
                receita.setTipo(cursor.getString(4));
                receita.setConta(cursor.getString(5));
                receitas.add(receita);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            cursor.close();
        }
        return receitas;
    }

    public List<MovimentoReceita> listarMovReceita(){
        List<MovimentoReceita> receitas= new ArrayList<MovimentoReceita>();
        String sql= "SELECT * FROM lancarReceita";
        Cursor cursor= getReadableDatabase().rawQuery(sql, null);
        try {
            while(cursor.moveToNext()) {
                MovimentoReceita receita = new MovimentoReceita();
                receita.setId(cursor.getLong(0));
                receita.setValor(cursor.getFloat(1));
                receita.setDescricao(cursor.getString(2));
                receita.setData(cursor.getString(3));
                receitas.add(receita);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            cursor.close();
        }
        return receitas;
    }

    public List<MovimentoDespesa> listarMovDespesa(){
        List<MovimentoDespesa> despesas= new ArrayList<MovimentoDespesa>();
        String sql= "SELECT * FROM lancarDespesa";
        Cursor cursor= getReadableDatabase().rawQuery(sql, null);
        try {
            while(cursor.moveToNext()) {
                MovimentoDespesa despesa = new MovimentoDespesa();
                despesa.setId(cursor.getLong(0));
                despesa.setValor(cursor.getFloat(1));
                despesa.setDescricao(cursor.getString(2));
                despesa.setData(cursor.getString(3));
                despesas.add(despesa);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            cursor.close();
        }
        return despesas;
    }

    /*
      **
       * Método respensável por cadastrar uma nova despesa
       * @param despesa
       *
    * */
    public void cadastrarDespesa(Despesa despesa){
        ContentValues values= new ContentValues();
        values.put("valor", despesa.getValor());
        values.put("descricao", despesa.getDescricao());
        values.put("data", despesa.getData());
        values.put("tipo", despesa.getTipo());
        values.put("conta", despesa.getConta());
        getWritableDatabase().insert("despesa", null, values);
        close();
        Log.i("Cadastrado: ", "ID: " + despesa.getId());
    }
    /*
      **
       * Método responsável para listar as receitas cadastradas
       * @return List Receita
       * @throws Exception e
    * */
    public List<Despesa> listarDespesa(){
        List<Despesa> despesas= new ArrayList<Despesa>();
        String sql= "SELECT * FROM despesa";
        Cursor cursor= getReadableDatabase().rawQuery(sql, null);
        try {
            while(cursor.moveToNext()) {
                Despesa despesa = new Despesa();
                despesa.setId(cursor.getLong(0));
                despesa.setValor(cursor.getFloat(1));
                despesa.setDescricao(cursor.getString(2));
                despesa.setData(cursor.getString(3));
                despesa.setTipo(cursor.getString(4));
                despesa.setConta(cursor.getString(5));
                despesas.add(despesa);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            cursor.close();
        }
        return despesas;
    }

    public List<Poupanca> listarPoupanca(){
        List<Poupanca> poupancas= new ArrayList<Poupanca>();
        String sql= "SELECT * FROM poupanca";
        Cursor cursor= getReadableDatabase().rawQuery(sql, null);
        try {
            while(cursor.moveToNext()) {
                Poupanca poupanca= new Poupanca();
                poupanca.setId(cursor.getLong(0));
                poupanca.setBanco(cursor.getString(1));
                poupanca.setValor(cursor.getFloat(2));
                poupanca.setData(cursor.getString(3));
                poupancas.add(poupanca);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            cursor.close();
        }
        return poupancas;
    }

    public List<Cartao> listarCartao(){
        List<Cartao> cartoes= new ArrayList<Cartao>();
        String sql= "SELECT * FROM cartao";
        Cursor cursor= getReadableDatabase().rawQuery(sql, null);
        try {
            while(cursor.moveToNext()) {
                Cartao cartao= new Cartao();
                cartao.setId(cursor.getLong(0));
                cartao.setValor(cursor.getFloat(1));
                cartao.setDescricao(cursor.getString(2));
                cartao.setBandeira(cursor.getString(3));
                cartao.setLimite(cursor.getFloat(4));
                cartao.setFatura(cursor.getString(5));
                cartao.setDataVenc(cursor.getString(6));
                cartoes.add(cartao);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            cursor.close();
        }
        return cartoes;
    }
    /*
      **
       * Método respensável por cadastrar uma nova conta
       * @param Conta conta
       *
    * */
    public void cadastrarConta(Conta conta){
        ContentValues values= new ContentValues();
        values.put("nome", conta.getNome());
        values.put("tipo", conta.getTipo());
        values.put("saldo", conta.getSaldo());
        long id= getWritableDatabase().insert("conta", null, values);
        conta.setId(id);
        Log.i("Cadastrado: ", "ID: " + conta.getId());
        close();
    }

    public void addPoupanca(Poupanca poupanca){
        ContentValues values= new ContentValues();
        values.put("banco", poupanca.getBanco());
        values.put("valor", poupanca.getValor());
        values.put("data", poupanca.getData());
        long id= getWritableDatabase().insert("poupanca", null, values);
        poupanca.setId(id);
        close();
    }

    public void lancarReceita(MovimentoReceita movimento){
        ContentValues values= new ContentValues();
        values.put("valor", movimento.getValor());
        values.put("data", movimento.getData());
        values.put("descricao", movimento.getDescricao());
        values.put("tipo", movimento.getTipo());
        values.put("conta", movimento.getConta());
        getWritableDatabase().insert("lancarReceita", null, values);
        close();
    }

    public void lancarDespesa(MovimentoDespesa movimento){
        ContentValues values= new ContentValues();
        values.put("valor", movimento.getValor());
        values.put("data", movimento.getData());
        values.put("descricao", movimento.getDescricao());
        values.put("tipo", movimento.getTipo());
        values.put("conta", movimento.getConta());
        getWritableDatabase().insert("lancarDespesa", null, values);
        close();
    }

    public List<String> listarConta(){
        List<String> labels= new ArrayList<String>();
        String query= "SELECT nome FROM conta";
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor c= db.rawQuery(query, null);
        if(c.moveToFirst()){
            do{
                labels.add(c.getString(0));
            }while(c.moveToNext());
            c.close();

        }

        return labels;
    }

    public void cadastrarCategoriaReceita(String categoria,String tipo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", categoria);
        values.put("tipo", tipo);
        db.insert("tipoReceita", null, values);
        db.close();
    }

    public void cadastrarCategoriaDespesa(String categoria, String tipo){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put("nome", categoria);
        values.put("tipo", tipo);
        db.insert("tipoDespesa", null, values);
        db.close();
    }

    public void cadastrarCategoriaConta(String categoria){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put("nome", categoria);
        db.insert("tipoConta", null, values);
        db.close();
    }

    public List<String> listarCategoriasReceita(){
        List<String> labels= new ArrayList<String>();
        String query= "SELECT nome FROM tipoReceita";
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor c= db.rawQuery(query, null);
        if(c.moveToFirst()){
            do{
                labels.add(c.getString(0));
            }while(c.moveToNext());
            c.close();

        }
        return labels;
    }
    public List<String> listarCategoriasDespesa(){
        List<String> labels= new ArrayList<String>();
        String query= "SELECT nome FROM tipoDespesa";
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor c= db.rawQuery(query, null);
        if(c.moveToFirst()){
            do{
                labels.add(c.getString(0));
            }while(c.moveToNext());
            c.close();

        }
        return labels;
    }

    public List<String> listarCategoriaConta(){
        List<String> labels= new ArrayList<String>();
        String query= "SELECT nome FROM tipoConta";
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor c= db.rawQuery(query, null);
        if(c.moveToFirst()){
            do{
                labels.add(c.getString(0));
            }while(c.moveToNext());
            c.close();

        }
        return labels;
    }

    public List<String> listaCat(String nome){
        List<String> labels= new ArrayList<String>();
        //String sql= " WHERE tipo=?  ORDER BY data";
        String sql= "SELECT _id, data, descricao, valor FROM lancarReceita WHERE tipo= ? UNION  SELECT _id, data, descricao, valor FROM lancarDespesa WHERE tipo= ? ORDER BY data" ;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor c= db.rawQuery(sql, new String[]{nome, nome});

        if(c.moveToFirst()){
            do{
                List<String> aux= new ArrayList<String>();
                long id1= c.getLong(0);
                String dat1= c.getString(1);
                String desc1= c.getString(2);
                float val1= c.getFloat(3);
                Log.i("ID1", "= " + id1);
                Log.i("VAL1", "= " + val1);
                Log.i("DAT1", "= " + dat1);
                Log.i("DESC1", "= " +   desc1);
                String sval= String.valueOf(val1);
                aux.add(dat1);
                aux.add(desc1);
                aux.add(sval);
                labels.addAll(aux);

            }while(c.moveToNext());
            c.close();

        }
        return labels;
    }

    public void atualizarPoupanca(Poupanca poupanca){
        ContentValues values= new ContentValues();
        values.put("valor", poupanca.getValor());
        String _id= String.valueOf(poupanca.getId());
        Log.i("ID", "ID " + _id);
        Log.i("SOMA", "Valor Final " + poupanca.getValor());
        String condicao= "_id=?";
        String[] args= new String[]{_id};
        SQLiteDatabase db= this.getReadableDatabase();
        db.update("poupanca", values, condicao, args);
        close();
    }

    public void atualizarMovReceita(MovimentoReceita movimento){
        ContentValues values= new ContentValues();
        values.put("valor", movimento.getValor());
        values.put("descricao", movimento.getDescricao());
        values.put("data", movimento.getData());
        values.put("tipo", movimento.getTipo());
        values.put("conta", movimento.getConta());

        String _id= String.valueOf(movimento.getId());
        String condicao= "_id=?";
        String[] args= new String[]{_id};
        SQLiteDatabase db= this.getReadableDatabase();
        db.update("lancarReceita", values, condicao, args);
        close();
    }



    public void atualizarMovDespesa(MovimentoDespesa movimento){
        ContentValues values= new ContentValues();
        values.put("valor", movimento.getValor());
        values.put("descricao", movimento.getDescricao());
        values.put("data", movimento.getData());
        values.put("tipo", movimento.getTipo());
        values.put("conta", movimento.getConta());

        String _id= String.valueOf(movimento.getId());
        String condicao= "_id=?";
        String[] args= new String[]{_id};
        SQLiteDatabase db= this.getReadableDatabase();
        db.update("lancarDespesa", values, condicao, args);
        close();
    }

    public void deletarPoupanca(Poupanca poupanca){
        SQLiteDatabase db= this.getReadableDatabase();
        String _id= String.valueOf(poupanca.getId());
        String[] args= new String[]{_id};
        db.delete("poupanca", "_id=?", args);
        close();
    }

    public void atualizarCartao(Cartao cartao){
        ContentValues values= new ContentValues();
        values.put("valor", cartao.getValor());
        String _id= String.valueOf(cartao.getId());
        String condicao= "_id=?";
        String[] args= new String[]{_id};
        SQLiteDatabase db= this.getReadableDatabase();
        db.update("cartao", values, condicao, args);

    }

    public void atualizarReceita(Receita receita){
        ContentValues values= new ContentValues();
        values.put("valor", receita.getValor());
        values.put("descricao", receita.getDescricao());
        values.put("data", receita.getData());
        values.put("tipo", receita.getTipo());
        values.put("conta", receita.getConta());

        String _id= String.valueOf(receita.getId());
        String condicao= "_id=?";
        String[] args= new String[]{_id};
        SQLiteDatabase db= this.getReadableDatabase();
        db.update("receita", values, condicao, args);
        close();
    }
    public void atualizarDespesa(Despesa despesa){
        ContentValues values= new ContentValues();
        values.put("valor", despesa.getValor());
        values.put("descricao", despesa.getDescricao());
        values.put("data", despesa.getData());
        values.put("tipo", despesa.getTipo());
        values.put("conta", despesa.getConta());

        String _id= String.valueOf(despesa.getId());
        String condicao= "_id=?";
        String[] args= new String[]{_id};
        SQLiteDatabase db= this.getReadableDatabase();
        db.update("despesa", values, condicao, args);
        close();
    }

    public void deletarCartao(Cartao cartao){
        SQLiteDatabase db= this.getReadableDatabase();
        String _id= String.valueOf(cartao.getId());
        String[] args= new String[]{_id};
        db.delete("cartao", "_id=?", args);
    }

    public void deletarReceita(Receita receita){
        SQLiteDatabase db= this.getReadableDatabase();
        String _id= String.valueOf(receita.getId());
        String[] args= new String[]{_id};
        db.delete("receita", "_id=?", args);
    }

    public void deletarDespesa(Despesa despesa){
        SQLiteDatabase db= this.getReadableDatabase();
        String _id= String.valueOf(despesa.getId());
        String[] args= new String[]{_id};
        db.delete("despesa", "_id=?", args);
    }

    public void deletarMovReceita(MovimentoReceita movimento){
        SQLiteDatabase db= this.getReadableDatabase();
        String _id= String.valueOf(movimento.getId());
        String[] args= new String[]{_id};
        db.delete("lancarReceita", "_id=?", args);
    }

    public void deletarMovDespesa(MovimentoDespesa movimento){
        SQLiteDatabase db= this.getReadableDatabase();
        String _id= String.valueOf(movimento.getId());
        String[] args= new String[]{_id};
        db.delete("lancarDespesa", "_id=?", args);
    }

    public void cadastrarCartao(Cartao cartao){
        ContentValues values= new ContentValues();
        values.put("valor", cartao.getValor());
        values.put("descricao", cartao.getDescricao());
        values.put("bandeira", cartao.getBandeira());
        values.put("limite", cartao.getLimite());
        values.put("fatura", cartao.getFatura());
        values.put("dataVenc", cartao.getDataVenc());
        long id= getWritableDatabase().insert("cartao", null, values);
        cartao.setId(id);
        close();
    }

    public void historicoDeposito(long id, float valor, String banco, String data){
        Log.i("CONF2", "Banco= " + banco);
        Log.i("CONF2", "Data= " + data);
        ContentValues values= new ContentValues();
        values.put("_id", id);
        values.put("banco", banco);
        values.put("valor", valor);
        values.put("data", data);
        getWritableDatabase().insert("histDeposito", null, values);

    }

    public void historicoRetirado(long id, float valor, String banco, String data){
        ContentValues values= new ContentValues();
        values.put("_id", id);
        values.put("banco", banco);
        values.put("valor", valor);
        values.put("data", data);
        getWritableDatabase().insert("histRetirado", null, values);

    }

    public void historicoCompra(float valor, String descricao, String bandeira, String fatura){
        ContentValues values= new ContentValues();
        values.put("valor", valor);
        values.put("descricao", descricao);
        values.put("bandeira", bandeira);
        values.put("data", fatura);
        getWritableDatabase().insert("histCompra", null, values);

    }
    public void historicoPago(float valor, String descricao, String bandeira, String fatura){
        ContentValues values= new ContentValues();
        values.put("valor", valor);
        values.put("descricao", descricao);
        values.put("bandeira", bandeira);
        values.put("data", fatura);
        getWritableDatabase().insert("histPago", null, values);

    }

    public boolean buscarIdCartao(Cartao cartao){
        SQLiteDatabase db= this.getReadableDatabase();
        String sql= "SELECT * FROM cartao WHERE _id=?";
        Cursor cursor= db.rawQuery(sql, new String[]{String.valueOf(cartao.getId())});
        Log.i("BD3", "RESULTADO CURSOR" + cursor);
        if(cursor != null){
            Log.i("BD3", "Retorno False");
            return false;
        }
        Log.i("BD3", "Retorno True");
        return true;
    }

    public List<String> listagemHistorico(){

        List<String> dados= new ArrayList<>();
        String id;
        String valor;
        String data;
        String descricao;
        String[] lista;
        String sql= "SELECT _id, valor, data, descricao FROM lancarReceita UNION ALL SELECT _id, valor, data, descricao FROM lancarDespesa ORDER BY data";
        SQLiteDatabase db= this.getReadableDatabase();
        Log.i("CONF1", "Chegou aki");
        Cursor cursor= db.rawQuery(sql, null);
        Log.i("CONF2", "Chegou aki");
        if(cursor.moveToFirst()){
            Log.i("CONF", "Chegou aki");
            do{

                dados.add(cursor.getString(0));

            }while(cursor.moveToNext());
                cursor.close();
        }else{
            Log.i("CONF", "Não deu certo");
        }
        return dados;
    }

    public List<Receita> buscaCategoria(String categoria){
        Cursor cursor= this.getReadableDatabase().query("receita", new String[]{"_id","valor","data","descricao","tipo", "conta"}, "tipo=?", new String[]{categoria}, null, null, null);
        List<Receita> receitas= new ArrayList<>();

        while(cursor.moveToNext()){
            Receita receita= new Receita();
            receita.setId(cursor.getLong(0));
            receita.setValor(cursor.getFloat(1));
            receita.setData(cursor.getString(2));
            receita.setDescricao(cursor.getString(3));
            receita.setTipo(cursor.getString(4));
            receita.setConta(cursor.getString(5));
            receitas.add(receita);
        }
        return receitas;

    }

    public List<Receita> buscaConta(String conta){
        Cursor cursor= this.getReadableDatabase().query("receita", new String[]{"_id","valor","data","descricao","tipo", "conta"}, "conta=?", new String[]{conta}, null, null, null);
        List<Receita> receitas= new ArrayList<>();

        while(cursor.moveToNext()){
            Receita receita= new Receita();
            receita.setId(cursor.getLong(0));
            receita.setValor(cursor.getFloat(1));
            receita.setData(cursor.getString(2));
            receita.setDescricao(cursor.getString(3));
            receita.setTipo(cursor.getString(4));
            receita.setConta(cursor.getString(5));
            receitas.add(receita);
        }
        return receitas;

    }

    public List<Receita> buscaData(String data){
        Cursor cursor= this.getReadableDatabase().query("receita", new String[]{"_id","valor","data","descricao","tipo", "conta"}, "data=?", new String[]{data}, null, null, null);
        List<Receita> receitas= new ArrayList<>();

        while(cursor.moveToNext()){
            Receita receita= new Receita();
            receita.setId(cursor.getLong(0));
            receita.setValor(cursor.getFloat(1));
            receita.setData(cursor.getString(2));
            receita.setDescricao(cursor.getString(3));
            receita.setTipo(cursor.getString(4));
            receita.setConta(cursor.getString(5));
            receitas.add(receita);
        }
        return receitas;

    }

    public List<Receita> buscaValor(float valor){
        Cursor cursor= this.getReadableDatabase().query("receita", new String[]{"_id","valor","data","descricao","tipo", "conta"}, "valor=?", new String[]{String.valueOf(valor)}, null, null, null);
        List<Receita> receitas= new ArrayList<>();

        while(cursor.moveToNext()){
            Receita receita= new Receita();
            receita.setId(cursor.getLong(0));
            receita.setValor(cursor.getFloat(1));
            receita.setData(cursor.getString(2));
            receita.setDescricao(cursor.getString(3));
            receita.setTipo(cursor.getString(4));
            receita.setConta(cursor.getString(5));
            receitas.add(receita);
        }
        return receitas;
    }

    public List<Despesa> buscaCategoriaDespesa(String categoria){
        Cursor cursor= this.getReadableDatabase().query("despesa", new String[]{"_id","valor","data","descricao","tipo", "conta"}, "tipo=?", new String[]{categoria}, null, null, null);
        List<Despesa> despesas= new ArrayList<>();

        while(cursor.moveToNext()){
            Despesa despesa= new Despesa();
            despesa.setId(cursor.getLong(0));
            despesa.setValor(cursor.getFloat(1));
            despesa.setData(cursor.getString(2));
            despesa.setDescricao(cursor.getString(3));
            despesa.setTipo(cursor.getString(4));
            despesa.setConta(cursor.getString(5));
            despesas.add(despesa);
        }
        return despesas;

    }

    public List<Despesa> buscaContaDespesa(String conta){
        Cursor cursor= this.getReadableDatabase().query("despesa", new String[]{"_id","valor","data","descricao","tipo", "conta"}, "conta=?", new String[]{conta}, null, null, null);
        List<Despesa> despesas= new ArrayList<>();

        while(cursor.moveToNext()){
            Despesa despesa= new Despesa();
            despesa.setId(cursor.getLong(0));
            despesa.setValor(cursor.getFloat(1));
            despesa.setData(cursor.getString(2));
            despesa.setDescricao(cursor.getString(3));
            despesa.setTipo(cursor.getString(4));
            despesa.setConta(cursor.getString(5));
            despesas.add(despesa);
        }
        return despesas;

    }

    public List<Despesa> buscaDataDespesa(String data){
        Cursor cursor= this.getReadableDatabase().query("despesa", new String[]{"_id","valor","data","descricao","tipo", "conta"}, "data=?", new String[]{data}, null, null, null);
        List<Despesa> despesas= new ArrayList<>();

        while(cursor.moveToNext()){
            Despesa despesa= new Despesa();
            despesa.setId(cursor.getLong(0));
            despesa.setValor(cursor.getFloat(1));
            despesa.setData(cursor.getString(2));
            despesa.setDescricao(cursor.getString(3));
            despesa.setTipo(cursor.getString(4));
            despesa.setConta(cursor.getString(5));
            despesas.add(despesa);
        }
        return despesas;

    }

    public List<Despesa> buscaValorDespesa(float valor){
        Cursor cursor= this.getReadableDatabase().query("despesa", new String[]{"_id","valor","data","descricao","tipo", "conta"}, "valor=?", new String[]{String.valueOf(valor)}, null, null, null);
        List<Despesa> despesas= new ArrayList<>();

        while(cursor.moveToNext()){
            Despesa despesa= new Despesa();
            despesa.setId(cursor.getLong(0));
            despesa.setValor(cursor.getFloat(1));
            despesa.setData(cursor.getString(2));
            despesa.setDescricao(cursor.getString(3));
            despesa.setTipo(cursor.getString(4));
            despesa.setConta(cursor.getString(5));
            despesas.add(despesa);
        }
        return despesas;
    }


}
