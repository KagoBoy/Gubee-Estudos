package model;
import java.time.LocalDate;

public record Usuario(String id, String nome, String email,
LocalDate dataCadastro) {

}
