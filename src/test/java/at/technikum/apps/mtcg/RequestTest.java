package at.technikum.apps.mtcg;

import at.technikum.server.http.HttpMethod;
import at.technikum.server.http.Request;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTest {

    @Test
    void testGetBodyWithId() {
        Request request = new Request();
        request.setRoute("/echo/1");
        request.setBody("id=99");

        assertEquals("id=99", request.getBody());
    }

    @Test
    void testGetRoute() {
        Request request = new Request();
        request.setRoute("/echo");

        assertEquals("/echo", request.getRoute());
    }

    @Test
    void testGetMethod() {
        Request request = new Request();
        request.setMethod(HttpMethod.GET);

        assertEquals("GET", request.getMethod());
    }

    @Test
    void testGetToken() {
        Request request = new Request();
        request.setToken("mtcgToken");

        assertEquals("mtcgToken", request.getToken());
    }
}
