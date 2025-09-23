package entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import entidades_enum.Elo;

public class Pessoa {
    private String nome;
    private LocalDate dataNasc;
    private String nickName;
    private String role1;
    private String role2;
    private Elo peakElo;
    private int idade;
    private DateTimeFormatter frmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    

    

    public Pessoa() {
    }

    
    public Pessoa(String nome, LocalDate dataNasc, String nickName, String role1, String role2, Elo peakElo, int idade) {
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.nickName = nickName;
        this.role1 = role1;
        this.role2 = role2;
        this.peakElo = peakElo;
        this.idade = idade;
    }
    
    public int getIdade() {
        return idade;
    }


    public void setIdade(int idade) {
        this.idade = idade;
    }


    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public LocalDate getDataNasc() {
        return dataNasc;
    }
    public void setDataNasc(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    
    public String getRole1() {
        return role1;
    }


    public void setRole1(String role1) {
        this.role1 = role1;
    }


    public String getRole2() {
        return role2;
    }


    public void setRole2(String role2) {
        this.role2 = role2;
    }


    public Elo getPeakElo() {
        return peakElo;
    }
    public void setPeakElo(Elo peakElo) {
        this.peakElo = peakElo;
    }



    @Override
    public String toString() {
        return "Pessoa [nome=" + nome + ", dataNasc=" + dataNasc.format(frmt) + ", nickName=" + nickName + ", role1=" + role1
                + ", role2=" + role2 + ", peakElo=" + peakElo + ", idade=" + idade + "]";
    }


}
