import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class CRUD<T extends Registro> {

	private RandomAccessFile arq;
	private Constructor<T> construtor;
	private Hash<bucketPaciente> hashExt;
	private final String nomeArquivo = "pacientes.db";
	private final char DELETADO = '*';

	/**
	 * Construtor do CRUD
	 * @param c Construtor da classe generica T
	 */
	public CRUD(Constructor<T> construtor) {
		try {
			this.construtor = construtor;
			arq = new RandomAccessFile(this.nomeArquivo, "rw");
			if (arq.length() == 0) {
				arq.writeInt(0);
			}
			hashExt = new Hash<>(bucketPaciente.class.getConstructor(), 4);

		} catch (Exception e) {
			System.out.println("Erro ao criar CRUD: " + e.getMessage());
		}
	}

	/**
	 * Insere Paciente no arquivo
	 * @param objeto Paciente a ser inserido
	 * @return ID do objeto Paciente criadp
	 */
	public int create(T objeto) {
		try {
			/** TRY */

			if (arq.length() == 0) {
				arq.writeInt(0);
			}

			arq.seek(0);
			int ultimoID = arq.readInt();
			objeto.setID(ultimoID + 1);
			arq.seek(0);
			arq.writeInt(objeto.getID());

			// cria byteArray do objeto
			arq.seek(arq.length());
			byte[] b = objeto.toByteArray();
			long pos = arq.getFilePointer();

			// escreve L�pide
			arq.writeByte(' ');

			// escreve Tamanho do array
			arq.writeInt(b.length);

			// escreve Array
			arq.write(b);

			// cria estrutura do hash extensivel
			hashExt.create(new bucketPaciente(objeto.getID(),pos));

			/** FIM TRY */
		} catch (Exception e) {
			System.out.println("Erro ao inserir no arquivo:" + e.getMessage());
		}
		return objeto.getID();
	}

	/**
	 * Le Paciente do arquivo
	 * @param id Id do paciente a ser lido
	 * @return objeto lido
	 */
	public T read(int id) {
		try {
			/** TRY */
			arq.seek(4);
			// pular cabecalho

			bucketPaciente bPaciente = hashExt.read(id);
			long pos = bPaciente.getEnd();
			byte lapide;
			byte[] b;
			int tam;
			T objeto;

			if (pos != -1) {
				arq.seek(pos);
				lapide = arq.readByte();
				tam = arq.readInt();
				b = new byte[tam];
				arq.read(b);
				if (lapide != DELETADO) {
					objeto = this.construtor.newInstance();
					objeto.fromByteArray(b);
					if (objeto.getID() == id) {
						return objeto;
					}
				}
			}

			/** FIM TRY */
		} catch (Exception e) {
			System.out.println("Erro ao ler do arquivo:" + e.getMessage());
		}
		return null;
	}

	/**
	 * Atualiza um paciente inserido no arquivo
	 * @param novoObjeto Paciente a ser atualizado
	 * @return booleano caso exista paciente no arquivo
	 */
	public boolean update(T novoObjeto) {
		try {
			/** TRY */
			arq.seek(4);

			bucketPaciente bPaciente = hashExt.read(novoObjeto.getID());
			long pos = bPaciente.getEnd();
			byte lapide;
			byte[] b;
			byte[] novoB;
			int tam;
			T objeto;

			if (pos != -1){

				arq.seek(pos);
				lapide = arq.readByte();
				tam = arq.readInt();
				b = new byte[tam];
				arq.read(b);
				
				if (lapide != DELETADO) {
					objeto = this.construtor.newInstance();
					objeto.fromByteArray(b);
					novoB = novoObjeto.toByteArray();
					if (novoB.length <= b.length) {
						arq.seek(pos);
						arq.writeByte(' ');
						arq.writeInt(tam);
						arq.write(novoB);
					} else {
						arq.seek(pos);
						arq.writeByte(DELETADO);
						arq.seek(arq.length());
						pos = arq.getFilePointer();
						arq.writeByte(' ');
						arq.writeInt(novoB.length);
						arq.write(novoB);
						hashExt.update( new bucketPaciente(novoObjeto.getID(),pos) );
					}
					return true;
				}
			}

			/** FIM TRY */
		} catch (Exception e) {
			System.out.println("Erro ao atualizar o registro:" + e.getMessage());
		}
		return false;
	}

	/**
	 * Deleta Paciente do Arquivo
	 * @param id Id do paciente a ser removido
	 * @return Booleano caso seja removido.
	 */
	public boolean delete(int id) {
		try {
			/** TRY */
			arq.seek(4);
			// pular cabecalho

			bucketPaciente bPaciente = hashExt.read(id);
			long pos = bPaciente.getEnd();
			byte lapide;
			int tam;
			byte[] b;
			T objeto;

			if (pos != -1){
				arq.seek(pos);
				lapide = arq.readByte();
				tam = arq.readInt();
				b = new byte[tam];
				if (lapide != DELETADO){
					objeto = this.construtor.newInstance();
					objeto.fromByteArray(b);
					arq.seek(pos);
					arq.writeByte(DELETADO);
					hashExt.delete(id);
					return true;
				}
			}

			/** FIM TRY */
		} catch (Exception e) {
			System.out.println("Erro ao deletar o registro:" + e.getMessage());
		}
		return false;
	}

	/** 
     * Imprime todos Clientes do arquivo
     */
    public void readAll() throws Exception {
        arq.seek(4);
        //pular cabeçalho

        byte lapide;
        byte[] b;
        int tam;
        Paciente objeto;
        while(arq.getFilePointer() < arq.length()) {
            lapide = arq.readByte();
            tam = arq.readInt();
            b = new byte[tam];
            arq.read(b);
            if(lapide != DELETADO) {
                objeto = new Paciente();
                objeto.fromByteArray(b);
                System.out.println(objeto.toString());
            }
        }

    }


}
