package com.montaury.pokebagarre.metier;

import static org.assertj.core.api.Assertions.assertThat;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import net.bytebuddy.implementation.bytecode.Throw;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.table.TableRowSorter;

import static org.junit.jupiter.api.Assertions.*;

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
}