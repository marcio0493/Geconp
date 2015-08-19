package br.com.geconp.geconp.model;

/**
 * Created by Marcio on 06/05/2015.
 */
public class Cartao {
    private long id;
    private float valor;
    private String descricao;
    private String bandeira;
    private float limite;
    private String fatura;
    private String dataVenc;

    public Cartao() {
    }

    public Cartao(float valor, String descricao, String bandeira, float limite, String fatura, String dataVenc) {
        this.valor = valor;
        this.descricao = descricao;
        this.bandeira = bandeira;
        this.limite = limite;
        this.fatura = fatura;
        this.dataVenc = dataVenc;
    }

    public Cartao(long id, float valor) {
        this.id = id;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Cartao(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public float getLimite() {
        return limite;
    }

    public void setLimite(float limite) {
        this.limite = limite;
    }

    public String getFatura() {
        return fatura;
    }

    public void setFatura(String fatura) {
        this.fatura = fatura;
    }

    public String getDataVenc() {
        return dataVenc;
    }

    public void setDataVenc(String dataVenc) {
        this.dataVenc = dataVenc;
    }

    @Override
    public String toString() {
        return descricao + " "+ bandeira+ " "+ "Valor: " + valor;
    }
}
