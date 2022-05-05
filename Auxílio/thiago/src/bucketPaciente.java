import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class bucketPaciente implements RegistroHash<bucketPaciente> {

    private int id;
    private long end;
    private final short TAM = 30;

    public bucketPaciente(){
        this(-1,0);
    }

    public bucketPaciente(int id, long end){
        this.id = id;
        this.end = end;
    }

    @Override
    public int hashCode(){
        return this.id;
    }    

    public Long getEnd(){
        return this.end;
    }

    @Override
    public String toString(){
        return "ID: "+this.id+" END: "+this.end;
    }

    public short size(){
        return this.TAM;
    }

    /**
     * Converte os atributos da classe em um vetor de bytes
     * @return Vetor de bytes com as informações do Paciente
     * @throws IOException caso haja problema com a manipulação do arquivo
     */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeLong(end);
        byte[] bs = baos.toByteArray();
        byte[] bs2 = new byte[TAM];
        for (int i = 0; i < TAM; i++)
            bs2[i] = ' ';
        for (int i = 0; i < bs.length && i < TAM; i++)
            bs2[i] = bs[i];
        return bs2;
    }
    

    /**
     * Converte o vetor de bytes em atributos da classe
     * @param ba Vetor de bytes com as informações do Paciente
     * @throws IOException caso haja problema com a manipulação do arquivo
     */
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.end = dis.readLong();
    }    

}
