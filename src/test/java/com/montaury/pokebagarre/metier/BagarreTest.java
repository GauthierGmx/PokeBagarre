package com.montaury.pokebagarre.metier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.registerCustomDateFormat;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.erreurs.ErreurRecuperationPokemon;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import net.bytebuddy.implementation.bytecode.Throw;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.table.TableRowSorter;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class BagarreTest {
PokeBuildApi uneFausseApi;
Bagarre uneBagarre;
    @BeforeEach
    void preparer(){
        uneFausseApi = Mockito.mock(PokeBuildApi.class);
        uneBagarre = new Bagarre(uneFausseApi);
    }

    @Test
    void devrait_lever_une_erreur_si_le_premier_pokemon_est_nul() {
        /* GIVEN */
        //Bagarre uneBagarre = new Bagarre();
        Throwable uneErreur = Assertions.catchThrowable(()->uneBagarre.demarrer(null,"pikachu"));

        /* THEN*/
        assertThat(uneErreur).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void devrait_lever_une_erreur_si_le_premier_pokemon_est_vide() {
        /* GIVEN */
        //Bagarre uneBagarre = new Bagarre();
        Throwable uneErreur = Assertions.catchThrowable(()->uneBagarre.demarrer("","pikachu"));

        /* THEN*/
        assertThat(uneErreur).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void devrait_lever_une_erreur_si_le_deuxieme_pokemon_est_nul() {
        /* GIVEN */
        //Bagarre uneBagarre = new Bagarre();
        Throwable uneErreur = Assertions.catchThrowable(()->uneBagarre.demarrer("pikachu",null));

        /* THEN*/
        assertThat(uneErreur).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le second pokemon n'est pas renseigne");
    }

    @Test
    void devrait_lever_une_erreur_si_le_deuxieme_pokemon_est_vide() {
        /* GIVEN */
        //Bagarre uneBagarre = new Bagarre();
        Throwable uneErreur = Assertions.catchThrowable(()->uneBagarre.demarrer("pikachu",""));

        /* THEN*/
        assertThat(uneErreur).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le second pokemon n'est pas renseigne");
    }

    @Test
    void devrait_lever_une_erreur_si_les_deux_pokemon_sont_identique() {
        /* GIVEN */
        //Bagarre uneBagarre = new Bagarre();
        Throwable uneErreur = Assertions.catchThrowable(()->uneBagarre.demarrer("pikachu","pikachu"));

        /* THEN*/
        assertThat(uneErreur).isInstanceOf(ErreurMemePokemon.class).hasMessage("Impossible de faire se bagarrer un pokemon avec lui-meme");
    }

    @Test
    void devrait_lever_une_erreur_api_avec_premier_pokemon() {
        // GIVEN
            // clause 'when' de fausseApi --> pour créer les Pokemon dans la configuration souhaitée
            Mockito.when(uneFausseApi.recupererParNom("pikachu")).thenReturn(CompletableFuture.completedFuture(new Pokemon("pikachu", "url_pikachu", new Stats(1,2))));
            Mockito.when(uneFausseApi.recupererParNom("scarabrute")).thenReturn(CompletableFuture.failedFuture(new ErreurRecuperationPokemon("scarabrute")));
        // WHEN
            CompletableFuture<Pokemon> futurVainqueur = uneBagarre.demarrer("pikachu", "scarabrute");
        
        // THEN
            assertThat(futurVainqueur)
                    .failsWithin(Duration.ofSeconds(2))
                    .withThrowableOfType(ExecutionException.class)
                    .havingCause()
                    .isInstanceOf(ErreurRecuperationPokemon.class)
                    .withMessage("Impossible de recuperer les details sur 'scarabrute'");
    }
        
    @Test
    void devrait_lever_une_erreur_api_avec_deuxieme_pokemon() {
        /* GIVEN */
        Mockito.when(uneFausseApi.recupererParNom("pikachu")).thenReturn(CompletableFuture.failedFuture(new ErreurRecuperationPokemon("pikachu")));
        Mockito.when(uneFausseApi.recupererParNom("scarabrute")).thenReturn(CompletableFuture.completedFuture(new Pokemon("scarabrute", "url_scarabrute", new Stats(1, 2))));

        /* WHEN */
        CompletableFuture<Pokemon> futurVainqueur = uneBagarre.demarrer("pikachu", "scarabrute");

        /* THEN */
        assertThat(futurVainqueur)
                .failsWithin(Duration.ofSeconds(2))
                .withThrowableOfType(ExecutionException.class)
                .havingCause()
                .isInstanceOf(ErreurRecuperationPokemon.class)
                .withMessage("Impossible de recuperer les details sur 'pikachu'");
    }

    @Test
    void devrait_declarer_le_premier_pokemon_vainqueur() {
        /* GIVEN */
        Mockito.when(uneFausseApi.recupererParNom("pikachu")).thenReturn(CompletableFuture.completedFuture(new Pokemon("pikachu", "url_pikachu", new Stats(2, 1))));
        Mockito.when(uneFausseApi.recupererParNom("scarabrute")).thenReturn(CompletableFuture.completedFuture(new Pokemon("scarabrute", "url_scarabrute", new Stats(1,2))));

        /* WHEN */
        CompletableFuture<Pokemon> futurVainqueur = uneBagarre.demarrer("pikachu", "scarabrute");

        /* THEN */
        assertThat(futurVainqueur)
                .succeedsWithin(Duration.ofSeconds(2))
                .satisfies(pokemon -> assertThat(pokemon.getNom()).isEqualTo("pikachu"));
    }

    @Test
    void devrait_declarer_le_deuxieme_pokemon_vainqueur() {
        /* GIVEN */
        Mockito.when(uneFausseApi.recupererParNom("pikachu")).thenReturn(CompletableFuture.completedFuture(new Pokemon("pikachu", "url_pikachu", new Stats(1, 2))));
        Mockito.when(uneFausseApi.recupererParNom("scarabrute")).thenReturn(CompletableFuture.completedFuture(new Pokemon("scarabrute", "url_scarabrute", new Stats(2,1))));

        /* WHEN */
        CompletableFuture<Pokemon> futurVainqueur = uneBagarre.demarrer("pikachu", "scarabrute");

        /* THEN */
        assertThat(futurVainqueur)
                .succeedsWithin(Duration.ofSeconds(2))
                .satisfies(pokemon -> assertThat(pokemon.getNom()).isEqualTo("scarabrute"));
    }
}