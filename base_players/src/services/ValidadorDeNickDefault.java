package services;

public class ValidadorDeNickDefault implements ValidadorDeNick{
    @Override
    public boolean validar(String nickname) {
        return nickname != null && nickname.contains("#");
    }
}
