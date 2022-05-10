// usuario insere um nome e uma cidade
// lista invertida pega o nome e separa -> ex: Atletico Mineiro -> Atletico / Mineiro 
// faz a mesma coisa do nome para a cidade
// armazena num arquivo bytes a palavra e o id que esta relacionada com ela
// Exemplo: Galo 1, 2, 3; Mineiro 1, 2
// tem que ter um separador de uma palavra para a outra como um ';'
import java.io.RandomAccessFile;

import java.io.*;

public class ListaInvertida {
    private RandomAccessFile arq;
    private final String arqListaInveritidaNome = "dados/listaInvertida/listaInvertidaNome.db";
    private final String arqListaInveritidaCidade = "dados/listaInvertida/listaInvertidaCidade.db";
    private String nome;    // nome do time
    private String cidade;  // cidade do time
    private byte id;        // id do time

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

    public ListaInvertida(String nome, String cidade, byte id) {
        this.nome = nome;
        this.cidade = cidade;
        this.id = id;
    }

    public byte getId() {
        return this.id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setId(byte id) {
        this.id = id;
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
     * 
     */
    public void createArqName(String nome, byte id) {
        String palavras[] = new String[contarNumeroPalavras(nome)];
        palavras = nome.split(" ");

        try {
            arq = new RandomAccessFile(arqListaInveritidaNome, "rw");

            arq.seek(arq.length()); // Navegar para a última posição do arquivo
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
