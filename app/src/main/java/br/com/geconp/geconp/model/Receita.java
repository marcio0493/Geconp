package br.com.geconp.geconp.model;

/**
 * Created by MARCIO on 11/01/15.
 */
public class Receita {
    private long id;
    private float valor;
    private String descricao;
    private String data;
    private String tipo;
    private String conta;

    public Receita(float valor, String data, String descricao, String tipo, String conta) {
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.tipo = tipo;
        this.conta = conta;
    }

    public Receita(long id, float valor, String data, String descricao, String tipo, String conta) {
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.tipo = tipo;
        this.conta = conta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Receita() {
        super();
    }

    public double getValor() {
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    @Override
    public String toString() {
        return descricao + " Valor: " + valor;
    }
}
