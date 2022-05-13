import java.io.RandomAccessFile;

import java.io.*;

public class ListaInvertida {
    private RandomAccessFile arq;
    private final String arqListaInveritidaNome = "dados/listaInvertida/listaInvertidaNome.db";
    private final String arqListaInveritidaCidade = "dados/listaInvertida/listaInvertidaCidade.db";

    /**
     * Função construtora, que cria os arquivos caso eles não existam com o intuito de não oferecer bugs ao programa
     */
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

            String palavra_arq;

            arq.seek(0);
            while(arq.getFilePointer() < arq.length()) {
                palavra_arq = arq.readUTF();
                arq.readByte();
                arq.readByte();
                arq.readByte();
                arq.readByte();
                arq.readByte();
                arq.readLong();
                if(palavra.compareTo(palavra_arq) == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Função para procurar a posicao no arquivo que esta livre para inserir o indice
     * @param palavra -> palavra que foi repetida
     * @param arquivo -> arquivo que tem que ser lido
     * @return -> a posicao do arquivo livre
     */
    public long posIndiceLivre(String palavra, String arquivo) {
        try {
            arq = new RandomAccessFile(arquivo, "rw");

            arq.seek(0);
            long pos = arq.getFilePointer();

            arq.seek(0);
            while(arq.getFilePointer() < arq.length()) {
                arq.readUTF();

                // Pega a posição antes de ler para se caso o valor seja == a -1 retornar a posicao correta livre
                pos = arq.getFilePointer();
                if(arq.readByte() == -1) {
                    return pos;
                }

                pos = arq.getFilePointer();
                if (arq.readByte() == -1) {
                    return pos;
                }

                pos = arq.getFilePointer();
                if (arq.readByte() == -1) {
                    return pos;
                }

                pos = arq.getFilePointer();
                if (arq.readByte() == -1) {
                    return pos;
                }

                pos = arq.getFilePointer();
                if (arq.readByte() == -1) {
                    return pos;
                }

                pos = arq.getFilePointer();
                if (arq.readLong() == -1) {
                    arq.seek(pos);                  // vai para a ultima posicao livre registrada
                    arq.writeLong(arq.length());    // escreve a ultima posicao do arquivo como se fosse um ponteiro para a continuacao do array
                    arq.seek(arq.length());         // vai para a ultima posica
                    return arq.getFilePointer();    // retorna a ultima posicao do arquivo para criar o objeto apontado
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Cria a lista invertida do nome e cidade do time, quando ele existe faz um ponteiro para a proxima localizacao da continuacao, pois
     * ele so tem tamanho 5. Caso ele nao exista ele cria um normal de tamanho 5 na ultima posicao do arquivo
     * @param nome -> nome ou cidade do time inteiro a ser inserido na lista
     * @param id -> id do time a ser inserido na lista
     */
    public void createArqLista(String nome, byte id, String arquivo) {
        String palavras[] = new String[contarNumeroPalavras(nome)];
        palavras = nome.split(" ");

        try {
            arq = new RandomAccessFile(arquivo, "rw");

            for(int i = 0; i < palavras.length; i++) {
                if(contemPalavra(palavras[i], arquivo) == true) {
                    long posAvaliable = posIndiceLivre(palavras[i], arquivo);

                    if(posAvaliable != arq.length()) {
                        arq.seek(posAvaliable);
                        arq.writeByte(id);
                    } else {
                        // Temos que criar a mesma palavra novamente com o id desejado na frente
                        arq.seek(posAvaliable);
                        arq.writeUTF(palavras[i]);
                        arq.writeByte(id);
                        arq.writeByte(-1);
                        arq.writeByte(-1);
                        arq.writeByte(-1);
                        arq.writeByte(-1);
                        arq.writeLong(-1);
                    }
                } else {
                    arq.seek(arq.length());
                    arq.writeUTF(palavras[i]);
                    arq.writeByte(id);
                    arq.writeByte(-1);
                    arq.writeByte(-1);
                    arq.writeByte(-1);
                    arq.writeByte(-1);
                    arq.writeLong(-1); // endereço
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Função apenas para msotrar a lista invertida
     */
    public void showListaInvertida() {
        try {
            arq = new RandomAccessFile(arqListaInveritidaNome, "rw");

            while(arq.getFilePointer() < arq.length()) {
                System.out.println(arq.readUTF());
                System.out.println(arq.readByte());
                System.out.println(arq.readByte());
                System.out.println(arq.readByte());
                System.out.println(arq.readByte());
                System.out.println(arq.readByte());
                System.out.println(arq.readLong());
                System.out.println("-----------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}