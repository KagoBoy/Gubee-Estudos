package entidades;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import entidades_enum.Elo;
import services.ValidadorDeNick;
import services.ValidadorDeNickDefault;

public class Pessoa {
    private String nome;
    private LocalDate dataNasc;
    @NickName
    private String nickName;
    private String role1;
    private String role2;
    private Elo peakElo;
    private int idade;
    private DateTimeFormatter frmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private ValidadorDeNick validador = new ValidadorDeNickDefault();

    
    public Pessoa() {
    }

    public Pessoa(String nome, LocalDate dataNasc, String nickName, String role1, String role2, Elo peakElo) {
        this.nome = nome;
        this.dataNasc = dataNasc;
        if (validarNick(nickName)) {
            this.nickName = nickName;
        } else {
            throw new IllegalArgumentException("Nickname " + nickName + " inválido!");
        }

        this.role1 = role1;
        this.role2 = role2;
        this.peakElo = peakElo;
        this.idade = Period.between(dataNasc, LocalDate.now()).getYears();
    }

    public String getNome() {
        return nome;
    }

    public String getNickName() {
        return nickName;
    }


    @Override
    public String toString() {
        return "Pessoa [nome=" + nome + ", dataNasc=" + dataNasc.format(frmt) + ", nickName=" + nickName + ", role1="
                + role1
                + ", role2=" + role2 + ", peakElo=" + peakElo + ", idade=" + idade + "]";
    }

    public String toString_2() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: " + nome + "\n");
        sb.append("Nick: " + nickName + "\n");
        sb.append("PeakElo: " + peakElo + "\n");
        sb.append("Idade: " + idade);
        return sb.toString();
    }

    public boolean validarNick(String nickName) {
        return validador.validar(nickName);
    }
   
}
