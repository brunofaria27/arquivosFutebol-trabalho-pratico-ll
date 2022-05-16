import java.io.RandomAccessFile;



import java.io.*;

public class IndexDAO  {
    private RandomAccessFile arq;
    private final String nomeArquivoIndex = "dados/index/clubes_index.db";
    private byte id;
    private long endereco;

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

    public IndexDAO(byte id, long endereco) {
        this.id = id;
        this.endereco = endereco;
    }

    public byte getId() {
        return id;
    }

    public long getEndereco() {
        return endereco;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public void setEndereco(long endereco) {
        this.endereco = endereco;
    }

    /**
     * Adiciona ao arquivo de indices o id e endereco do objeto criado no original
     * @param indexArq -> objeto com id e endereco do objeto original
     */
    public void addValue(IndexDAO indexArq) {
        try {
            arq = new RandomAccessFile(nomeArquivoIndex, "rw");
            long i = (indexArq.getId() - 1) * 9;
            arq.seek(i);  // Navegar para a última posição do arquivo
            arq.writeByte(indexArq.getId());
            arq.writeLong(indexArq.getEndereco());
            arq.close();
        } catch (Exception e) {
            System.out.println("Erro na inserção do id e endereço no arquivo de indices.");
        }
    }

    /**
     * Atualiza o endereco no arquivo de indices, depois do user atualizar no arquivo original
     * @param index -> objeto com id e o novo endereco do arquivo de dados original, caso o objeto mude de posicao
     */
    public boolean updateValue(IndexDAO indexArq) {
        try {
            arq = new RandomAccessFile(nomeArquivoIndex, "rw");

            while(arq.getFilePointer() < arq.length()) {
                if(arq.readByte() == indexArq.getId()) {
                    arq.writeLong(indexArq.endereco);  // Atualiza o valor do endereço
                    arq.close();
                    return true;
                }
            }
            arq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Apagar o objeto que foi apagado no arquivo original no arquivo de indices tambem
     * OBS: pode-se tentar implementar algo sem a lapide diminuindo a posicao de cada insercao na posicao inserida
     * @param id -> recebe da funcao CRUD.delete o id que deve ser deletado
     */
    public boolean deleteValue(byte id) {
        //TODO: Entender uma melhor forma para excluir o item do arquivo de indices e implementar
        try {
            arq = new RandomAccessFile(nomeArquivoIndex, "rw");

            while(arq.getFilePointer() < arq.length()) {
                long pos = arq.getFilePointer();
                if(arq.readChar() == ' ') {
                    arq.seek(pos);
                    arq.writeChar('*');
                    arq.close();
                    return true;
                }
            }
            arq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Realiza uma busca binaria no arquivo de indices
     * @param i
     * @return
     */
    public long buscaBinaria(int idParaPesquisa) {
        try {

            RandomAccessFile arq = new RandomAccessFile(nomeArquivoIndex, "rw"); // abre o arquivo ou cria se ele não existir
            long esq = 0, dir = arq.length() / 9, meio;
            byte idArq;
            while(esq <= dir) {
              meio = (int)((esq + dir) / 2);
              arq.seek(meio * 9);
              idArq = arq.readByte();
              if(idParaPesquisa < idArq)
                dir = meio - 1;
              else if(idParaPesquisa > idArq)
                esq = meio + 1;
              else {
                return arq.readLong();
              }
            }
            arq.close();
          } catch(Exception e) {
            e.printStackTrace();
          }
          long lixo = -1;
          return lixo;






        // try {
        //     arq = new RandomAccessFile(nomeArquivoIndex, "rw");

        //     long tam = arq.length();
        //     long dir = tam - 1; // Última posição
        //     long esq = 0;   // Primeira posição
        //     long meio = 0; // Guardar posição do meio

        //     while(esq <= dir) {
        //         meio  = (esq + dir) / 2;
        //         arq.seek(meio);
        //         if(arq.readByte() <= id) {
        //             esq = meio + 1;
        //         } else {
        //             dir = meio - 1;
        //         }
        //     }

        //     if(dir <= tam - 1 && dir >= 0) {
        //         arq.seek(meio);
        //         if(arq.readByte() == id) {
        //             return arq.readByte();
        //         }
        //     }

        //     return -1;
        // } catch (Exception e) {
        //     System.out.println("Falha na busca binária no arquivo de indíces.");
        // }

    }

    /**
     * Mostar o arquivo de indices para testes de funcionamento
     */
    public void showArq() {
        try {
            arq = new RandomAccessFile(nomeArquivoIndex, "rw");

            byte id;
            long endereco;

            IndexDAO index;

            while (arq.getFilePointer() < arq.length()) {
                id = arq.readByte();
                endereco = arq.readLong();

                index = new IndexDAO(id, endereco);
                System.out.println(index);
            }
        } catch (Exception e) {
            System.out.println("Erro ao imprimir o arquivo de indices!");
        }
    }

    public String toString() {
        return "ID......: " + this.id + " ENDEREÇO: " + this.endereco;
    }
}
