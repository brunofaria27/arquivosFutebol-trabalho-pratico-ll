import java.io.IOException;

/**
 * Interface para classe Paciente.
 */
public interface Registro {
    public int getID();
    public void setID(int n);
    public byte[] toByteArray() throws IOException;
    public void fromByteArray(byte[] ba) throws IOException;
  }
  