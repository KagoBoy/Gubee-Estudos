import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import model.Livro;
import model.Usuario;
import repository.BibliotecaRepository;
import services.BibliotecaService;

public class App {
    public static void main(String[] args) throws Exception {
        var repository = new BibliotecaRepository();
        var service = new BibliotecaService(repository);
        
        // Records 
        var usuario = new Usuario("U001", "João Silva", "joao@email.com", LocalDate.now());
        
        System.out.println("=== SISTEMA DE BIBLIOTECA MODERNA ===");
        System.out.println("Demonstrando Java 8 ao 21\n");
        
        // Streams + Optional 
        System.out.println("=== BUSCA E DESCRIÇÃO DE ITENS ===");
        repository.buscarPorTitulo("dom").forEach(item -> 
            System.out.println("✓ " + service.descreverItem(item))
        );
        
        System.out.println("\n=== REALIZANDO EMPRÉSTIMOS ===");
        service.realizarEmprestimo("L001", usuario, 14);
        service.realizarEmprestimo("R001", usuario, 7);
        
        System.out.println("\n=== RELATÓRIO ===");
        service.gerarRelatorio();
        
        // Factory Methods 
        System.out.println("\n=== COLECÕES IMUTÁVEIS ===");
        var listaImutavel = List.of("Java", "Records", "Streams");
        var setImutavel = Set.of("Pattern", "Matching", "Sealed");
        System.out.println("Lista imutável: " + listaImutavel);
        System.out.println("Set imutável: " + setImutavel);
        
        // Pattern Matching instanceof 
        System.out.println("\n=== PATTERN MATCHING INSTANCEOF ===");
        Object obj = new Livro("L003", "Teste", "Autor", 2024);
        if (obj instanceof Livro livro) {
            System.out.println("É um livro: " + livro.titulo());
        }
    }
}
