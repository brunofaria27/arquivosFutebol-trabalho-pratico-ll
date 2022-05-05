import java.io.RandomAccessFile;

import java.io.*;

public class IndexDAO {
    private RandomAccessFile arq;
    private final String nomeArquivoIndex = "dados/index/clubes_index.db";

    public IndexDAO() {
        try {
            boolean exists = (new File(nomeArquivoIndex)).exists();

            if (exists) {
                // Arquivo Existe
            } else {
                try {
                    arq = new RandomAccessFile(nomeArquivoIndex, "rw"); // Cria o arquivo caso não exista
                    arq.close();
                } catch (Exception e) {
                    System.out.println("Erro para criar o arquivo de indices!");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro! " + e.getMessage());
        }
    }

    /**
     * Adiciona o id e endereco original em um arquivo de indices
     * @param id -> recebe na hora da criação do time
     * @param endereco -> pega a ultima posião inserida no arquivo original
     */
    public void addValue(byte id, long endereco) {
        try {
            arq = new RandomAccessFile(nomeArquivoIndex, "rw");
            arq.seek(arq.length()); // Navegar para a última posição do arquivo
            arq.writeByte(id);
            arq.writeLong(endereco);
            arq.close();
        } catch (Exception e) {
            System.out.println("Erro na inserção do id e endereço no arquivo de indices.");
        }
    }

    /**
     * Atualiza o endereco do dado no arquivo de indices
     * @param endereco -> Ao fazer o update, recebe o novo endereco dos dado atualizado
     */
//    public updateValue(int endereco) {

//    }

    /**
     * Mostar o arquivo de indices para testes de funcionamento
     */
    public void showArq() {
        try {
            arq = new RandomAccessFile(nomeArquivoIndex, "rw");

            byte id;
            long endereco;

            while (arq.getFilePointer() < arq.length()) {
                id = arq.readByte();
                endereco = arq.readLong();
                System.out.println("ID......: " + id + " ENDEREÇO: " + endereco);
            }
        } catch (Exception e) {
            System.out.println("Erro ao imprimir o arquivo de indices!");
        }
    }
}
