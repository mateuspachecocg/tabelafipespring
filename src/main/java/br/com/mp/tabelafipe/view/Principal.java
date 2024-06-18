package br.com.mp.tabelafipe.view;

import br.com.mp.tabelafipe.model.Dados;
import br.com.mp.tabelafipe.model.Modelo;
import br.com.mp.tabelafipe.service.ConsumoApi;
import br.com.mp.tabelafipe.service.ConversorDados;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConversorDados conversorDados = new ConversorDados();
    private final Scanner teclado = new Scanner(System.in);
    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/%s/marcas";


    public void startDialog() {
        System.out.println("BEM-VINDO AO TABELA FIPE APP");
        var opVeic = this.getTipoDeVeiculo();

        var responseMarcas = consumoApi.obterDados(String.format(ENDERECO, opVeic));
        Dados listaMarcas[] = conversorDados.converteDados(responseMarcas, Dados[].class);

        var marca = this.getMarca(listaMarcas);
        var responseVeiculos = consumoApi.obterDados(String.format(ENDERECO, opVeic)+"/"+ marca.get().codigo() +"/modelos");

        var listaModelos = conversorDados.converteDados(responseVeiculos, Modelo.class);

        var modeloEscolhido = this.getModelo(listaModelos.modelos());
        var responseFinal = consumoApi.obterDados(String.format(ENDERECO, opVeic)+"/"+ marca.get().codigo() +"/modelos"+ "/" + modeloEscolhido.get().codigo()+"/anos");
        System.out.println(responseFinal);
    }

    public Optional<Dados> getModelo(List<Dados> listaModelos) {
        System.out.println("Deseja ver os modelos disponiveis (S-sim/N-nao: ");
        var opVis = teclado.nextLine();
        if (opVis.equalsIgnoreCase("S")) {
            listaModelos.forEach(dm -> System.out.println("Cod:\t" + dm.codigo() + "\tMarca: " + dm.nome()));
        }
        System.out.println("Digite um trecho do nome do veiculo: ");
        var trechoNome = teclado.nextLine().toUpperCase();
        List<Dados> modelosPesquisados = listaModelos.stream()
                .filter(e -> e.nome().toUpperCase().contains(trechoNome))
                .collect(Collectors.toList());

        modelosPesquisados.forEach(dm -> System.out.println("Cod:\t" + dm.codigo() + "\tMarca: " + dm.nome()));
        System.out.println("Digite o codigo do modelos do veiuclo pesquisado: ");
        var opModel = teclado.nextInt();
        teclado.nextLine();

        Optional<Dados> modeloEscolhido = modelosPesquisados.stream()
                .filter(v -> v.codigo() == opModel)
                .findFirst();
        return modeloEscolhido;
    }

    private  Optional<Dados> getMarca(Dados listaMarcas[]) {
        Arrays.stream(listaMarcas).forEach(dm -> System.out.println("Cod:\t" + dm.codigo() + "\tMarca: " + dm.nome()));
        System.out.println("ESCOLHA A MARCA DO VEICULO");
        var opCodMarca = teclado.nextInt();
        teclado.nextLine();

        Optional<Dados> marcaEscolhida = Arrays.stream(listaMarcas)
                .filter(m -> m.codigo() == opCodMarca).findAny();

        if (marcaEscolhida.isPresent()) {
            return marcaEscolhida;
        } else {
            System.out.println("Tente novamente: ");
            return this.getMarca(listaMarcas);
        }
    }

    private String getTipoDeVeiculo() {
        System.out.println("""
                **** OPCOES ****
                1 - Carro
                2 - Moto
                3 - Caminhao
                
                Digite uma das opcoes para consultar valores: """);
        var opCod = teclado.nextInt();
        teclado.nextLine();
        switch (opCod){
            case 1:
                return "carros";
            case 2:
                return "motos";
            case 3:
                return "caminhoes";
            default:
                System.out.println("Opcao Invalida!");
                return this.getTipoDeVeiculo();
        }

    }

}
