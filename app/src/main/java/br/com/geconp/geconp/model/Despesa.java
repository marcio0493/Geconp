package br.com.geconp.geconp.model;

/**
 * Created by MARCIO on 16/01/15.
 */
public class Despesa {
    private long id;
    private float valor;
    private String data;
    private String descricao;
    private String tipo;
    private String conta;

    public Despesa(float valor, String data, String descricao, String tipo, String conta) {
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.tipo = tipo;
        this.conta = conta;
    }

    public Despesa(long id, float valor, String data, String descricao, String tipo, String conta) {
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.tipo = tipo;
        this.conta = conta;
    }

    public Despesa() {
        super();
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return descricao + " Valor: " + valor;
    }
}
