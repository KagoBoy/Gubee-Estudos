package services;

import java.time.LocalDate;

import entidades.Pessoa;
import entidades_enum.Elo;

public class PessoaService {


    private ValidadorDeNick validador;
    
    public PessoaService(ValidadorDeNick validador) {
        this.validador = validador;
    }
    
    public Pessoa criarPessoa(String nome, LocalDate dataNasc, String nickName, 
                             String role1, String role2, Elo peakElo) {
        if (!validador.validar(nickName)) {
            throw new IllegalArgumentException("Nickname inválido!");
        }
        return new Pessoa(nome, dataNasc, nickName, role1, role2, peakElo);
    }
}
