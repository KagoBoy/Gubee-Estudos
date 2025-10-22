package services;

public class PessoaServiceFactory {
    public static PessoaService criar() {
        ValidadorDeNick validador = new ValidadorDeNickDefault();
        return new PessoaService(validador);
    }
}
