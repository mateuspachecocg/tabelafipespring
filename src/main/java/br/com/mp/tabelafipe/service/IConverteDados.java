package br.com.mp.tabelafipe.service;

public interface IConverteDados {

    <T> T converteDados(String json, Class<T> tClass);
}
