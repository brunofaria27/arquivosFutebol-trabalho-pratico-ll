import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

class dateUtil {

     /**
     * Transforma variável Date em String
     * @param data Data que será transformada em String
     * @return Data em formato de String
     */
    public static String dateToString(Date data) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strData = formatter.format(data);
        return strData;
    }

    /**
     * Transforma variável String em Date
     * @param data Data que será transformada em String
     * @return Data em formato de Date
     */
    public static Date stringToDate(String strData) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        try{
            data = formatter.parse(strData);            
        } catch(Exception e) {
            System.out.println("Data Inválida ! digite no formato [dd/MM/yyyy] "+e.getMessage());
        }

        return data;
    }

    /**
     * Validação do formato de data [dd/MM/yyyy]
     * @param strData Data informada pelo usuario
     * @return true caso data estiver em formato certo
     */
    public static boolean validaData(String strData) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
             formatter.parse(strData);
             return true;
        }
        catch(ParseException e){
             return false;
        }
    }

    /**
     * Calcula o tempo de execução
     * @return tempo de execução em milisegundos
     */
    public static long now() {
        return new Date().getTime();
    }

}
