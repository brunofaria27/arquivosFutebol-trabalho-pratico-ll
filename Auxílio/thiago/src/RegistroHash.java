import java.io.IOException;

/**
 * Interface para classe Hash
 */
public interface RegistroHash<T> {
    public int hashCode();
    public short size();
    public Long getEnd(); 
    public byte[] toByteArray() throws IOException;
    public void fromByteArray(byte[] ba) throws IOException;
}
