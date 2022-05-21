// Author: Luiz Henrique Motta Dias e Athos Geraldo Salomon Dolabela da Silveira
import java.io.*;
import java.util.Scanner;
import javax.sound.midi.SysexMessage;

public class Main {
    //Para o menu aparecer
    public static void menu() {
        System.out.println("\n1 - Criar time");
        System.out.println("2 - Criar partida");
        System.out.println("3 - Apagar time");
        System.out.println("4 - Buscar times");
        System.out.println("5 - Todos os Times");
        System.out.println("6 - Atualizar dados do time");
        System.out.println("0 - Sair do menu");
    }
    //Leitura dos demais ID's
    public static int getLastId(int id) {
        RandomAccessFile arq;
        int idReturn;

        try {
            arq = new RandomAccessFile("Teams.db", "rw");
            idReturn = arq.readInt();
            arq.close();

            return idReturn;
        } catch(IOException e) {
            System.out.println("Erro ao ler o último id a ser inserido!");
            return id;
        }
    }
    public static void main(String[] args) {
        CRUD crud = new CRUD();
        Scanner sc = new Scanner(System.in);
        RandomAccessFile arq;
        
        byte opcao;
        int id = -1;

        id = getLastId(id);

        do {
            menu();

            System.out.print("\nDigite sua opção: ");
            opcao = sc.nextByte();

            if (opcao != 0) {
                if (opcao == 1) {
                    String name;
                    String cnpj;
                    String city;

                    id++;

                    sc.nextLine(); // fazendo a captura do "enter" para não ter problema na leitura dos dados de criação
                    System.out.print("\nname: ");
                    name = sc.nextLine();
                    System.out.print("CNPJ: ");
                    cnpj = sc.nextLine();
                    System.out.print("city: ");
                    city = sc.nextLine();

                    Teams c = new Teams((byte) id, name, cnpj, city);
                    crud.create(c, id);
                } else if (opcao == 2) {
                    String time1;
                    String time2;
                    int golsTime1;
                    int golsTime2;

                    sc.nextLine();

                    System.out.print("\nInsira o name do primeiro time: ");
                    time1 = sc.nextLine();
                    System.out.print("Insira o name do segundo time: ");
                    time2 = sc.nextLine();
                    System.out.print("Insira a quantidade de gols que o primeiro time fez: ");
                    golsTime1 = sc.nextInt();
                    System.out.print("Insira a quantidade de gols que o segundo time fez: ");
                    golsTime2 = sc.nextInt();

                    if (crud.readByName(time1) != null && crud.readByName(time2) != null) { //Checando se os names de time inserido são válidos
                        crud.matchGenerator(time1, time2, golsTime1, golsTime2);
                    } else {
                        System.out.println("\nUm dos times inseridos é inválido!");
                    }
                } else if(opcao == 3) {
                    byte idSearch;

                    System.out.println("\nInsira o ID que deseja pesquisar: ");
                    idSearch = sc.nextByte();
                    
                    crud.readById(idSearch);
                } else if(opcao == 4) {
                    System.out.println("\nInsira o ID do usuário que deseja deletar: ");
                    byte idDel = sc.nextByte();
                    
                    if (crud.delete(idDel) == true) {
                        System.out.println("Time deletado com sucesso!");
                    } else {
                        System.out.println("Time não foi encontrado!");
                    }
                } else if (opcao == 5) {
                    byte idUpd;
                    String nameUpd;
                    String cnpjUpd;
                    String cityUpd;
                    
                    System.out.print("\nInsira o ID do time que deseja alterar: ");
                    idUpd = sc.nextByte();

                    if (crud.readById(idUpd)) {
                        System.out.println("\n***Insira a seguir os novos dados***\n");
                        sc.nextLine();  // Pegar enter
                        System.out.print("Nome: ");
                        nameUpd = sc.nextLine();
                        System.out.print("CNPJ: ");
                        cnpjUpd = sc.nextLine();
                        System.out.print("Cidade: ");
                        cityUpd = sc.nextLine();
                        Teams cUpd = new Teams(idUpd, nameUpd, cnpjUpd, cityUpd);
                        if (crud.update(cUpd)) {
                            System.out.println("Registro atualizado com sucesso!");
                        } else {
                            System.out.println("Não foi possível atualizar o registro!");
                        }
                    } else {
                        System.out.println("\nID inserido inexistente!");
                    }
                } else if (opcao == 6) {
                    crud.readAll();
                }
            }
        } while (opcao != 0);
    }
}
