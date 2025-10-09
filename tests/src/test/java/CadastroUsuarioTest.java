import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.CadastroUsuario;
import com.example.ServicoEmail;

public class CadastroUsuarioTest {

    @Test
    public void deveEnviarMensagem() {
        class ServicoEmailStub implements ServicoEmail {

            String destinatario;
            String mensagem;

            @Override
            public void enviar(String destinatario, String mensagem) {
                this.destinatario = destinatario;
                this.mensagem = mensagem;
            }

        }

        ServicoEmailStub stubEmail = new ServicoEmailStub();
        CadastroUsuario cadastrador = new CadastroUsuario(stubEmail);


        cadastrador.cadastrar("Yan", "yan@gmail.com");

        assertEquals("yan@gmail.com", stubEmail.destinatario);
        assertEquals("Bem-vindo Yan!", stubEmail.mensagem);
    }


    

}
