// Author: Luiz Henrique Motta Dias e Athos Geraldo Salomon Dolabela da Silveira
import java.io.*;

public class CRUD {
    private RandomAccessFile arq;
    private final String nomeArquivo = "Teams.db";

    //Criação da classe e arquivo
    public CRUD() {
        try {
            boolean exists = (new File(nomeArquivo)).exists();

            if (exists) {
                // Arquivo já existe
            } else {
                try {
                    int id = -1;

                    arq = new RandomAccessFile(nomeArquivo, "rw");
                    arq.writeInt(id);
                    arq.close();
                } catch (Exception e) {
                    System.out.println("Erro relacionado ao ID no arquivo!");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro! " + e.getMessage());
        }
    }
    //Criador de times 2.0
    public void create(Teams c, int id) {
        byte[] ba;
        try {
            arq = new RandomAccessFile(nomeArquivo, "rw");

            ba = c.toByteArray();
            arq.seek(0); // Movendo o ponteiro para o início do arquivo para atualizar o valor do ID
            arq.writeInt(id);
            arq.seek(arq.length());
            arq.writeChar(' '); // Escritura da Lápide do registro
            arq.writeInt(ba.length);
            arq.write(ba);
            arq.close();
        } catch (IOException e) {
            System.out.println("Erro ao adicionar time!");
        }
    }
    //Teams a ser substituido = c
    public boolean update(Teams c) {
        try {
            long pos;
            char lapide;
            byte[] b;
            byte[] novoB;
            int tam;
            Teams objeto;

            arq = new RandomAccessFile(nomeArquivo, "rw");

            arq.seek(4);

            while(arq.getFilePointer() < arq.length()) {
                pos = arq.getFilePointer();
                lapide = arq.readChar();
                tam = arq.readInt();
                b = new byte[tam];
                arq.read(b);
                if (lapide != '*') {
                    objeto = new Teams();
                    objeto.fromByteArray(b);
                    if (objeto.getId() == c.getId()) {
                        novoB = c.toByteArray();

                        if (novoB.length <= tam) {
                            arq.seek(pos + 6); //Bloqueador de atualização
                            arq.write(novoB);
                            arq.close();                            
                        } else {
                            arq.seek(arq.length());
                            arq.writeChar(' ');
                            arq.writeInt(novoB.length);
                            arq.write(novoB);
                            delete(objeto.getId());
                            arq.close();
                        }
                        return true;
                    }
                }
            }
            arq.close();
            return false;

        } catch (Exception e) {
            return false;
        }
    }
    //Removedor de cliente 2.0
    public boolean delete(byte id) {
        try {
            long pos;
            char lapide;
            int tam;
            byte[] b;
            Teams Teams;

            arq = new RandomAccessFile(nomeArquivo, "rw");
            arq.seek(4); //Passar do cabeçalho

            while(arq.getFilePointer() < arq.length()) {
                pos = arq.getFilePointer();
                lapide = arq.readChar();
                tam = arq.readInt();
                b = new byte[tam];
                arq.read(b);
                if(lapide != '*') {
                    Teams = new Teams();
                    Teams.fromByteArray(b);
                    if(Teams.getId() == id) {
                        arq.seek(pos);
                        arq.writeChar('*');
                        arq.close();
                        return true;
                    }
                }
            }
            arq.close();
            return false;
        } catch(IOException e) {
            System.out.println("Erro ao deletar time!");
            return false;
        }   
    }
    public boolean readById(byte id) {
        try {
            arq = new RandomAccessFile(nomeArquivo, "rw");

            char lapide;
            byte[] b;
            int tam;
            Teams objeto;

            arq.seek(4);

            while (arq.getFilePointer() < arq.length()) {
                lapide = arq.readChar();
                tam = arq.readInt();
                b = new byte[tam];
                arq.read(b);

                if (lapide != '*') {
                    objeto = new Teams();
                    objeto.fromByteArray(b);

                    if (objeto.getId() == id) {
                        System.out.println(objeto);
                        return true;
                    }
                }
            }
            return false;

        } catch (IOException e) {
            System.out.println("Time não encontrado!");
            return false;
        }
    }
    //Pesquisa de Teamss
    public Teams readByName(String s) {
        try {
            arq = new RandomAccessFile("Teams.db", "rw");

            Teams c;
            byte[] ba;
            int tam;

            arq.seek(4);

            while (arq.getFilePointer() < arq.length()) {
                char lapide = arq.readChar();
                tam = arq.readInt();
                ba = new byte[tam];
                arq.read(ba);

                if (lapide != '*') {
                    c = new Teams();
                    c.fromByteArray(ba);
                    if (c.nome.equals(s)) {
                        return c;
                    }
                }
            }

            return null;
        } catch (IOException e) {
            System.out.println("Time não encontrado!");
            return null;
        }
    }

    //Criação de partida entre dois times de futebol
    public void matchGenerator(String t1, String t2, int golsT1, int golsT2) {
        Teams c1 = readByName(t1);
        Teams c2 = readByName(t2);

        c1.increaseMatches();
        c2.increaseMatches();

        if (golsT1 > golsT2) {
            c1.updPoints(3);
        } else if (golsT1 < golsT2) {
            c2.updPoints(3);
        } else {
            c1.updPoints(1);
            c2.updPoints(1);
        }
        if (update(c1) && update(c2)) {
            System.out.println("\nPartida registrada e dados alterados com sucesso!");
        } else {
            System.out.println("\nNão foi possível registrar a partida e/ou alterar os dados!");
        }
    }
    //Todos os times listados
    public void readAll() {
        try {
            arq = new RandomAccessFile(nomeArquivo, "rw");
            arq.seek(4);

            char lapide;
            byte[] b;
            int tam;
            Teams objeto;

            while (arq.getFilePointer() < arq.length()) {
                lapide = arq.readChar();
                tam = arq.readInt();
                b = new byte[tam];
                arq.read(b);
                if (lapide != '*') {
                    objeto = new Teams();
                    objeto.fromByteArray(b);
                    System.out.println(objeto.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro, times não apresentados!");
        }
    }
}




