package at.technikum.server.http;

import java.util.Objects;

public class Response {

    private int statusCode;

    private String statusMessage;

    private String contentType;

    private String body;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Response response = (Response) o;
        return statusCode == response.statusCode && Objects.equals(statusMessage, response.statusMessage) && Objects.equals(contentType, response.contentType) && Objects.equals(body, response.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, statusMessage, contentType, body);
    }

    public void setStatus(HttpStatus httpStatus) {
        this.statusCode = httpStatus.getCode();
        this.statusMessage = httpStatus.getMessage();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(HttpContentType httpContentType) {
        this.contentType = httpContentType.getMimeType();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
