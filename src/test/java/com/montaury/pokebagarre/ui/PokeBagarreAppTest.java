package com.montaury.pokebagarre.ui;
import java.util.concurrent.TimeUnit;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
@ExtendWith(ApplicationExtension.class)
class PokeBagarreAppTest {
    private static final String IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1 = "#nomPokemon1";
    private static final String IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2 = "#nomPokemon2";
    private static final String IDENTIFIANT_BOUTON_BAGARRE = ".button";
    @Start
    private void start(Stage stage) {
        new PokeBagarreApp().start(stage);
    }
    @Test
    void devrait_afficher_une_erreur_si_premier_pokemon_non_renseigne(FxRobot unRobot) {
        //robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        //robot.write("Text");
        //await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
        //assertThat(...).isEqualTo(...)
        // );

        unRobot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        await().atMost(5,TimeUnit.SECONDS).untilAsserted(()->assertThat(getMessageErreur(unRobot)).isEqualTo("Erreur: Le premier pokemon n'est pas renseigne"));
    }

    @Test
    void devrait_afficher_une_erreur_si_deuxieme_pokemon_non_renseigne(FxRobot unRobot){
        unRobot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        unRobot.write("pikachu");
        unRobot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        await().atMost(5,TimeUnit.SECONDS).untilAsserted(()->assertThat(getMessageErreur(unRobot)).isEqualTo("Erreur: Le second pokemon n'est pas renseigne"));
    }

    @Test
    void devrait_afficher_une_erreur_si_les_deux_pokemons_sont_identiques(FxRobot unRobot) {
        unRobot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        unRobot.write("pikachu");
        unRobot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        unRobot.write("pikachu");
        unRobot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        await().atMost(5,TimeUnit.SECONDS).untilAsserted(()->assertThat(getMessageErreur(unRobot)).isEqualTo("Erreur: Impossible de faire se bagarrer un pokemon avec lui-meme"));
    }

    @Test
    void devrait_afficher_une_erreur_si_le_premier_pokemon_est_inconnue(FxRobot unRobot)
    {
        unRobot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        unRobot.write("moauzea");
        unRobot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        unRobot.write("pikachu");
        unRobot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        await().atMost(5,TimeUnit.SECONDS).untilAsserted(()->assertThat(getMessageErreur(unRobot)).isEqualTo("Erreur: Impossible de recuperer les details sur 'moauzea'"));
    }

    @Test
    void devrait_afficher_une_erreur_si_le_deuxieme_pokemon_est_inconnue(FxRobot unRobot){
        unRobot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        unRobot.write("pikachu");
        unRobot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        unRobot.write("moifl");
        unRobot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        await().atMost(5,TimeUnit.SECONDS).untilAsserted(()->assertThat(getMessageErreur(unRobot)).isEqualTo("Erreur: Impossible de recuperer les details sur 'moifl'"));
    }

    @Test
    void devrait_determiner_le_vainqueur_lorsque_les_deux_pokemons_sont_renseignes_et_different(FxRobot unRobot){
        unRobot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
        unRobot.write("MewTwo");
        unRobot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
        unRobot.write("pikachu");
        unRobot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        await().atMost(5,TimeUnit.SECONDS).untilAsserted(()->assertThat(getResultatBagarre(unRobot)).isEqualTo("Le vainqueur est: Mewtwo"));
    }
    private static String getResultatBagarre(FxRobot robot) {
        return robot.lookup("#resultatBagarre").queryText().getText();
    }

    private static String getMessageErreur(FxRobot robot) {
        return robot.lookup("#resultatErreur").queryLabeled().getText();
    }
}