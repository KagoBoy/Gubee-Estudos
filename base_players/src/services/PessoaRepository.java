package services;

import java.util.concurrent.CopyOnWriteArrayList;

import entidades.Pessoa;



public class PessoaRepository implements IRepository<Pessoa>{

    private CopyOnWriteArrayList<Pessoa> pessoas = new CopyOnWriteArrayList<>();

    private Pessoa atualizaPlayer(int indice, Pessoa pe) {
        pessoas.set(indice, pe);
        return pe;
    }

    @Override
    public void add(Pessoa p) {
        pessoas.add(p);
        System.out.println("Player adicionado!");
    }

    @Override
    public void removeByNick(String nick) {
        Pessoa encontrada = getByNick(nick);
        if (encontrada != null) {
            pessoas.remove(encontrada);
            System.out.println("Player com nick " + nick + " removido com sucesso!");
        } else {
            System.out.println("Player não foi encontrado!");
        }
    }

    @Override
    public void removeByName(String name) {
        Pessoa encontrada = getByName(name);
        if (encontrada != null) {
            pessoas.remove(encontrada);
            System.out.println("Player com nick " + name + " removido com sucesso!");
        } else {
            System.out.println("Player não foi encontrado!");
        }
    }

    @Override
    public Pessoa getByNick(String nick) {
        return pessoas.stream().filter(p -> p.getNickName().equalsIgnoreCase(nick)).findFirst().orElse(null);
    }

    @Override
    public Pessoa getByName(String name) {
        return pessoas.stream().filter(p -> p.getNome().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public void getAll() {
        for (Pessoa pessoa : pessoas) {
            System.out.println(pessoa);
        }
    }

    @Override
    public Pessoa updateByNick(String nick, Pessoa p) {
        Pessoa encontrada = getByNick(nick);
        if (encontrada != null) {
            int indice = pessoas.indexOf(encontrada);
            atualizaPlayer(indice, p);
            return p;
        }
        return null;
    }

    @Override
    public Pessoa updateByName(String name, Pessoa p) {
        Pessoa encontrada = getByName(name);
        if (encontrada != null) {
            int indice = pessoas.indexOf(encontrada);
            atualizaPlayer(indice, p);
            return p;
        }
        return null;
    }


}
