package com.example.crud.dto;

import java.util.List;

public class EventoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String data;
    private List<UsuarioIdDTO> usuarios;

    public EventoDTO(Long id, String nome, String descricao, String data, List<UsuarioIdDTO> usuarios){
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.usuarios = usuarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData(){
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }

    public List<UsuarioIdDTO> getUsuarios(){
        return usuarios;
    }

    public void setUsuarios(List<UsuarioIdDTO> usuarios) {
        this.usuarios = usuarios;
    }
}
