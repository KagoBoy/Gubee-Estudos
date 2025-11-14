package reactive.test;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
public class OperatorsTest {

    @BeforeAll
    public static void setUp(){
        BlockHound.install();
    }


    @Test
    public void subscribeOnSimple() {
        Flux<Integer> flux = Flux.range(1, 4)
                .map(i -> {
                    log.info("Map 1 - Number {} on Thread {}", i, Thread.currentThread());
                    return i;
                })
                .subscribeOn(Schedulers.single()) // apenas uma thread adicional alem da main
                .map(i -> {
                    log.info("Map 2 - Number {} on Thread {}", i, Thread.currentThread());
                    return i;
                });

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void publishOnSimple() {
        Flux<Integer> flux = Flux.range(1, 4)
                .map(i -> {
                    log.info("Map 1 - Number {} on Thread {}", i, Thread.currentThread());
                    return i;
                })
                .publishOn(Schedulers.boundedElastic()) //mais de uma thread adicional além da main
                .map(i -> {
                    log.info("Map 2 - Number {} on Thread {}", i, Thread.currentThread());
                    return i;
                });

        flux.subscribe();
        flux.subscribe();

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void multipleSubscribeOnSimple() {
        Flux<Integer> flux = Flux.range(1, 4)
                //quando tem dois subscribeOn: o primeiro subscribe é usado como padrão
                //dessa forma nesse código é usado o single ao invés do boundedElastic
                //mesmo dando um subscribeOn= boundedElastic depois
                .subscribeOn(Schedulers.single())
                .map(i -> {
                    log.info("Map 1 - Number {} on Thread {}", i, Thread.currentThread());
                    return i;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(i -> {
                    log.info("Map 2 - Number {} on Thread {}", i, Thread.currentThread());
                    return i;
                });


        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void multiplePublishOnSimple() {
        Flux<Integer> flux = Flux.range(1, 4)
                // no publishOn se colocar dois Schedulers diferente
                // cada Map pega o Scheduler setado
                // nesse caso o map1 será boundedElastica e o map2 single
                .publishOn(Schedulers.boundedElastic())
                .map(i -> {
                    log.info("Map 1 - Number {} on Thread {}", i, Thread.currentThread());
                    return i;
                })
                .publishOn(Schedulers.single())
                .map(i -> {
                    log.info("Map 2 - Number {} on Thread {}", i, Thread.currentThread());
                    return i;
                });

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void publishAndSubscribeOnSimple() {
        Flux<Integer> flux = Flux.range(1, 4)
                //quando usamos publishOn e subscribeOn, tendo o publishOn setado primeiro
                // apenas o publishOn é usado como padrão
                //logo nesse caso todos os maps serão single
                .publishOn(Schedulers.single())
                .map(i -> {
                    log.info("Map 1 - Number {} on Thread {}", i, Thread.currentThread());
                    return i;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(i -> {
                    log.info("Map 2 - Number {} on Thread {}", i, Thread.currentThread());
                    return i;
                });

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void subscribeAndPublishOnSimple() {
        Flux<Integer> flux = Flux.range(1, 4)
                //quando usamos publishOn e subscribeOn, porém o subscribeOn é setado primeiro
                // é usado o subscribeOn até que o publishOn seja setado, e o resto será usado com o publish
                //logo nesse caso map1 vai com o subscribeOn e map2 com publishOn
                .subscribeOn(Schedulers.single())
                .map(i -> {
                    log.info("Map 1 - Number {} on Thread {}", i, Thread.currentThread());
                    return i;
                })
                .publishOn(Schedulers.boundedElastic())
                .map(i -> {
                    log.info("Map 2 - Number {} on Thread {}", i, Thread.currentThread());
                    return i;
                });

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void subscribeOnIO() {
        Mono<List<String>> list = Mono.fromCallable(() -> Files.readAllLines(Path.of("text-file")))
                .log()
                .subscribeOn(Schedulers.boundedElastic());

        // list.subscribe(s -> log.info("{}", s));
        // Thread.sleep(2000);

        StepVerifier.create(list)
                        .expectSubscription()
                        .thenConsumeWhile(l -> {
                            Assertions.assertFalse(l.isEmpty());
                            log.info("Size {}", l.size());
                            return true;
                        })
                        .verifyComplete();
    }

    @Test
    public void switchIfEmptyOperator(){
        Flux<Object> flux = emptyFlux()
                .switchIfEmpty(Flux.just("not empty anymore"))
                .log();

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext("not empty anymore")
                .expectComplete()
                .verify();
    }
    private Flux<Object> emptyFlux() {
        return Flux.empty();
    }

    @Test
    public void deferOperator() throws Exception{
        //se passar com o just.subscribe, todos terão output de time igual,
        //pois o just mantem o time na memoria quando uma instancia é criada
        Mono<Long> just = Mono.just(System.currentTimeMillis());

        // ja com o defer uma nova instancia é criada a cada .subscribe
        // o que dá um output de time diferente para cada subscribe
        Mono<Long> defer = Mono.defer(() -> Mono.just(System.currentTimeMillis()));

        defer.subscribe(l -> log.info("time {}", l));
        Thread.sleep(100);
        defer.subscribe(l -> log.info("time {}", l));
        Thread.sleep(100);
        defer.subscribe(l -> log.info("time {}", l));
        Thread.sleep(100);
        defer.subscribe(l -> log.info("time {}", l));

        AtomicLong atomicLong = new AtomicLong();
        defer.subscribe(atomicLong::set);
        Assertions.assertTrue(atomicLong.get() > 0);
    }

    @Test
    public void concatOperator() {
        Flux<String> flux1 = Flux.just("a", "b");
        Flux<String> flux2 = Flux.just("c", "d");

        Flux<String> concatFlux = Flux.concat(flux1, flux2).log();

        StepVerifier.create(concatFlux)
                .expectSubscription()
                .expectNext("a", "b", "c", "d")
                .expectComplete()
                .verify();
    }

    @Test
    public void concatWithOperator() {
        Flux<String> flux1 = Flux.just("a", "b");
        Flux<String> flux2 = Flux.just("c", "d");

        Flux<String> concatFlux = flux1.concatWith(flux2).log();

        StepVerifier.create(concatFlux)
                .expectSubscription()
                .expectNext("a", "b", "c", "d")
                .expectComplete()
                .verify();
    }

    @Test
    public void combineLatestOperator() {
        Flux<String> flux1 = Flux.just("a", "b");
        Flux<String> flux2 = Flux.just("c", "d");

        Flux<String> concatFlux = Flux.combineLatest(flux1, flux2,
                (s1, s2) -> s1.toUpperCase() + s2.toUpperCase())
                .log();


        StepVerifier.create(concatFlux)
                .expectSubscription()
                .expectNext("BC", "BD")
                .expectComplete()
                .verify();
    }

    @Test
    public void mergeOperator() throws InterruptedException {
        Flux<String> flux1 = Flux.just("a", "b").delayElements(Duration.ofMillis(200));
        Flux<String> flux2 = Flux.just("c", "d");
        Flux<String> mergeFlux = Flux.merge(flux1, flux2)
                .delayElements(Duration.ofMillis(200))
                .log();

        //mergeFlux.subscribe(log::info);

        //Thread.sleep(1000);

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNext("c", "d", "a", "b")
                .expectComplete()
                .verify();
    }

    @Test
    public void mergeWithOperator() throws InterruptedException {
        Flux<String> flux1 = Flux.just("a", "b").delayElements(Duration.ofMillis(200));
        Flux<String> flux2 = Flux.just("c", "d");
        Flux<String> mergeFlux = flux1.mergeWith(flux2)
                .delayElements(Duration.ofMillis(200))
                .log();


        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNext("c", "d", "a", "b")
                .expectComplete()
                .verify();
    }

    @Test
    public void mergeSequentialOperator() throws InterruptedException {
        Flux<String> flux1 = Flux.just("a", "b").delayElements(Duration.ofMillis(200));
        Flux<String> flux2 = Flux.just("c", "d");

        //mantem a sequencia e pode fazer o merge em mais de 2 flux
        Flux<String> mergeFlux = Flux.mergeSequential(flux1, flux2, flux1)
                .delayElements(Duration.ofMillis(200))
                .log();


        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNext("a", "b", "c", "d", "a", "b")
                .expectComplete()
                .verify();
    }

    @Test
    public void concatOperatorError() {
        Flux<String> flux1 = Flux.just("a", "b")
                .map(s -> {
                    if (s.equals("b")) throw new IllegalArgumentException();
                    return s;
                });
        Flux<String> flux2 = Flux.just("c", "d");

        Flux<String> concatFlux = Flux.concatDelayError(flux1, flux2).log();

        StepVerifier.create(concatFlux)
                .expectSubscription()
                .expectNext("a", "c", "d")
                .expectError()
                .verify();
    }

    @Test
    public void mergeDelayErrorOperator() throws InterruptedException {
        Flux<String> flux1 = Flux.just("a", "b")
                .map(s -> {
                    if (s.equals("b")) throw new IllegalArgumentException();
                    return s;
                }).doOnError(t -> log.error("We could do something with this"));
        Flux<String> flux2 = Flux.just("c", "d");

        Flux<String> mergeFlux = Flux.mergeDelayError(1, flux1, flux2, flux1)
                .log();

        mergeFlux.subscribe(log::info);


        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNext("a", "c", "d", "a")
                .expectError()
                .verify();

    }

    @Test
    public void flatMapOperator() throws Exception {
        Flux<String> flux = Flux.just("a", "b");
        Flux<String> flatFlux = flux.map(String::toUpperCase)
                .flatMap(this::findByName)
                .log();

        flatFlux.subscribe(log::info);


        StepVerifier.create(flatFlux)
                .expectSubscription().expectNext("nameB1", "nameB2", "nameA1", "nameA2")
                .verifyComplete();
    }

    @Test
    public void flatMapSequentialOperator() throws Exception {
        Flux<String> flux = Flux.just("a", "b");
        Flux<String> flatFlux = flux.map(String::toUpperCase)
                .flatMapSequential(this::findByName)
                .log();

        flatFlux.subscribe(log::info);


        StepVerifier.create(flatFlux)
                .expectSubscription().expectNext("nameA1", "nameA2", "nameB1", "nameB2")
                .verifyComplete();
    }


    @Test
    public void zipOperator(){
        Flux<String> titleFlux = Flux.just("Grand Blue", "Baki");
        Flux<String> studioFlux = Flux.just("Zero-G", "TMS Entertainment");
        Flux<Integer> episodesFlux = Flux.just(12, 24);

        Flux<Anime> animeFlux = Flux.zip(titleFlux, studioFlux, episodesFlux)
                .flatMap(tuple -> Flux.just(new Anime(tuple.getT1(), tuple.getT2(), tuple.getT3())));

        animeFlux.subscribe(anime -> log.info(anime.toString()));

        StepVerifier.create(animeFlux)
                .expectSubscription()
                .expectNext(
                        new Anime("Grand Blue", "Zero-G", 12),
                        new Anime("Baki", "TMS Entertainment", 24))
                .verifyComplete();
    }
    @Test
    public void zipWithOperator(){
        Flux<String> titleFlux = Flux.just("Grand Blue", "Baki");
        Flux<Integer> episodesFlux = Flux.just(12, 24);

        Flux<Anime> animeFlux = titleFlux.zipWith(episodesFlux)
                .flatMap(tuple -> Flux.just(new Anime(tuple.getT1(), null, tuple.getT2())));

        animeFlux.subscribe(anime -> log.info(anime.toString()));

        StepVerifier.create(animeFlux)
                .expectSubscription()
                .expectNext(
                        new Anime("Grand Blue", null, 12),
                        new Anime("Baki", null, 24))
                .verifyComplete();
    }

    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    class Anime {
        private String title;
        private String studio;
        private int episodes;

    }

    public Flux<String> findByName(String name){
        return name.equals("A") ? Flux.just("nameA1", "nameA2").delayElements(Duration.ofMillis(100)) : Flux.just("nameB1", "nameB2");
    }



}
