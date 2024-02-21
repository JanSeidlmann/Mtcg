package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.controller.CardController;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CardTest {
    private CardRepository cardRepositoryMock;
    private PackageService packageServiceMock;
    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardRepositoryMock = mock(CardRepository.class);
        packageServiceMock = mock(PackageService.class);
        cardService = spy(new CardService(cardRepositoryMock, packageServiceMock));
    }

    @Test
    void testGetCardsWithCards() {
        Request requestMock = mock(Request.class);
        when(requestMock.getToken()).thenReturn("Bearer testuser-mtcgToken");

        when(packageServiceMock.extractUsernameFromToken("Bearer testuser-mtcgToken")).thenReturn("testuser");

        List<Card> cards = new ArrayList<>();
        Card card1 = new Card();
        card1.setName("TestCard1");
        card1.setType("Monster");
        card1.setDamage(10);
        cards.add(card1);

        Card card2 = new Card();
        card2.setName("TestCard2");
        card2.setType("Spell");
        card2.setDamage(20);
        cards.add(card2);

        when(cardRepositoryMock.getCards("testuser")).thenReturn(cards);

        Response response = cardService.getCards(requestMock);

        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
        assertEquals("The user has cards, the response contains these" + cards, response.getBody());
    }

    @Test
    void testGetCardsWithoutCards() {
        Request requestMock = mock(Request.class);
        when(requestMock.getToken()).thenReturn("Bearer testuser-mtcgToken");
        when(packageServiceMock.extractUsernameFromToken("Bearer testuser-mtcgToken")).thenReturn("testuser");
        when(cardRepositoryMock.getCards("testuser")).thenReturn(new ArrayList<>());

        Response response = cardService.getCards(requestMock);

        assertEquals(HttpStatus.NO_CONTENT.getCode(), response.getStatusCode());
        assertEquals("The request was fine, but the user doesn't have any cards", response.getBody());
    }

    @Test
    void testExtractTypeFromName() {
        Card card = new Card();
        card.setName("WaterCard");

        assertEquals("Water", card.extractTypeFromName());
    }
}
