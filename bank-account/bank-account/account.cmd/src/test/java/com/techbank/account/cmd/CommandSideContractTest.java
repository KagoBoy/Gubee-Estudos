package com.techbank.account.cmd;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techbank.account.common.dto.AccountType;
import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import producers.EventProducer;

@Provider("account-command-side")
@PactFolder("${user.dir}/../account.query/target/pacts")
@SpringBootTest
public class CommandSideContractTest {

    @Mock
    private EventProducer eventProducer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void verifyPact(PactVerificationContext context) {
        context.verifyInteraction();
    }


    // -------- OPEN ACCOUNT --------
    @State("an account was opened")
    public void toAccountOpenedState() {
        AccountOpenedEvent event = new AccountOpenedEvent(
                "João Silva",
                AccountType.SAVINGS,
                new Date(),
                1000.0);
        eventProducer.produce("BankAccountEvents", event);
        verify(eventProducer, atLeastOnce())
                .produce(eq("BankAccountEvents"), any(AccountOpenedEvent.class));
    }

    @PactVerifyProvider("AccountOpenedEvent")
    public String verifyAccountOpenedEventMessage() throws Exception {
        AccountOpenedEvent event = new AccountOpenedEvent(
                "João Silva",
                AccountType.SAVINGS,
                new Date(),
                1000.0);

        return objectMapper.writeValueAsString(event);
    }

    // -------- DEPOSIT FUNDS --------
    @State("funds were deposited")
    public void toFundsDepositedState() {
        FundsDepositedEvent event = new FundsDepositedEvent(500.0);
        eventProducer.produce("BankAccountEvents", event);
        verify(eventProducer).produce(eq("BankAccountEvents"), any(FundsDepositedEvent.class));
    }

    @PactVerifyProvider("FundsDepositedEvent")
    public String verifyFundsDepositedEventMessage() throws Exception {
        FundsDepositedEvent event = new FundsDepositedEvent(500.0);
        return objectMapper.writeValueAsString(event);
    }

    // -------- WITHDRAW FUNDS --------
    @State("funds were withdrawn")
    public void toFundsWithdrawnState() {
        FundsWithdrawnEvent event = new FundsWithdrawnEvent(200.0);
        eventProducer.produce("BankAccountEvents", event);
        verify(eventProducer).produce(eq("BankAccountEvents"), any(FundsWithdrawnEvent.class));
    }

    @PactVerifyProvider("FundsWithdrawnEvent")
    public String verifyFundsWithdrawnEventMessage() throws Exception {
        FundsWithdrawnEvent event = new FundsWithdrawnEvent(200.0);
        return objectMapper.writeValueAsString(event);
    }

    // -------- CLOSE ACCOUNT --------
    @State("an account was closed")
    public void toAccountClosedState() {
        AccountClosedEvent event = new AccountClosedEvent();
        eventProducer.produce("BankAccountEvents", event);
        verify(eventProducer).produce(eq("BankAccountEvents"),
                any(AccountClosedEvent.class));
    }

    @PactVerifyProvider("AccountClosedEvent")
    public String verifyAccountClosedEventMessage() throws Exception {
        AccountClosedEvent event = new AccountClosedEvent();
        return objectMapper.writeValueAsString(event);
    }
}
