package com.example;

public class CadastroUsuario {


    private final ServicoEmail servicoEmail;

    public CadastroUsuario(ServicoEmail servicoEmail) {
        this.servicoEmail = servicoEmail;
    }

    public boolean cadastrar(String nome, String email) {
        if (email == null || !email.contains("@")) {
            return false; 
        }

        servicoEmail.enviar(email, "Bem-vindo " + nome + "!");

        return true;
    }

}
