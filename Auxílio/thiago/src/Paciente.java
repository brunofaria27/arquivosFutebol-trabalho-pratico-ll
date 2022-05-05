import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class Paciente implements Registro{
    public int id;
    public String nome;
    public Date dataDeNascimento;
    public char sexo;
    public String anotacoes;
    public final int M = 255;
    
    /**
     * Construtor vazio da Classe   
     */
    public Paciente(){
        this.id = -1;
        this.nome = "";
        this.dataDeNascimento = new Date();
        this.sexo = ' ';
        this.anotacoes = "";
    }

    /**
     * Construtor da Classe
     * @param id ID do Paciente
     * @param nome Nome do Paciente
     * @param dataDeNascimento Data de nascimento do Paciente
     * @param sexo Sexo do Paciente
     * @param anotacoes Anotações sobre Paciente
     */
    public Paciente(int id, String nome, Date dataDeNascimento, char sexo, String anotacoes) {
        this.id = id;
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.sexo = sexo;
        this.anotacoes = anotacoes;
    }

    /**
     * Imprime os atributos do Paciente
     */
    @Override
    public String toString() {
        return "\nID..............: " + this.id + "\nNome............: " + this.nome + "\nDataDeNascimento: "+ dateUtil.dateToString(this.dataDeNascimento) + "\nSexo............: " + this.sexo + "\nAnotações.......: " + this.anotacoes;
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
        dos.writeUTF(nome);
        dos.writeUTF(dateUtil.dateToString(dataDeNascimento));
        dos.writeChar(sexo);
        dos.writeUTF(anotacoes);
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
        id = dis.readInt();
        nome = dis.readUTF();
        dataDeNascimento = dateUtil.stringToDate(dis.readUTF());
        sexo = dis.readChar();
        anotacoes = dis.readUTF();
    }

    /**
     * Envia o id do Paciente
     * @return Id do Paciente
     */
    @Override
    public int getID() {
        return id;
    }

    /**
     * Recebe um inteiro como Parâmetro e o define como id do Paciente
     * @param id Novo id do Paciente
     */
    @Override
    public void setID(int id) {
        this.id = id;
    }

}