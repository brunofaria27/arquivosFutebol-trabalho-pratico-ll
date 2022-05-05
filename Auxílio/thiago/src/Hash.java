import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.lang.reflect.Constructor;

public class Hash<T extends RegistroHash<T>> {

    /** INICIO CLASSE BUCKET */
    class Bucket {

        private Constructor<T> construtor;
        private ArrayList<T> objetoArray;
        private byte profundidadeLocal;
        private short numMax;
        private short numObjetos;
        private short bytesObjeto;
        private short bytesBucket;

        /**
         * Construtor do bucket
         * @param construtor        construtor da classe generica T
         * @param numMax            Numero maximo de objetos no bucket
         * @param profundidadeLocal Profundidade Local do bucket
         */
        public Bucket(Constructor<T> construtor, int numMax, int profundidadeLocal) {
            try {
                // if (qtdmax > 32767)
                //     throw new Exception("Quantidade máxima de 32.767 elementos");
                // if (pl > 127)
                //     throw new Exception("Profundidade local máxima de 127 bits");
                this.construtor = construtor;
                this.profundidadeLocal = (byte) profundidadeLocal;
                this.numMax = (short) numMax;
                this.numObjetos = 0;
                this.objetoArray = new ArrayList<>(numMax);
                this.bytesObjeto = this.construtor.newInstance().size();
                this.bytesBucket = (short) (bytesObjeto * numMax + 3);
            } catch (Exception e) {
                System.out.println("Erro ao criar bucket: " + e.getMessage());
            }
        }

        /**
         * Converte os atributos da classe em um vetor de bytes
         * @return Vetor de bytes com as informações do Paciente
         * @throws IOException caso haja problema com a manipulação do arquivo
         */
        public byte[] toByteArray() throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(profundidadeLocal);
            dos.writeShort(numObjetos);
            int i = 0;

            for(/**/;i<numObjetos;i++){
                dos.write(objetoArray.get(i).toByteArray());
            }

            byte[] vazio = new byte[bytesObjeto];
            
            for(/**/;i<numMax;i++){
                dos.write(vazio);
            }
            
            return baos.toByteArray();
        }

        /**
         * Converte o vetor de bytes em atributos da classe
         * @param ba Vetor de bytes com as informações do Paciente
         * @throws IOException caso haja problema com a manipulação do arquivo
         */
        public void fromByteArray(byte[] ba) throws Exception {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeLocal = dis.readByte();
            numObjetos = dis.readShort();
            objetoArray = new ArrayList<>(numMax);
            byte[] dados = new byte[bytesObjeto];
            T objeto;
            for(int i=0;i<numMax;i++){
                dis.read(dados);
                objeto = construtor.newInstance();
                objeto.fromByteArray(dados);
                objetoArray.add(objeto);
            }
        }

        /**
         * Verifica se bucket está vazio
         * @return true caso bucket estiver vazio
         */
        public boolean empty() {
            return numObjetos == 0;
        }

        /**
         * Verifica se bucket está cheio
         * @return true caso bucket estiver cheio
         */
        public boolean full() {
            return numObjetos == numMax;
        }

        /**
         * Cria Objeto (id,end) no bucket
         * @param objeto Objeto a ser criado
         * @return true caso for criado
         */
        public boolean create(T objeto) {
            if (full()) {
                return false;
            }
            int i = numObjetos - 1;
            while (i >= 0 && objeto.hashCode() < objetoArray.get(i).hashCode()) {
                i--;
            }
            objetoArray.add(i + 1, objeto);
            numObjetos++;
            return true;
        }

        /**
         * Le Objeto a partir da chave (id)
         * @param chave Id do objeto a ser lido
         * @return Objeto (id, end) lido
         */
        public T read(int chave) {
            if (empty()) {
                return null;
            }
            int i = 0;
            while (i < numObjetos && chave > objetoArray.get(i).hashCode()) {
                i++;
            }
            if (i < numObjetos && chave == objetoArray.get(i).hashCode()) {
                return objetoArray.get(i);
            } else {
                return null;
            }
        }

        /**
         * Atualiza Objeto (id,end)
         * @param objeto Objeto novo
         * @return true caso for atualizado
         */
        public boolean update(T objeto) {
            if (empty()) {
                return false;
            }
            int i = 0;
            while (i < numObjetos && objeto.hashCode() > objetoArray.get(i).hashCode()) {
                i++;
            }
            if (i < numObjetos && objeto.hashCode() == objetoArray.get(i).hashCode()) {
                objetoArray.set(i, objeto);
                return true;
            } else {
                return false;
            }
        }

        /**
         * Deleta Objeto (id.end)
         * @param chave Id do objeto a ser deletado
         * @return return true caso for deletado
         */
        public boolean delete(int chave) {
            if (empty()) {
                return false;
            }
            int i = 0;
            while (i < numObjetos && chave > objetoArray.get(i).hashCode()) {
                i++;
            }
            if (chave == objetoArray.get(i).hashCode()) {
                objetoArray.remove(i);
                numObjetos--;
                return true;
            } else {
                return false;
            }
        }

        /**
         * Tamanho do bucket em bytes
         * @return Tamanho do bucket em bytes
         */
        public int size() {
            return bytesBucket;
        }

    }
    /** FIM CLASSE BUCKET */

    /** INICIO CLASSE DIRETORIO */
    class Diretorio {

        private byte profundidadeGlobal;
        private long[] endArray;

        /**
         * Construtor do Diretorio
         */
        public Diretorio() {
            profundidadeGlobal = 0;
            endArray = new long[1];
            endArray[0] = 0;
        }

        /**
         * Converte os atributos da classe em um vetor de bytes
         * @return Vetor de bytes com as informações do Paciente
         * @throws IOException caso haja problema com a manipulação do arquivo
         */
        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(profundidadeGlobal);
            int quantidade = (int) Math.pow(2, profundidadeGlobal);
            int i = 0;
            while (i < quantidade) {
                dos.writeLong(endArray[i]);
                i++;
            }
            return baos.toByteArray();
        }

        /**
         * Converte o vetor de bytes em atributos da classe
         * @param ba Vetor de bytes com as informações do Paciente
         * @throws IOException caso haja problema com a manipulação do arquivo
         */
        public void fromByteArray(byte[] ba) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeGlobal = dis.readByte();
            int quantidade = (int) Math.pow(2, profundidadeGlobal);
            endArray = new long[quantidade];
            int i = 0;
            while (i < quantidade) {
                endArray[i] = dis.readLong();
                i++;
            }
        }

        /**
         * Atualiza endereço do bucket
         * @param pos Posição do endereço do bucket no Diretorio
         * @param end Novo Endereço
         * @return true caso for atualizado
         */
        public boolean atualizaEnd(int pos, long end) {
            if (pos > Math.pow(2, profundidadeGlobal))
                return false;
            endArray[pos] = end;
            return true;
        }

        /**
         * Retorna Diretorio na posição pos
         * @param pos Posição do diretorio
         * @return Diretorio na posição pos
         */
        public long endereco(int pos) {
            if (pos > Math.pow(2, profundidadeGlobal))
                return -1;
            return endArray[pos];
        }

        /**
         * Duplica o tamanho do Diretorio
         * @return true caso funcionar
         */
        public boolean duplica() {
            if (profundidadeGlobal == 127) {
                return false;
            }
            profundidadeGlobal++;

            int q1 = (int) Math.pow(2, profundidadeGlobal - 1);
            int q2 = (int) Math.pow(2, profundidadeGlobal);

            long[] endArray2 = new long[q2];
            int i = 0;
            while (i < q1) {
                endArray2[i] = endArray[i];
                i++;
            }
            while (i < q2) {
                endArray2[i] = endArray[i - q1];
                i++;
            }

            endArray = endArray2;
            return true;
        }

        /**
         * Função hash para determinar o bucket que um objeto será inserido
         * @param chave Id do Objeto
         * @return Posição no diretorio do Objeto 
         */
        public int hash(int chave){
            return ( Math.abs(chave) % ((int) Math.pow(2, profundidadeGlobal)));
        }

        /**
         * Função hash considerando a profundidade local
         * @param chave             Id do objeto
         * @param profundidadeLocal Profundidade local
         * @return Posição no diretorio do Objeto
         */
        public int hash2(int chave, int profundidadeLocal){
            return (Math.abs(chave) % ((int) Math.pow(2, profundidadeLocal)));
        }

    }
    /** FIM CLASSE DIRETORIO */

    private final String nomeArquivoDir = "pacientes_dir.db";
    private final String nomeArquivoBucket    = "pacientes_bucket.db";
    private RandomAccessFile arqDir, arqBucket;
    private int numMaxBucket;
    private Diretorio dir;
    Constructor<T> construtor;

    /**
     * Construtor do Hash
     * @param construtor Construtor da classe generica T 
     * @param numMaxBucket Numero maximo de objetos no bucket
     */
    public Hash(Constructor<T> construtor, int numMaxBucket){
        try{
            this.construtor = construtor;
            this.numMaxBucket = numMaxBucket;
            // inicializa arqs
            arqDir = new RandomAccessFile(nomeArquivoDir, "rw");
            arqBucket = new RandomAccessFile(nomeArquivoBucket, "rw");
            
            // se arquivos nao existirem, inicializa diretorio e bucket
            if(arqDir.length() == 0 || arqBucket.length() == 0){
                dir = new Diretorio();
                arqDir.write(dir.toByteArray());

                Bucket bucket = new Bucket(this.construtor, numMaxBucket,0);
                arqBucket.seek(0); // ?
                arqBucket.write(bucket.toByteArray());
            }

        } catch(Exception e){
            System.out.println("Erro ao criar Hash "+ e.getMessage());
        }

    }

    /**
     * Cria objeto na estrutura do hash extensível
     * @param objeto Objeto a ser criado
     * @return true caso for criado
     */
    public boolean create(T objeto) throws Exception{
        
        // Carrega o diretório
        byte[] bd = new byte[(int) arqDir.length()];
        arqDir.seek(0);
        arqDir.read(bd);
        dir = new Diretorio();
        dir.fromByteArray(bd);

        // Identifica a hash do diretório,
        int i = dir.hash(objeto.hashCode());

        // Recupera o cesto
        long endBucket = dir.endereco(i);
        Bucket bucket = new Bucket(construtor, numMaxBucket,0);
        byte[] ba = new byte[bucket.size()];
        arqBucket.seek(endBucket);
        arqBucket.read(ba);
        bucket.fromByteArray(ba);

        // Testa se a chave já não existe no cesto
        if (bucket.read(objeto.hashCode()) != null) throw new Exception("Objeto já existe");

        // Testa se o cesto já não está cheio
        // Se não estiver, create o par de chave e dado
        if (!bucket.full()) {
            // Insere a chave no cesto e o atualiza
            bucket.create(objeto);
            arqBucket.seek(endBucket);
            arqBucket.write(bucket.toByteArray());
            return true;
        }

        // Duplica o diretório
        byte pl = bucket.profundidadeLocal;
        if (pl >= dir.profundidadeGlobal)
        dir.duplica();
        byte pg = dir.profundidadeGlobal;

        // Cria os novos cestos, com os seus dados no arquivo de cestos
        Bucket c1 = new Bucket(construtor, numMaxBucket, pl + 1);
        arqBucket.seek(endBucket);
        arqBucket.write(c1.toByteArray());

        Bucket c2 = new Bucket(construtor, numMaxBucket, pl + 1);
        long novoEndBucket = arqBucket.length();
        arqBucket.seek(novoEndBucket);
        arqBucket.write(c2.toByteArray());

        // Atualiza os dados no diretório
        int inicio = dir.hash2(objeto.hashCode(), bucket.profundidadeLocal);
        int deslocamento = (int) Math.pow(2, pl);
        int max = (int) Math.pow(2, pg);
        boolean troca = false;
        for (int j = inicio; j < max; j += deslocamento) {
            if (troca) dir.atualizaEnd(j, novoEndBucket);
            troca = !troca;
        }

        // Atualiza o arquivo do diretório
        bd = dir.toByteArray();
        arqDir.seek(0);
        arqDir.write(bd);

        // Reinsere as chaves
        for (int j = 0; j < bucket.numObjetos; j++) {
            create(bucket.objetoArray.get(j));
        }
        create(objeto);
        return false;
    }
        
    /**
     * Le objeto a partir de sua ID
     * @param chave Id do objeto a ser lido
     * @return Objeto lido
     */
    public T read(int chave) throws Exception{

        byte[] b = new byte[ (int) arqDir.length() ];
        arqDir.seek(0);
        arqDir.read(b);
        dir = new Diretorio();
        dir.fromByteArray(b);

        int i = dir.hash(chave);

        long endBucket = dir.endereco(i);
        Bucket bucket = new Bucket(construtor, numMaxBucket, 0);
        byte[] ba = new byte[bucket.size()]; 
        arqBucket.seek(endBucket);
        arqBucket.read(ba);
        bucket.fromByteArray(ba);
        
        return bucket.read(chave);
    }

    /**
     * Atualiza objeto na estrutura 
     * @param objeto Novo objeto    
     * @return true caso for atualizado
     */
    public boolean update(T objeto) throws Exception{

        byte[] bd = new byte[(int) arqDir.length()];
        arqDir.seek(0);
        arqDir.read(bd);
        dir = new Diretorio();
        dir.fromByteArray(bd);

    
        int i = dir.hash(objeto.hashCode());

        long endBucket = dir.endereco(i);
        Bucket bucket = new Bucket(construtor, numMaxBucket,0);
        byte[] ba = new byte[bucket.size()];
        arqBucket.seek(endBucket);
        arqBucket.read(ba);
        bucket.fromByteArray(ba);

        if (!bucket.update(objeto)) return false;

        arqBucket.seek(endBucket);
        arqBucket.write(bucket.toByteArray());
        return true;
    }

    /**
     * Deleta objeto na estrutura 
     * @param chave Id do objeto a ser deletado
     * @return true caso for deletado
     */
    public boolean delete(int chave) throws Exception{

        byte[] bd = new byte[(int) arqDir.length()];
        arqDir.seek(0);
        arqDir.read(bd);
        dir = new Diretorio();
        dir.fromByteArray(bd);

        int i = dir.hash(chave);

        long endBucket = dir.endereco(i);
        Bucket bucket = new Bucket(construtor, numMaxBucket,0);
        byte[] ba = new byte[bucket.size()];
        arqBucket.seek(endBucket);
        arqBucket.read(ba);
        bucket.fromByteArray(ba);

        if (!bucket.delete(chave))
        return false;

        arqBucket.seek(endBucket);
        arqBucket.write(bucket.toByteArray());
        return true;
    }

    /**
     * Imprime na tela o conteúdo atual do Hash Extensível
     */
    public void imprimir(){
        try {
            byte[] bd = new byte[(int) arqDir.length()];
            arqDir.seek(0);
            arqDir.read(bd);
            dir = new Diretorio();
            dir.fromByteArray(bd);
            System.out.println("\n Diretório :");
            System.out.println(dir);

            System.out.println("\nBuckets :");
            arqBucket.seek(0);

            long pos;

            while (arqBucket.getFilePointer() != arqBucket.length()) {
                pos = arqBucket.getFilePointer();
                System.out.println("Endereço: "+ pos);
                Bucket bucket = new Bucket(construtor, numMaxBucket,0);
                byte[] ba = new byte[bucket.size()];
                arqBucket.read(ba);
                bucket.fromByteArray(ba);
                System.out.println(bucket);
            }
        } catch (Exception e) {
            System.out.println("Erro ao imprimir estrutura Hash: "+e.getMessage());
        }    
    }
}
