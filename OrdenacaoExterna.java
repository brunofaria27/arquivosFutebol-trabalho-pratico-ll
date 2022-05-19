import java.io.*;

public class OrdenacaoExterna {
    void OrdeneExterno()
{ int NBlocos = 0;
  ArqEntradaTipo ArqEntrada, ArqSaida;
  ArqEntradaTipo[OrdemIntercalConst] ArrArqEnt;
  short Fim;
  int Low, High, Lim;
  NBlocos = 0;
  ArqEntrada = abrir arquivo a ser ordenado;
  do   /*Formacao inicial dos NBlocos ordenados */
    { NBlocos++;
      Fim = EnchePaginas(NBlocos, ArqEntrada);
      OrdeneInterno;
      ArqSaida = AbreArqSaida(NBlocos);
      DescarregaPaginas(ArqSaida);
      fclose(ArqSaida); 
    } while (!Fim);
  fclose(ArqEntrada);
  Low = 0;
  High = NBlocos-1;
  while (Low < High) /* Intercalacao dos NBlocos ordenados */
    { Lim = Minimo(Low + OrdemIntercalConst-1, High);
      AbreArqEntrada(ArrArqEnt, Low, Lim);
      High++;
      ArqSaida = AbreArqSaida(High);
      Intercale(ArrArqEnt, Low, Lim, ArqSaida);
      fclose(ArqSaida);
      for(i= Low; i < Lim; i++)
        { fclose(ArrArqEnt[i]);
          Apague_Arquivo(ArrArqEnt[i]);
        }
      Low += OrdemIntercalConst;
    }
  Mudar o nome do arquivo High para o nome fornecido pelo usuario;
}
}