import entidades_enum.Elo;
import entidades.Pessoa;

public class App {
    public static void main(String[] args) throws Exception {
        Pessoa pessoa = new Pessoa();

        try {
            pessoa.adicionarPlayer("Yan Victor", "10/02/2003", "dr Yam #drs", "Atirador", "Suporte", Elo.MESTRE);
            pessoa.adicionarPlayer("Leonardo", "20/06/2003", "perspectiva #leo", "Suporte", "Atirador", Elo.MESTRE);
            pessoa.adicionarPlayer("Roberto Neto", "10/12/2002", "Minazuki #br3", "Mid", "Atirador", Elo.MESTRE);
            pessoa.adicionarPlayer("Endy Alexandre", "25/08/2001", "Dyne #br1", "Jungle", "Top", Elo.MESTRE);
            pessoa.adicionarPlayer("Lucas Tarzan", "15/07/2003", "Tarzan Sergipano #br1", "Top", "Jungle", Elo.CHALLENGER);
            pessoa.buscaAllPlayers();

            pessoa.atualizaPlayer(0, "Yan Victor", "10/02/2003", "dr Yam #drs", "Atirador", "Suporte", Elo.CHALLENGER);
            pessoa.atualizaPlayerPorNick("Dyne #br1", "Endy Alexandre", "25/08/2001", "Dyne #br1", "Jungle", "Top", Elo.FERRO);
            pessoa.atualizaPlayerPorNome("Lucas Tarzan", "Lucas Tarzan", "15/07/2003", "Tarzan Sergipano #br1", "Top", "Jungle", Elo.BRONZE);

            System.out.println(pessoa.buscaPlayerNick("dr yam #DRS"));
            System.out.println(pessoa.buscaPlayerNome("Endy alexandre"));
            pessoa.removerPlayerNick("Minazuki #BR3");
            pessoa.removerPlayerNome("Leonardo");

            System.out.println(pessoa.buscaPlayerNick("dr yam #drs").toString_2());

            pessoa.adicionarPlayer("Lucas Tarzan", "15/07/2003", "Tarzan Sergipano br1", "Top", "Jungle", Elo.CHALLENGER);


        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        } finally {
            pessoa.buscaAllPlayers();
        }


        
    }
}
