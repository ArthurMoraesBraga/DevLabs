package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "automovel")
public class Automovel implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String matricula;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(nullable = false, unique = true, length = 20)
    private String placa;

    @Column(name = "imagem_url", length = 1000)
    private String imagemUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_proprietario", length = 20)
    private TipoProprietario tipoProprietario;

    @Column(name = "proprietario_id")
    private Long proprietarioId;

    @Override
    public Long getId() { return id; }

    @Override
    public void setId(Long id) { this.id = id; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public TipoProprietario getTipoProprietario() { return tipoProprietario; }
    public void setTipoProprietario(TipoProprietario tipoProprietario) { this.tipoProprietario = tipoProprietario; }

    public Long getProprietarioId() { return proprietarioId; }
    public void setProprietarioId(Long proprietarioId) { this.proprietarioId = proprietarioId; }
}