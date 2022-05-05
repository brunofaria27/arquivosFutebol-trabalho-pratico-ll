import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main{
    /**
     * Integrantes do Trabalho: Reynaldo Villar Garavini, Thiago Amado Costa
     */
    public static void main(String[] args) {

        CRUD<Paciente> crud;
        try {
            crud = new CRUD<>(Paciente.class.getConstructor());
            Scanner in = new Scanner(System.in);
            int menu;
            final int k = 4000000;

            do{
                System.out.println("\n1- Inserir Ficha de Paciente");
                System.out.println("2- Imprimir Ficha de Paciente");
                System.out.println("3- Atualizar Ficha de Paciente");
                System.out.println("4- Deletar Ficha de Paciente");
                System.out.println("5- Imprimir Arquivos");
                System.out.println("6- Rodar Simulação");
                System.out.println("0- Sair\n");
                System.out.print("Opção: ");
                menu = in.nextInt();
                //avança para proxima linha
                in.nextLine();
                System.out.print("\n");

                switch(menu) {
                    //Inserir
                    case 1: 
                        //ler nome
                        System.out.print("Nome............: ");
                        String nome1 = in.nextLine();
                        System.out.print("\n");
                        //ler data de nascimento
                        String strData1;
                        boolean formatado1 = false;
                        Date dataDeNascimento1 = new Date();
                        do{
                            System.out.print("DataDeNascimento: ");
                            strData1 = in.nextLine();
                            formatado1 = dateUtil.validaData(strData1);
                            if(formatado1==true) {
                                dataDeNascimento1 = dateUtil.stringToDate(strData1);
                            } else {
                                System.out.println("Data Inválida!!!");
                            }                   
                        } while(formatado1==false);
                        System.out.print("\n");
                        //ler sexo
                        System.out.print("Sexo(M/F).......: ");
                        char sexo1 = in.nextLine().charAt(0);
                        System.out.print("\n");
                        //ler anotações
                        System.out.print("Anotações.......: ");
                        String anotacoes1 = in.nextLine();
                        System.out.println("\n");

                        //cria objeto paciente
                        Paciente p1 = new Paciente(-1, nome1, dataDeNascimento1, sexo1, anotacoes1);

                        //inicia medidor de tempo
                        long inicioCreate = dateUtil.now();
                        //função inserir do CRUD passando o novo paciente como parâmetro
                        crud.create(p1);
                        //finaliza medidor de tempo
                        long fimCreate = dateUtil.now();
                        //imprime tempo de execução do crud.create()
                        System.out.println("Tempo de Execução: " + (fimCreate-inicioCreate) + " ms");

                        break;
                    //Imprimir
                    case 2:
                        //ler id
                        System.out.print("ID: ");
                        int id2 = in.nextInt();
                        System.out.print("\n");

                        //inicia medidor de tempo
                        long inicioRead = dateUtil.now();
                        //função imprimir do CRUD
                        Paciente p2 = crud.read(id2);
                        //finaliza medidor de tempo
                        long fimRead = dateUtil.now();
                        //imprime tempo de execução do crud.read()
                        System.out.println("Tempo de Execução: " + (fimRead-inicioRead) + " ms");

                        if(p2 != null) {
                            //imprime informações do paciente na tela
                            System.out.println(p2.toString());                            
                        } else {
                            //mensagem de erro
                            System.out.println("Paciente de ID " + id2 + " não existe");
                        }
                        break;
                    //Atualizar
                    case 3: 
                        //ler id
                        System.out.print("ID...: ");
                        int id3 = in.nextInt();
                        System.out.print("\n");
                        //avança para proxima linha
                        in.nextLine();
                        //ler nome
                        System.out.print("Nome............: ");
                        String nome3 = in.nextLine();
                        System.out.print("\n");
                        //ler data de nascimento
                        String strData3;
                        boolean formatado3 = false;
                        Date dataDeNascimento3 = new Date();
                        do{
                            System.out.print("DataDeNascimento: ");
                            strData3 = in.nextLine();
                            formatado3 = dateUtil.validaData(strData3);
                            if(formatado3==true) {
                                dataDeNascimento3 = dateUtil.stringToDate(strData3);
                            } else {
                                System.out.println("Data Inválida!!!");
                            }                   
                        } while(formatado3==false);
                        //ler sexo
                        System.out.print("Sexo(M/F).......: ");
                        char sexo3 = in.nextLine().charAt(0);
                        System.out.print("\n");
                        //ler anotações
                        System.out.print("Anotações.......: ");
                        String anotacoes3 = in.nextLine();
                        System.out.println("\n");

                        //cria objeto paciente
                        Paciente p3 = new Paciente(id3, nome3, dataDeNascimento3, sexo3, anotacoes3);

                        

                        //inicia medidor de tempo
                        long inicioUpdate = dateUtil.now();
                        //função atualizar CRUD passando o novo paciente como parâmetro
                        crud.update(p3);
                        //finaliza medidor de tempo
                        long fimUpdate = dateUtil.now();
                        //imprime tempo de execução do crud.update()
                        System.out.println("Tempo de Execução: " + (fimUpdate-inicioUpdate) + " ms");

                        break;
                    //Deletar
                    case 4:
                        //ler id
                        System.out.print("ID: ");
                        int id4 = in.nextInt();
                        System.out.print("\n");
                        
                        //inicia medidor de tempo
                        long inicioDelete = dateUtil.now();
                        //função delete CRUD
                        boolean deletado = crud.delete(id4);
                        //finaliza medidor de tempo
                        long fimDelete = dateUtil.now();
                        //imprime tempo de execução do crud.delete()
                        System.out.println("Tempo de Execução: " + (fimDelete-inicioDelete) + " ms");

                        if(deletado) {
                            System.out.println("Paciente deletado com sucesso!");
                        } else {
                            System.out.println("Erro ao deletar paciente!");
                        }

                        break;

                    case 5:
                        //função ler todos os paciente do CRUD
                        crud.readAll();
                        break;
                    //Simulação
                    case 6:
                        long tempoTotalCreate = 0;
                        long tempoTotalRead = 0;
                        Paciente[] p = new Paciente[k];

                        for(int i = 0; i < k; i++) {
                            String nomeTmp = "Nome" + i + " Sobrenome" + i + " Sobrenome" + i;
                            Date dataDeNascimentoTmp = new GregorianCalendar(2021, Calendar.JANUARY, i%12).getTime();
                            char sexoTmp = (i%2 == 0) ? 'M' : 'F';
                            String anotacoesTmp = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis,";

                            //cria objeto paciente
                            p[i] = new Paciente(-1, nomeTmp, dataDeNascimentoTmp, sexoTmp, anotacoesTmp);
                        }

                        for(int i = 0; i < k; i++) {
                            //inicia medidor de tempo
                            long inicioCreateTmp = dateUtil.now();
                            //função inserir do CRUD passando o novo paciente como parâmetro
                            crud.create(p[i]);
                            //finaliza medidor de tempo
                            long fimCreateTmp = dateUtil.now();
                            //armazena o intervalo de tempo da função create()
                            tempoTotalCreate += (fimCreateTmp-inicioCreateTmp);
                        }

                        for(int i = 0; i < k; i++) {
                            //inicia medidor de tempo
                            long inicioReadTmp = dateUtil.now();
                            //função inserir do CRUD passando o novo paciente como parâmetro
                            crud.read(i);
                            //finaliza medidor de tempo
                            long fimReadTmp = dateUtil.now();
                            //armazena o intervalo de tempo da função read()
                            tempoTotalRead += (fimReadTmp-inicioReadTmp);
                        }

                        System.out.println("------------------------------------------------Resultados------------------------------------------------");
                        System.out.println("Tempo de Execução das Inserções: " + (tempoTotalCreate) + "m");
                        System.out.println("Tempo de Execução das Pesquisas: " + (tempoTotalRead) + "m");

                        break;
                    //Sair
                    case 0: 
                        //mensagem de saída do programa
                        System.out.println("Programa Encerrado!");
                        break;
                    //Erro - Número Inválido
                    default:
                        System.out.println("Número Inválido!");
                        break;
                }

            } while(menu!=0);

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}