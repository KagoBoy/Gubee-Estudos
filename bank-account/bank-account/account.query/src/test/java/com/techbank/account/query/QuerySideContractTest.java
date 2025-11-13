package com.techbank.account.query;

import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.dsl.LambdaDsl;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "account-command-side")
public class QuerySideContractTest {

    @Pact(consumer = "account-query-side")
    public V4Pact createPact(PactBuilder builder) {
        return builder
                .usingLegacyMessageDsl() 
                .given("an account was opened")
                .expectsToReceive("AccountOpenedEvent")
                .withMetadata(Map.of("Content-Type", "application/json"))
                .withContent(LambdaDsl.newJsonBody(o -> {
                    o.stringType("accountHolder", "João Silva");
                    o.stringType("accountType", "SAVINGS");
                    o.numberType("openingBalance", 1000.0);
                }).build())
                .toPact();
    }

    @Pact(consumer = "account-query-side")
    public V4Pact fundsDepositedEventPact(PactBuilder builder) {
        return builder
                .usingLegacyMessageDsl()
                .given("funds were deposited")
                .expectsToReceive("FundsDepositedEvent")
                .withMetadata(Map.of("Content-Type", "application/json"))
                .withContent(LambdaDsl.newJsonBody(o -> {
                    o.numberType("amount", 500.0);
                }).build())
                .toPact();
    }

    @Pact(consumer = "account-query-side")
    public V4Pact fundsWithdrawnEventPact(PactBuilder builder) {
        return builder
                .usingLegacyMessageDsl()
                .given("funds were withdrawn")
                .expectsToReceive("FundsWithdrawnEvent")
                .withMetadata(Map.of("Content-Type", "application/json"))
                .withContent(LambdaDsl.newJsonBody(o -> {
                    o.numberType("amount", 200.0);
                }).build())
                .toPact();
    }

    @Pact(consumer = "account-query-side")
    public V4Pact closeAccountEventPact(PactBuilder builder) {
        return builder
                .usingLegacyMessageDsl()
                .given("an account was closed")
                .expectsToReceive("AccountClosedEvent")
                .withMetadata(Map.of("Content-Type", "application/json"))
                .withContent(LambdaDsl.newJsonBody(o -> {
                    o.numberType("version", 0);
                }).build())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    void verifyConsumerPact() {
        System.out.println("Contrato consumer gerado com sucesso!");
    }

    @Test
    @PactTestFor(pactMethod = "fundsDepositedEventPact")
    void verifyDepositConsumerPact() {
        System.out.println("Contrato deposit consumer gerado com sucesso!");
    }

    @Test
    @PactTestFor(pactMethod = "fundsWithdrawnEventPact")
    void verifyWithdrawConsumerPact() {
        System.out.println("Contrato withdraw consumer gerado com sucesso!");
    }

    @Test
    @PactTestFor(pactMethod = "closeAccountEventPact")
    void verifyClosedConsumerPact() {
        System.out.println("Contrato closed account consumer gerado com sucesso!");
    }
}
