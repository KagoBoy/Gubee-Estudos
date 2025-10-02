package model;
public record Livro(String isbn, String titulo, String autorLivro,
int anoPublicacao) implements ItemBiblioteca {

    @Override
    public String id() {
        return isbn;
    }

    @Override
    public String autor() {
        return autorLivro;
    }



}
