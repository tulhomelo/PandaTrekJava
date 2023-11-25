package br.com.pandatrek;

import java.util.Date;

public class Organizacao {

    private int id;
    private String nome;
    private Date dataCadastro;
    private int ativo;
    private int excluido;
    private Date dataExclusao;
    private String descricao;

    public Organizacao(String nome, String descricao, int ativo) {
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public Organizacao(int id, String nome, Date dataCadastro, int ativo, int excluido,
            Date dataExclusao, String descricao) {
        this.id = id;
        this.nome = nome;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
        this.excluido = excluido;
        this.dataExclusao = dataExclusao;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public int getAtivo() {
        return ativo;
    }

    public void setAtivo(int ativo) {
        this.ativo = ativo;
    }

    public int getExcluido() {
        return excluido;
    }

    public void setExcluido(int excluido) {
        this.excluido = excluido;
    }

    public Date getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(Date dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return getId() + "-" + getNome();
    }
}
