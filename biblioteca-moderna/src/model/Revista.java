package model;
import java.time.LocalDate;

public record Revista(String issn, String titulo, int numero, String autorRevista,
LocalDate dataPublicacao) implements ItemBiblioteca {

    @Override
    public String id() {
        return issn;
    }

    @Override
    public String autor() {
        return autorRevista;
    }

    

}
