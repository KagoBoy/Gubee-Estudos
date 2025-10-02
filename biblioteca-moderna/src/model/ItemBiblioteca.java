package model;
public sealed interface ItemBiblioteca permits Livro, Revista {
    String id();
    String titulo();
    String autor();
}
