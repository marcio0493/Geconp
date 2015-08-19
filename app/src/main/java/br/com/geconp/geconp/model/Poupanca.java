package br.com.geconp.geconp.model;

/**
 * Created by MARCIO on 04/05/2015.
 */
public class Poupanca {
    private long id;
    private String banco;
    private float valor;
    private String data;

    public Poupanca(String banco, float valor, String data) {
        this.banco = banco;
        this.valor = valor;
        this.data = data;
    }

    public Poupanca() {
    }

    public Poupanca(long id, float valor) {
        this.id = id;
        this.valor = valor;
    }

    public Poupanca(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data + " " + banco + " "+ valor;
    }
}
