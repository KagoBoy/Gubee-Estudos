package model;
import java.time.LocalDate;

public record Emprestimo(String id, Usuario usuario, ItemBiblioteca item,
LocalDate dataEmprestimo, LocalDate dataDevolucao) {

}
