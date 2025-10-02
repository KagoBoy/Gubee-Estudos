package services;
import java.time.LocalDate;

import model.Emprestimo;
import model.ItemBiblioteca;
import model.Livro;
import model.Revista;
import model.Usuario;
import repository.BibliotecaRepository;

public class BibliotecaService {
    private final BibliotecaRepository repository;

    public BibliotecaService(BibliotecaRepository repository){
        this.repository = repository;
    }

    public String descreverItem(ItemBiblioteca item) {
        return switch (item) {
            case Livro livro -> 
                "Livro: '" + livro.titulo() + "' por " + livro.autor() + 
                " (" + livro.anoPublicacao() + ") - ISBN: " + livro.isbn();
            case Revista revista -> 
                "Revista: '" + revista.titulo() + "' - Ed. " + revista.numero();
        };
    }
    
    public void processarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo instanceof Emprestimo emp)
            System.out.printf("""
            ===== COMPROVANTE DE EMPRÉSTIMO =====
            ID: %s
            Usuário: %s (%s)
            Item: %s
            Data do Empréstimo: %s
            Data de Devolução: %s
            =====================================
            %n""", 
            emp.id(), 
            emp.usuario().nome(), 
            emp.usuario().email(),
            descreverItem(emp.item()),
            emp.dataEmprestimo(),
            emp.dataDevolucao()
        );
    }

    public void realizarEmprestimo(String itemId, Usuario usuario, int diasEmprestimo) {
        var itemOptional = repository.buscarPorId(itemId);
        
        itemOptional.ifPresentOrElse(
            item -> {
                var emprestimo = new Emprestimo(
                    "EMP" + System.currentTimeMillis(),
                    usuario,
                    item,
                    LocalDate.now(),
                    LocalDate.now().plusDays(diasEmprestimo)
                );
                
                repository.adicionarEmprestimo(emprestimo);
                processarEmprestimo(emprestimo);
            },
            () -> System.out.println("Item não encontrado: " + itemId)
        );
    }

    public void gerarRelatorio() {
        var emprestimosAtivos = repository.getEmprestimosAtivos();
        
        System.out.println("===== RELATÓRIO DE EMPRÉSTIMOS ATIVOS =====");
        
        if (emprestimosAtivos.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo no momento.");
            return;
        }
        
        emprestimosAtivos.forEach(emp -> {
            var diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(
                LocalDate.now(), emp.dataDevolucao()
            );
            
            String linhaRelatorio = String.format(
                "Usuário: %s | Item: %s | Dias restantes: %d",
                emp.usuario().nome(),
                emp.item().titulo(),
                diasRestantes
            );
            
            System.out.println(linhaRelatorio);
        });
        
        System.out.println("Total de empréstimos ativos: " + emprestimosAtivos.size());
    }
}
