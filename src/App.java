import metodos.Cadastro;

public class App {
    public static void main(String[] args) throws Exception {
        Cadastro c = new Cadastro();
        c.adicionarPlayer("Yan Victor", "10/02/2003", "dr Yam #drs", "Atirador", "Suporte", "Mestre");
        c.adicionarPlayer("Leonardo", "20/06/2003", "perspectiva #leo", "Suporte", "Atirador", "Mestre");
        c.adicionarPlayer("Roberto Neto", "10/12/2002", "Minazuki #br3", "Mid", "Atirador", "Mestre");
        c.adicionarPlayer("Endy Alexandre", "25/08/2001", "Dyne #br1", "Jungle", "Top", "Mestre");
        c.adicionarPlayer("Lucas Tarzan", "15/07/2003", "Tarzan Sergipano #br1", "Top", "Jungle", "Challenger");
        c.buscaAllPlayers();

        c.atualizaPlayer(0, "Yan Victor", "10/02/2003", "dr Yam #drs", "Atirador", "Suporte", "Challenger");
        c.atualizaPlayerPorNick("Dyne #br1", "Endy Alexandre", "25/08/2001", "Dyne #br1", "Jungle", "Top", "Ferro");
        c.atualizaPlayerPorNome("Lucas Tarzan", "Lucas Tarzan", "15/07/2003", "Tarzan Sergipano #br1", "Top", "Jungle", "Prata");

        System.out.println(c.buscaPlayerNick("dr yam #DRS"));
        System.out.println(c.buscaPlayerNome("Endy alexandre"));
        c.removerPlayerNick("Minazuki #BR3");
        c.removerPlayerNome("Leonardo");

        c.buscaAllPlayers();

        
    }
}
