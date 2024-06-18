package br.com.mp.tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Dados(@JsonAlias("codigo") Integer codigo,
                    @JsonAlias("nome") String nome) {

    @Override
    public String toString() {
        return "DadosMarca{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                '}';
    }
}
