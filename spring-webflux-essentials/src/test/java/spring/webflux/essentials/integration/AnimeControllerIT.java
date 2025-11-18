package spring.webflux.essentials.integration;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.webflux.essentials.domain.Anime;
import spring.webflux.essentials.repository.AnimeRepository;
import spring.webflux.essentials.util.AnimeCreator;
import spring.webflux.essentials.util.WebTestClientUtil;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class AnimeControllerIT {

//    @Autowired
//    private WebTestClientUtil webTestClientUtil;

    private final static String REGULAR_USER = "user";
    private final static String ADMIN_USER = "yan";
    @MockBean
    private AnimeRepository animeRepository;

    @Autowired
    private WebTestClient client;

//    private WebTestClient testClientUser;
//
//    private WebTestClient testClientAdmin;
//
//    private WebTestClient testClientInvalid;

    private final Anime anime = AnimeCreator.createValidAnime();

    @BeforeAll
    public static void BlockHoundSetup(){
        BlockHound.install();
    }

    @BeforeEach
    public void setUp(){
//        testClientUser = webTestClientUtil.authenticateClient("user", "yan123");
//        testClientAdmin = webTestClientUtil.authenticateClient("yan", "yan123");
//        testClientInvalid = webTestClientUtil.authenticateClient("invalidUser", "invalidPassword");


        BDDMockito.when(animeRepository.findAll()).thenReturn(Flux.just(anime));
        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Mono.just(anime));
        BDDMockito.when(animeRepository.save(AnimeCreator.createAnimeToBeSaved())).thenReturn(Mono.just(anime));
        BDDMockito.when(animeRepository
                        .saveAll(List.of(AnimeCreator.createAnimeToBeSaved(), AnimeCreator.createAnimeToBeSaved())))
                .thenReturn(Flux.just(anime, anime));
        BDDMockito.when(animeRepository.delete(ArgumentMatchers.any(Anime.class))).thenReturn(Mono.empty());
        BDDMockito.when(animeRepository.save(AnimeCreator.createValidAnime())).thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("listAll returns a flux of anime")
    @WithUserDetails(REGULAR_USER)
    public void listAll_ReturnFluxOfAnime_WhenSuccessful(){
        client.get().uri("/animes")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().jsonPath("$.[0].id").isEqualTo(anime.getId())
                .jsonPath("$.[0].name").isEqualTo(anime.getName());
    }

    @Test
    @DisplayName("listAll returns a flux of anime")
    @WithUserDetails(REGULAR_USER)
    public void listAll_Flavor2_ReturnFluxOfAnime_WhenSuccessful(){
        client.get().uri("/animes")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Anime.class)
                .hasSize(1)
                .contains(anime);
    }

    @Test
    @DisplayName("findById returns Mono with anime when it exists")
    @WithUserDetails(REGULAR_USER)
    public void findById_ReturnMonoAnime_WhenSuccessful(){
        client.get().uri("/animes/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Anime.class)
                .isEqualTo(anime);
    }

    @Test
    @DisplayName("findById returns Mono error when anime does not exists")
    @WithUserDetails(REGULAR_USER)
    public void findById_ReturnMonoError_WhenEmptyMonoIsReturned(){

        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Mono.empty());
        client.get().uri("/animes/{id}", 1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.developerMessage").isEqualTo("A ResponseStatusException Happened");
    }

    @Test
    @DisplayName("save create an anime when user successfully authenticated and have the role admin")
    @WithUserDetails(ADMIN_USER)
    public void save_CreatesAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        client.post().uri("/animes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(animeToBeSaved))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Anime.class)
                .isEqualTo(anime);
    }



    @Test
    @DisplayName("save returns Unauthorized when user is not authenticated")
    public void save_ReturnsUnauthorized_WhenUserIsNotAuthenticated(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        client.post().uri("/animes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(animeToBeSaved))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("save returns forbidden when user successfully authenticated and does not have role admin")
    @WithUserDetails(REGULAR_USER)
    public void save_ReturnsForbidden_WhenUserDoesNotHaveRoleAdmin(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        client.post().uri("/animes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(animeToBeSaved))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("saveBatch creates a list of anime when user successfully authenticated and have the role admin")
    @WithUserDetails(ADMIN_USER)
    public void saveBatch_CreatesListOfAnime_WhenSuccessfull(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();


        client.post().uri("/animes/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(List.of(animeToBeSaved, animeToBeSaved)))
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Anime.class)
                .hasSize(2)
                .contains(anime);
    }

    @Test
    @DisplayName("save returns mono error with bad request when name is empty when user successfully authenticated and have the role admin")
    @WithUserDetails(ADMIN_USER)
    public void save_ReturnsError_WhenNameIsEmpty(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved().withName("");
        client.post().uri("/animes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(animeToBeSaved))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400);
    }

    @Test
    @DisplayName("saveBatch returns mono error when one of the objects in the list contains empty or null name and when user successfully authenticated and have the role admin")
    @WithUserDetails(ADMIN_USER)
    public void saveBatch_ReturnsError_WhenNameIsEmpty(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();

        client.post().uri("/animes/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(List.of(animeToBeSaved, animeToBeSaved.withName(""))))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400);
    }


    @Test
    @DisplayName("delete removes the anime when user successfully authenticated and have the role admin")
    @WithUserDetails(ADMIN_USER)
    public void delete_RemovesAnime_WhenSuccessful(){
        client.delete().uri("/animes/{id}", 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("delete returns Mono error when anime does not exists when user successfully authenticated and have the role admin")
    @WithUserDetails(ADMIN_USER)
    public void delete_ReturnMonoError_WhenEmptyMonoIsReturned(){
        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Mono.empty());

        client.delete().uri("/animes/{id}", 1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.developerMessage").isEqualTo("A ResponseStatusException Happened");
    }

    @Test
    @DisplayName("update save updated anime and return empty mono when user successfully authenticated and have the role admin")
    @WithUserDetails(ADMIN_USER)
    public void update_SaveUpdatedAnime_WhenSuccessful(){
        client.put().uri("/animes/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(anime))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("update returns Mono error when anime does not exists and when user successfully authenticated and have the role admin")
    @WithUserDetails(ADMIN_USER)
    public void update_ReturnMonoError_WhenEmptyMonoIsReturned(){
        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Mono.empty());

        client.put().uri("/animes/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(anime))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.developerMessage").isEqualTo("A ResponseStatusException Happened");
    }

}
