package services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
// import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import entidades.Pessoa;
import entidades_enum.Elo;

public class Cadastro {

    Pessoa p;
    private CopyOnWriteArrayList<Pessoa> pessoas = new CopyOnWriteArrayList<>();
    private DateTimeFormatter frmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate dataFormatada;
    private LocalDate dataAtual = LocalDate.now();
    private int idade;
    
    //Adicionar player
    public void adicionarPlayer(String nome, String dataNasc, String nickName, String role1, String role2, Elo peakElo){
        dataFormatada = LocalDate.parse(dataNasc, frmt);
        idade = dataAtual.getYear() - dataFormatada.getYear();
        p = new Pessoa(nome, dataFormatada, nickName, role1, role2, peakElo, idade);
        pessoas.add(p);
        System.out.println("Player adicionado!");
    }

    //Metodos de remover players
    public void removerPlayerNick(String nickName){
        // Optional<Pessoa> encontrada = pessoas
        // .stream()
        // .filter(pessoa -> pessoa.getNickName()
        // .equalsIgnoreCase(nickName))
        // .findFirst();
        Pessoa encontrada = buscaPlayerNick(nickName);
        if (encontrada != null){
            pessoas.remove(encontrada);
            System.out.println("Player com nick " + nickName + " removido com sucesso!");
        }else {
            System.out.println("Player não foi encontrado!");
        }
        
    }

    public void removerPlayerNome(String nome){
        // Optional<Pessoa> encontrada = pessoas
        // .stream()
        // .filter(pessoa -> pessoa.getNome()
        // .equalsIgnoreCase(nome))
        // .findFirst();
        Pessoa encontrada = buscaPlayerNome(nome);
        if(encontrada != null){
            pessoas.remove(encontrada);
            System.out.println("Player com nome " + nome + " removido com sucesso!");
        }else {
            System.out.println("Player não foi encontrado!");
        }
        
    }


    //Metodos de atualizar players
    public Pessoa atualizaPlayer (int indice, String nome, String dataNasc, String nickName, String role1, String role2, Elo peakElo){
        dataFormatada = LocalDate.parse(dataNasc, frmt);
        idade = dataAtual.getYear() - dataFormatada.getYear();
        p = new Pessoa(nome, dataFormatada, nickName, role1, role2, peakElo, idade);
        pessoas.set(indice, p);
        return p;
    }

    private Pessoa atualizaPlayer (int indice, Pessoa pe){
        pessoas.set(indice, pe);
        return pe;
    }

    public Pessoa atualizaPlayerPorNome (String nome, String newNome, String dataNasc, String nickName, String role1, String role2, Elo peakElo){
        dataFormatada = LocalDate.parse(dataNasc, frmt);
        idade = dataAtual.getYear() - dataFormatada.getYear();
        p = new Pessoa(newNome, dataFormatada, nickName, role1, role2, peakElo, idade);
        Pessoa encontrada = buscaPlayerNome(nome);
        if(encontrada != null){
            int indice = pessoas.indexOf(encontrada);
            atualizaPlayer(indice, p);
            return p;
        }
        return null;
    }

    public Pessoa atualizaPlayerPorNick (String nick, String nome, String dataNasc, String newNick, String role1, String role2, Elo peakElo){
        dataFormatada = LocalDate.parse(dataNasc, frmt);
        idade = dataAtual.getYear() - dataFormatada.getYear();
        p = new Pessoa(nome, dataFormatada, newNick, role1, role2, peakElo, idade);
        Pessoa encontrada = buscaPlayerNick(nick);
        if(encontrada != null){
            int indice = pessoas.indexOf(encontrada);
            atualizaPlayer(indice, p);
            return p;
        }
        return null;
    }




    //Metodos de buscar players
    public Pessoa buscaPlayerNick(String nickname){
        for (Pessoa pessoa : pessoas){
            if(pessoa.getNickName().equalsIgnoreCase(nickname)){
                return pessoa;
            }
        }
        return null;
    }

    public Pessoa buscaPlayerNome(String nome){
        for (Pessoa pessoa : pessoas){
            if(pessoa.getNome().equalsIgnoreCase(nome)){
                return pessoa;
            }
        }
        return null;
    }

    public Pessoa buscaAllPlayers(){
        for (Pessoa pessoa : pessoas){
            System.out.println(pessoa);
        }
        return null;
    }



    
}
