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

            arq.seek(arq.length()); // Navegar para a última posição do arquivo
            arq.writeByte(indexArq.getId());
            arq.writeLong(indexArq.getEndereco());
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
            arq = new RandomAccessFile(nomeArquivoIndex, "r");
            long tamanhoDoArquivo = arq.length();
            long direitaDoArquivo = tamanhoDoArquivo - 1;
            long esquerdaDoArquivo = 0;
            long meioDoArquivo = 0;
            long posNoArquivoDeDados = 0;
            
            if(tamanhoDoArquivo != 0){  
                System.out.println();
                System.out.println("------------------DEBUG 1------------------");
                System.out.println("DEBUG: Meio da merda do Arquivo -> "+      meioDoArquivo);
                System.out.println("DEBUG: direita da merda do Arquivo -> "+   direitaDoArquivo);
                System.out.println("DEBUG: esquerda da merda do Arquivo -> "+  esquerdaDoArquivo);
                System.out.println("------------------DEBUG 1------------------");
                System.out.println();

                //Separar o arquivo em bloco de byte e long
                // separa  o bloco

                while(esquerdaDoArquivo <= direitaDoArquivo) {
                    meioDoArquivo = (esquerdaDoArquivo + direitaDoArquivo) / 2;
                    System.out.println("------------------DEBUG teste------------------");
                    
                    System.out.println(meioDoArquivo);
                    System.out.println(tamanhoDoArquivo);
                    System.out.println("------------------DEBUG teste------------------");

                    arq.seek(meioDoArquivo);

                    if(arq.readByte() <= idParaPesquisa) {
                        System.out.println("teste " + arq.read());
                      esquerdaDoArquivo = meioDoArquivo + 1;
                    } else {
                        direitaDoArquivo = meioDoArquivo - 1;
                    }

                    System.out.println();
                    System.out.println("------------------DEBUG 2------------------");
                    System.out.println("DEBUG: Meio da merda do Arquivo -> "+meioDoArquivo);
                    System.out.println("DEBUG CONTEUDO: " + arq.readByte());
                    arq.seek(meioDoArquivo+1);
                    System.out.println("DEBUG pos-1: " + arq.readByte());
                    arq.seek(meioDoArquivo);
                    System.out.println("-------------------------------------------");
                    System.out.println("DEBUG: direita da merda do Arquivo -> "+   direitaDoArquivo);
                    System.out.println("DEBUG: esquerda da merda do Arquivo -> "+  esquerdaDoArquivo);
                    System.out.println("------------------DEBUG 2------------------");
                    System.out.println();

                }

                if(direitaDoArquivo <= tamanhoDoArquivo - 1 && direitaDoArquivo >= 0) {
                    arq.seek(meioDoArquivo);

                    if(arq.readByte() == idParaPesquisa) {

                        arq.seek(meioDoArquivo);
                        posNoArquivoDeDados = arq.readByte();

                        System.out.println();
                        System.out.println("------------------DEBUG 3------------------");
                        System.out.println("DEBUG: "+ posNoArquivoDeDados);
                        System.out.println("------------------DEBUG 3------------------");
                        System.out.println();

                        return posNoArquivoDeDados;
                     }
                 }

            }else{
                System.out.println("Sem arquivo");
            }



        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("deu ruim na binaria");
        }






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
        return -1;
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
