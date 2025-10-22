package entidades;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArrayList;

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
    Pessoa p;
    private CopyOnWriteArrayList<Pessoa> pessoas = new CopyOnWriteArrayList<>();
    private LocalDate dataFormatada;
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


    // Adicionar player
    public void adicionarPlayer(String nome, String dataNasc, String nickName, String role1, String role2,
                                Elo peakElo) {
        dataFormatada = LocalDate.parse(dataNasc, frmt);
        Pessoa p = new Pessoa(nome, dataFormatada, nickName, role1, role2, peakElo);
        pessoas.add(p);
        System.out.println("Player adicionado!");

    }

    // Metodos de remover players
    public void removerPlayerNick(String nickName) {
        Pessoa encontrada = buscaPlayerNick(nickName);
        if (encontrada != null) {
            pessoas.remove(encontrada);
            System.out.println("Player com nick " + nickName + " removido com sucesso!");
        } else {
            System.out.println("Player não foi encontrado!");
        }

    }

    public void removerPlayerNome(String nome) {
        Pessoa encontrada = buscaPlayerNome(nome);
        if (encontrada != null) {
            pessoas.remove(encontrada);
            System.out.println("Player com nome " + nome + " removido com sucesso!");
        } else {
            System.out.println("Player não foi encontrado!");
        }

    }

    // Metodos de atualizar players
    public Pessoa atualizaPlayer(int indice, String nome, String dataNasc, String nickName, String role1, String role2,
                                 Elo peakElo) {
        dataFormatada = LocalDate.parse(dataNasc, frmt);
        p = new Pessoa(nome, dataFormatada, nickName, role1, role2, peakElo);
        pessoas.set(indice, p);
        return p;
    }

    private Pessoa atualizaPlayer(int indice, Pessoa pe) {
        pessoas.set(indice, pe);
        return pe;
    }

    public Pessoa atualizaPlayerPorNome(String nome, String newNome, String dataNasc, String nickName, String role1,
                                        String role2, Elo peakElo) {
        dataFormatada = LocalDate.parse(dataNasc, frmt);
        p = new Pessoa(newNome, dataFormatada, nickName, role1, role2, peakElo);
        Pessoa encontrada = buscaPlayerNome(nome);
        if (encontrada != null) {
            int indice = pessoas.indexOf(encontrada);
            atualizaPlayer(indice, p);
            return p;
        }
        return null;
    }

    public Pessoa atualizaPlayerPorNick(String nick, String nome, String dataNasc, String newNick, String role1,
                                        String role2, Elo peakElo) {
        dataFormatada = LocalDate.parse(dataNasc, frmt);
        p = new Pessoa(nome, dataFormatada, newNick, role1, role2, peakElo);
        Pessoa encontrada = buscaPlayerNick(nick);
        if (encontrada != null) {
            int indice = pessoas.indexOf(encontrada);
            atualizaPlayer(indice, p);
            return p;
        }
        return null;
    }

    // Metodos de buscar players
    public Pessoa buscaPlayerNick(String nickname) {
        for (Pessoa pessoa : pessoas) {
            if (pessoa.getNickName().equalsIgnoreCase(nickname)) {
                return pessoa;
            }
        }
        return null;
    }

    public Pessoa buscaPlayerNome(String nome) {
        for (Pessoa pessoa : pessoas) {
            if (pessoa.getNome().equalsIgnoreCase(nome)) {
                return pessoa;
            }
        }
        return null;
    }

    public Pessoa buscaAllPlayers() {
        for (Pessoa pessoa : pessoas) {
            System.out.println(pessoa);
        }
        return null;
    }

}
