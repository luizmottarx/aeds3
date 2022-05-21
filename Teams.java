// Author: Luiz Henrique Motta Dias e Athos Geraldo Salomon Dolabela da Silveira

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Teams {
    protected byte id;
    protected String name;
    protected String cnpj;
    protected String city;
    protected byte gamesPlayed;
    protected byte points;

    public Teams
(byte i, String n, String cnpj, String c) {
        this.id = i;
        this.name = n;
        this.cnpj = cnpj;
        this.city = c;
        this.gamesPlayed = 0;
        this.points = 0;
    }
    public Teams
() {
        this.id = -1;
        this.name = "";
        this.cnpj = "";
        this.city = "";
        this.gamesPlayed = 0;
        this.points = 0;
    }
    public byte getId() {
        return id;
    }
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.write(id);
        dos.writeUTF(name);
        dos.writeUTF(cnpj);
        dos.writeUTF(city);
        dos.write(gamesPlayed);
        dos.write(points);
        return baos.toByteArray();
    }
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readByte();
        name = dis.readUTF();
        cnpj = dis.readUTF();
        city = dis.readUTF();
        gamesPlayed = dis.readByte();
        points = dis.readByte();
    }
    //Número de jogos
    public void increaseMatches() {
        this.gamesPlayed++;
    }
    //Número de points
    public void updPoints(int x) {
        this.points += x;
    }
    public String toString() {
        return "\nID: " + id + "\nname Teams
    : " + name + "\nCNPJ: " + cnpj + "\ncity: " + city + "\nPartidas jogadas: " + gamesPlayed + "\npoints acumulados: " + points;
    }
}