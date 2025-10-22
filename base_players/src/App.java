import entidades_enum.Elo;
import services.IRepository;
import services.PessoaRepository;
import services.PessoaService;
import services.PessoaServiceFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import entidades.Pessoa;

public class App {
    public static void main(String[] args) throws Exception {
        IRepository<Pessoa> repoPessoas = new PessoaRepository();
        DateTimeFormatter frmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        PessoaService pessoaService = PessoaServiceFactory.criar();

        try {
            repoPessoas.add(pessoaService.criarPessoa("Yan Victor", LocalDate.parse("10/02/2003", frmt), "dr Yam #drs", "Atirador", "Suporte", Elo.MESTRE));
            repoPessoas.add(pessoaService.criarPessoa("Leonardo", LocalDate.parse("20/06/2003", frmt), "perspectiva #leo", "Suporte", "Atirador", Elo.MESTRE));
            repoPessoas.add(pessoaService.criarPessoa("Roberto Neto", LocalDate.parse("10/12/2002", frmt), "Minazuki #br3", "Mid", "Atirador", Elo.MESTRE));
            repoPessoas.add(pessoaService.criarPessoa("Endy Alexandre", LocalDate.parse("25/08/2001", frmt), "Dyne #br1", "Jungle", "Top", Elo.MESTRE));
            repoPessoas.add(pessoaService.criarPessoa("Lucas Tarzan", LocalDate.parse("15/07/2003", frmt), "Tarzan Sergipano #br1", "Top", "Jungle", Elo.CHALLENGER));
            repoPessoas.getAll();

            repoPessoas.updateByNick("Dyne #br1", pessoaService.criarPessoa("Endy Alexandre", LocalDate.parse("25/08/2001", frmt), "Dyne #br1", "Jungle", "Top", Elo.FERRO));
            repoPessoas.updateByName("Lucas Tarzan", pessoaService.criarPessoa("Lucas Tarzan", LocalDate.parse("15/07/2003", frmt), "Tarzan Sergipano #br1", "Top", "Jungle", Elo.BRONZE));

            System.out.println(repoPessoas.getByNick("dr yam #DRS"));
            System.out.println(repoPessoas.getByName("Endy alexandre"));
            repoPessoas.removeByNick("Minazuki #BR3");
            repoPessoas.removeByName("Leonardo");

            System.out.println(repoPessoas.getByNick("dr yam #drs").toString_2());

            repoPessoas.add(pessoaService.criarPessoa("Lucas Tarzan", LocalDate.parse("15/07/2003", frmt), "Tarzan Sergipano br1", "Top", "Jungle", Elo.CHALLENGER));


        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        } finally {
            repoPessoas.getAll();
        }


        
    }
}
