package repository;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Emprestimo;
import model.ItemBiblioteca;
import model.Livro;
import model.Revista;

public class BibliotecaRepository {


    private final List<ItemBiblioteca> itens = new ArrayList<>();
    private final List<Emprestimo> emprestimos = new ArrayList<>();

    public BibliotecaRepository() {
        inicializarDados();
    }

    private void inicializarDados() {
        itens.add(new Livro("L001", "Dom Casmurro", "Machado de Assis", 1899));
        itens.add(new Livro("L002", "1984", "George Orwell", 1949));
        itens.add(new Revista("R001", "Revista 01", 2874, "Autor 2", LocalDate.of(2025, 10, 2)));
        itens.add(new Revista("E001", "Clean Code", 2875, "Autor 3", LocalDate.of(2025, 5, 10)));
    }

    

    public Optional<ItemBiblioteca> buscarPorId(String id){
        return itens.stream()
                .filter(item -> item.id().equals(id))
                .findFirst();
    }

    public List<ItemBiblioteca> buscarPorTitulo(String titulo) {
        return List.copyOf(itens.stream()
            .filter(item -> item.titulo().toLowerCase().contains(titulo.toLowerCase()))
            .toList());
    }

    public void adicionarItem(ItemBiblioteca item) {
        itens.add(item);
    }
    
    public void adicionarEmprestimo(Emprestimo emprestimo) {
        emprestimos.add(emprestimo);
    }
    
    public List<Emprestimo> getEmprestimosAtivos() {
        return emprestimos.stream()
            .filter(emp -> emp.dataDevolucao().isAfter(LocalDate.now()))
            .toList();
    }
}
