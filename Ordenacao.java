import java.io.RandomAccessFile;

public class Ordenacao {
    static String numeroArquivo;

    // mod de 3 para forçar o número de arquivos
    public RandomAccessFile qualArquivo(int i, RandomAccessFile arquivo1, RandomAccessFile arquivo2, RandomAccessFile arquivo3, RandomAccessFile arquivo4) {
        switch (i % 3) {
        case 0:
            return arquivo1;   
        case 1:
            return arquivo2;
        case 2:
            return arquivo3; 
        default:
            return arquivo4;
        }
    }

  public void qualArquivoString(int i) {
        switch (i % 3) {
        case 0:
            numeroArquivo = "arquivoTemporario1.db";   
            break;
        case 1:
            numeroArquivo = "arquivoTemporario2.db"; 
            break;
        case 2:
            numeroArquivo = "arquivoTemporario3.db";  
            break;
        default:
            numeroArquivo = "arquivoTemporario4.db";  
            break;
        }
    }
}
