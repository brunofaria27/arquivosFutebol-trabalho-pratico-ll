// usuario insere um nome e uma cidade
// lista invertida pega o nome e separa -> ex: Atletico Mineiro -> Atletico / Mineiro 
// faz a mesma coisa do nome para a cidade
// armazena num arquivo bytes a palavra e o id que esta relacionada com ela
// Exemplo: Galo 1, 2, 3; Mineiro 1, 2
// tem que ter um separador de uma palavra para a outra como um ';'

// OBS: Será que vale a pena tratar a questão dos IDS com array no objeto lista invertida ou vale a pena somente tratar a questao de buscar todos e ler normalmente

import java.io.RandomAccessFile;

import java.io.*;

public class ListaInvertida {
    private RandomAccessFile arq;
    private final String arqListaInveritidaNome = "dados/listaInvertida/listaInvertidaNome.db";
    private final String arqListaInveritidaCidade = "dados/listaInvertida/listaInvertidaCidade.db";

    public ListaInvertida() {
        try {
            boolean exists_name = (new File(arqListaInveritidaNome)).exists();
            boolean exists_city = (new File(arqListaInveritidaCidade)).exists();

            if (exists_name == true && exists_city == true) {
                // Arquivo Existe
            } else {
                try {
                    arq = new RandomAccessFile(arqListaInveritidaNome, "rw"); // Cria o arquivo caso não exista
                    arq = new RandomAccessFile(arqListaInveritidaCidade, "rw");
                    arq.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Contar a quantidade de palavras que um nome tem
     * @param recebe o nome que deve ser pesquisado a quantidade de palvras
     * @return retorna a quantidade de palavras
     */
    public int contarNumeroPalavras(String nome) {
        int qnt_palavras = 0;

        for(int i = 0; i < nome.length(); i++) {
            if(nome.charAt(i) == ' ') {
                qnt_palavras++;
            }
        }

        return qnt_palavras;
    }

    /**
     * Procura no arquivo da lista Invertida se tem a palavra
     * @param palavra -> recebe uma palavra para ser pesquisada no arquivo
     * @param arquivo -> recebe o nome do arquivo que deve ser pesquisado
     * @return true se achara a palavra e false se nao achar a palavra
     */
    public boolean contemPalavra(String palavra, String arquivo) {
        try {
            arq = new RandomAccessFile(arquivo, "rw");

            // Tratar a questão de readUTF com todos os ids do lado, tem que ler todos os ids para continuar lendo os UTF

            while(arq.getFilePointer() < arq.length()) {
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 
     * @param nome
     * @param id
     */
    public void createArqName(String nome, byte id) {
        String palavras[] = new String[contarNumeroPalavras(nome)];
        palavras = nome.split(" ");

        // Tratar condições para criar caso n tenha e caso tenha

        try {
            arq = new RandomAccessFile(arqListaInveritidaCidade, "rw");

            arq.seek(arq.length()); // Navegar para a última posição do arquivo
            arq.writeBytes(palavras[0]);
            arq.writeByte(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
