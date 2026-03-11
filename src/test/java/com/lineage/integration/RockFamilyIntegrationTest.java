package com.lineage.domain.model;

import com.lineage.domain.valueobject.DateValue;
import com.lineage.domain.valueobject.NodeType;
import com.lineage.domain.valueobject.StringValue;
import com.lineage.domain.model.Property;
import com.lineage.domain.model.Node;
import com.lineage.domain.model.Relationship;
import com.lineage.domain.model.GraphAggregate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class RockFamilyIntegrationTest {

    private static final NodeType GENRE = NodeType.of("GENRE");
    private static final NodeType ARTIST = NodeType.of("ARTIST");

    private static final String EVOLVED_INTO = "EVOLVED_INTO";
    private static final String INFLUENCED_BY = "INFLUENCED_BY";
    private static final String BELONGS_TO = "BELONGS_TO";

    private GraphAggregate rockFamily;
    private UUID graphId;

    // -------------------------
    // Genres
    // -------------------------
    private Node gospel;
    private Node countryBlues;
    private Node folk;
    private Node country;
    private Node urbanBlues;
    private Node rhythmNBlues;
    private Node rockNRoll;
    private Node rockabilly;
    private Node soul;
    private Node ska;
    private Node artRock;
    private Node popRock;
    private Node britishRhythmNBlues;
    private Node rockSteady;
    private Node dooWop;
    private Node jazzRock;
    private Node bluesRock;
    private Node westCoast;
    private Node hardRock;
    private Node reggae;
    private Node funk;
    private Node soul2;
    private Node fusion;
    private Node disco;
    private Node rap;
    private Node newSka;
    private Node heavyMetal;
    private Node punkEra;
    private Node punkRock;
    private Node coldWave;
    private Node newAge;
    private Node ragamuffin;
    private Node dance;
    private Node countryRock;
    private Node folkRock;
    private Node countryMusic;

    @BeforeEach
    void setUp() {
        graphId = UUID.randomUUID();
        rockFamily = new GraphAggregate(graphId, "La Grande Famille du Rock");

        // Racines — années 50 et avant
        gospel = genre("Gospel");
        countryBlues = genre("Country Blues");
        folk = genre("Folk");
        country = genre("Country");
        urbanBlues = genre("Urban Blues");

        // 1954
        rhythmNBlues = genre("Rhythm'n'Blues");
        rockNRoll = genre("Rock'n'Roll");
        rockabilly = genre("Rockabilly");
        dooWop = genre("Doo Wop");

        // 1958-1960
        soul = genre("Soul");
        ska = genre("Ska");
        artRock = genre("Art Rock");
        popRock = genre("Pop Rock");
        britishRhythmNBlues = genre("British Rhythm'n'Blues");
        rockSteady = genre("Rock Steady");

        // 1964-1968
        jazzRock = genre("Jazz Rock");
        bluesRock = genre("Blues Rock");
        westCoast = genre("West Coast");
        hardRock = genre("Hard Rock");
        reggae = genre("Reggae");
        punkEra = genre("Punk Era");

        // 1970-1976
        funk = genre("Funk");
        fusion = genre("Fusion");
        disco = genre("Disco");
        newSka = genre("New Ska");
        heavyMetal = genre("Heavy Metal");
        punkRock = genre("Punk Rock");
        coldWave = genre("Cold Wave");
        newAge = genre("New Age");
        countryRock = genre("Country Rock");
        folkRock = genre("Folk Rock");
        countryMusic = genre("Country Music");

        // 1980+
        rap = genre("Rap");
        ragamuffin = genre("Ragamuffin");
        dance = genre("Dance");

        // Ajout au graphe
        rockFamily.addNode(gospel);
        rockFamily.addNode(countryBlues);
        rockFamily.addNode(folk);
        rockFamily.addNode(country);
        rockFamily.addNode(urbanBlues);
        rockFamily.addNode(rhythmNBlues);
        rockFamily.addNode(rockNRoll);
        rockFamily.addNode(rockabilly);
        rockFamily.addNode(dooWop);
        rockFamily.addNode(soul);
        rockFamily.addNode(ska);
        rockFamily.addNode(artRock);
        rockFamily.addNode(popRock);
        rockFamily.addNode(britishRhythmNBlues);
        rockFamily.addNode(rockSteady);
        rockFamily.addNode(jazzRock);
        rockFamily.addNode(bluesRock);
        rockFamily.addNode(westCoast);
        rockFamily.addNode(hardRock);
        rockFamily.addNode(reggae);
        rockFamily.addNode(punkEra);
        rockFamily.addNode(funk);
        rockFamily.addNode(fusion);
        rockFamily.addNode(disco);
        rockFamily.addNode(newSka);
        rockFamily.addNode(heavyMetal);
        rockFamily.addNode(punkRock);
        rockFamily.addNode(coldWave);
        rockFamily.addNode(newAge);
        rockFamily.addNode(countryRock);
        rockFamily.addNode(folkRock);
        rockFamily.addNode(countryMusic);
        rockFamily.addNode(rap);
        rockFamily.addNode(ragamuffin);
        rockFamily.addNode(dance);
    }

    // -------------------------
    // Helper factory methods
    // -------------------------
    private Node genre(String name) {
        return new Node(UUID.randomUUID(), name, null, GENRE);
    }

    private Node artist(String name) {
        return new Node(UUID.randomUUID(), name, null, ARTIST);
    }

    private DateValue date(int year) {
        return new DateValue(LocalDate.of(year, 1, 1));
    }

    private Relationship evolvedInto(Node from, Node to, int year) {
        return new Relationship(from, to, EVOLVED_INTO, date(year), null);
    }

    private Relationship influencedBy(Node from, Node to, int year) {
        return new Relationship(from, to, INFLUENCED_BY, date(year), null);
    }

    // -------------------------
    // Tests
    // -------------------------
    @Test
    void shouldContainAllGenres() {
        assertThat(rockFamily.getNodes()).hasSize(35);
        assertThat(rockFamily.findNodesByType(GENRE)).hasSize(35);
    }

    @Test
    void shouldModelRootsEvolvingIntoRockNRoll() {
        Relationship gospelToRock = evolvedInto(gospel, rockNRoll, 1954);
        Relationship rhythmToRock = evolvedInto(rhythmNBlues, rockNRoll, 1954);
        Relationship countryToRock = evolvedInto(country, rockNRoll, 1954);
        Relationship urbanToRhythm = evolvedInto(urbanBlues, rhythmNBlues, 1954);

        rockFamily.addRelationship(gospelToRock);
        rockFamily.addRelationship(rhythmToRock);
        rockFamily.addRelationship(countryToRock);
        rockFamily.addRelationship(urbanToRhythm);

        assertThat(rockFamily.getRelationships()).hasSize(4);
        assertThat(rockFamily.getRelationships())
                .extracting(r -> r.getFrom().getLabel())
                .contains("Gospel", "Rhythm'n'Blues", "Country", "Urban Blues");
    }

    @Test
    void shouldModelSkaLineage() {
        // Gospel → R&B → Rock'n'Roll → Ska → Rock Steady → Reggae → New Ska
        rockFamily.addRelationship(evolvedInto(gospel, rhythmNBlues, 1954));
        rockFamily.addRelationship(evolvedInto(rhythmNBlues, rockNRoll, 1954));
        rockFamily.addRelationship(evolvedInto(rockNRoll, ska, 1958));
        rockFamily.addRelationship(evolvedInto(ska, rockSteady, 1960));
        rockFamily.addRelationship(evolvedInto(rockSteady, reggae, 1974));
        rockFamily.addRelationship(evolvedInto(reggae, newSka, 1976));
        rockFamily.addRelationship(evolvedInto(newSka, ragamuffin, 1984));

        assertThat(rockFamily.getRelationships()).hasSize(7);
        assertThat(rockFamily.getRelationships())
                .allMatch(r -> r.getStartDate().isPresent())
                .allMatch(r -> r.isActive());
    }

    @Test
    void shouldModelPunkLineage() {
        // Rock'n'Roll → British R&B → Blues Rock → Hard Rock → Punk Era → Punk Rock →
        // Cold Wave
        rockFamily.addRelationship(evolvedInto(rockNRoll, britishRhythmNBlues, 1962));
        rockFamily.addRelationship(evolvedInto(britishRhythmNBlues, bluesRock, 1964));
        rockFamily.addRelationship(evolvedInto(bluesRock, hardRock, 1968));
        rockFamily.addRelationship(evolvedInto(hardRock, punkEra, 1970));
        rockFamily.addRelationship(evolvedInto(punkEra, punkRock, 1974));
        rockFamily.addRelationship(evolvedInto(punkRock, coldWave, 1980));

        assertThat(rockFamily.getRelationships()).hasSize(6);
    }

    @Test
    void shouldModelFunkAndDanceLineage() {
        // Soul → Funk → Disco → Dance / Rap
        rockFamily.addRelationship(evolvedInto(gospel, soul, 1958));
        rockFamily.addRelationship(evolvedInto(soul, funk, 1970));
        rockFamily.addRelationship(evolvedInto(funk, disco, 1974));
        rockFamily.addRelationship(evolvedInto(disco, dance, 1982));
        rockFamily.addRelationship(evolvedInto(funk, rap, 1982));

        assertThat(rockFamily.getRelationships()).hasSize(5);
        assertThat(rockFamily.findNodesByType(GENRE))
                .extracting(Node::getLabel)
                .contains("Soul", "Funk", "Disco", "Dance", "Rap");
    }

    @Test
    void shouldModelArtistsAttachedToGenres() {
        Node muddy = artist("Muddy Waters");
        Node chuckBerry = artist("Chuck Berry");
        Node elvis = artist("Elvis Presley");
        Node bobMarley = artist("Bob Marley");
        Node theClash = artist("The Clash");

        rockFamily.addNode(muddy);
        rockFamily.addNode(chuckBerry);
        rockFamily.addNode(elvis);
        rockFamily.addNode(bobMarley);
        rockFamily.addNode(theClash);

        rockFamily.addRelationship(new Relationship(muddy, urbanBlues, BELONGS_TO, date(1950), null));
        rockFamily.addRelationship(new Relationship(chuckBerry, rockNRoll, BELONGS_TO, date(1954), null));
        rockFamily.addRelationship(new Relationship(elvis, rockabilly, BELONGS_TO, date(1954), null));
        rockFamily.addRelationship(new Relationship(bobMarley, reggae, BELONGS_TO, date(1974), null));
        rockFamily.addRelationship(new Relationship(theClash, newSka, BELONGS_TO, date(1976), null));

        assertThat(rockFamily.findNodesByType(ARTIST)).hasSize(5);
        assertThat(rockFamily.getRelationships())
                .extracting(r -> r.getFrom().getLabel())
                .contains("Muddy Waters", "Chuck Berry", "Elvis Presley", "Bob Marley", "The Clash");
    }

    @Test
    void shouldModelCompleteRockFamily() {
        // Racines
        rockFamily.addRelationship(evolvedInto(gospel, rhythmNBlues, 1954));
        rockFamily.addRelationship(evolvedInto(gospel, soul, 1958));
        rockFamily.addRelationship(evolvedInto(countryBlues, urbanBlues, 1954));
        rockFamily.addRelationship(evolvedInto(urbanBlues, rhythmNBlues, 1954));
        rockFamily.addRelationship(evolvedInto(folk, folkRock, 1966));
        rockFamily.addRelationship(evolvedInto(country, rockNRoll, 1954));
        rockFamily.addRelationship(evolvedInto(country, countryRock, 1966));
        rockFamily.addRelationship(evolvedInto(country, countryMusic, 1968));

        // 1954-1960
        rockFamily.addRelationship(evolvedInto(rhythmNBlues, rockNRoll, 1954));
        rockFamily.addRelationship(evolvedInto(rhythmNBlues, dooWop, 1954));
        rockFamily.addRelationship(evolvedInto(rockNRoll, rockabilly, 1954));
        rockFamily.addRelationship(evolvedInto(rockNRoll, ska, 1958));
        rockFamily.addRelationship(evolvedInto(rockNRoll, artRock, 1960));
        rockFamily.addRelationship(evolvedInto(rockNRoll, popRock, 1960));
        rockFamily.addRelationship(evolvedInto(rockNRoll, britishRhythmNBlues, 1962));
        rockFamily.addRelationship(evolvedInto(ska, rockSteady, 1960));
        rockFamily.addRelationship(evolvedInto(soul, funk, 1970));

        // 1964-1970
        rockFamily.addRelationship(evolvedInto(britishRhythmNBlues, bluesRock, 1964));
        rockFamily.addRelationship(evolvedInto(britishRhythmNBlues, jazzRock, 1966));
        rockFamily.addRelationship(evolvedInto(bluesRock, westCoast, 1966));
        rockFamily.addRelationship(evolvedInto(bluesRock, hardRock, 1968));
        rockFamily.addRelationship(evolvedInto(rockSteady, reggae, 1974));
        rockFamily.addRelationship(evolvedInto(hardRock, punkEra, 1970));
        rockFamily.addRelationship(evolvedInto(jazzRock, fusion, 1970));

        // 1974-1980
        rockFamily.addRelationship(evolvedInto(funk, disco, 1974));
        rockFamily.addRelationship(evolvedInto(punkEra, punkRock, 1974));
        rockFamily.addRelationship(evolvedInto(reggae, newSka, 1976));
        rockFamily.addRelationship(evolvedInto(punkRock, coldWave, 1980));
        rockFamily.addRelationship(evolvedInto(hardRock, heavyMetal, 1982));

        // 1980+
        rockFamily.addRelationship(evolvedInto(funk, rap, 1982));
        rockFamily.addRelationship(evolvedInto(disco, dance, 1982));
        rockFamily.addRelationship(evolvedInto(newSka, ragamuffin, 1984));
        rockFamily.addRelationship(evolvedInto(coldWave, newAge, 1984));

        assertThat(rockFamily.getRelationships()).hasSize(33);
        assertThat(rockFamily.findNodesByType(GENRE)).hasSize(35);

        // Toutes les relations ont une date
        assertThat(rockFamily.getRelationships())
                .allMatch(r -> r.getStartDate().isPresent());

        // Toutes les relations sont actives — aucun genre n'a disparu
        assertThat(rockFamily.getRelationships())
                .allMatch(Relationship::isActive);
    }
}