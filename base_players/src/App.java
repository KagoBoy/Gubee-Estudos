import entidades_enum.Elo;
import services.Cadastro;

public class App {
    public static void main(String[] args) throws Exception {
        Cadastro c = new Cadastro();
        c.adicionarPlayer("Yan Victor", "10/02/2003", "dr Yam #drs", "Atirador", "Suporte", Elo.MESTRE);
        c.adicionarPlayer("Leonardo", "20/06/2003", "perspectiva #leo", "Suporte", "Atirador", Elo.MESTRE);
        c.adicionarPlayer("Roberto Neto", "10/12/2002", "Minazuki #br3", "Mid", "Atirador", Elo.MESTRE);
        c.adicionarPlayer("Endy Alexandre", "25/08/2001", "Dyne #br1", "Jungle", "Top", Elo.MESTRE);
        c.adicionarPlayer("Lucas Tarzan", "15/07/2003", "Tarzan Sergipano #br1", "Top", "Jungle", Elo.CHALLENGER);
        c.buscaAllPlayers();

        c.atualizaPlayer(0, "Yan Victor", "10/02/2003", "dr Yam #drs", "Atirador", "Suporte", Elo.CHALLENGER);
        c.atualizaPlayerPorNick("Dyne #br1", "Endy Alexandre", "25/08/2001", "Dyne #br1", "Jungle", "Top", Elo.FERRO);
        c.atualizaPlayerPorNome("Lucas Tarzan", "Lucas Tarzan", "15/07/2003", "Tarzan Sergipano #br1", "Top", "Jungle", Elo.BRONZE);

        System.out.println(c.buscaPlayerNick("dr yam #DRS"));
        System.out.println(c.buscaPlayerNome("Endy alexandre"));
        c.removerPlayerNick("Minazuki #BR3");
        c.removerPlayerNome("Leonardo");

        c.buscaAllPlayers();

        System.out.println(c.buscaPlayerNick("dr yam #drs").toString_2());

        
    }
}
