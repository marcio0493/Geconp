package br.com.geconp.geconp.model;

/**
 * Created by MARCIO on 16/01/15.
 */
public class Conta {
    private long id;
    private String nome;
    private String tipo;
    private float saldo;

    public Conta(String nome, String tipo, float saldo) {
        this.nome= nome;
        this.tipo = tipo;
        this.saldo = saldo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public Conta() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

}
