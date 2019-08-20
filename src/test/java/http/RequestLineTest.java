package http;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RequestLineTest {

  @Test
  @DisplayName("Request Line을 파싱한다")
  void parse() {
    RequestLine requestLine = RequestLine
        .parse("GET /users?userId=javajigi&password=password&name=JaeSung&noValue= HTTP/1.1");

    assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
    assertThat(requestLine.getPath()).isEqualTo("/users");
    assertThat(requestLine.getVersion()).isEqualTo("HTTP/1.1");
    assertThat(requestLine.getParameters()).hasSize(4)
        .containsEntry("userId", "javajigi")
        .containsEntry("password", "password")
        .containsEntry("name", "JaeSung")
        .containsEntry("noValue", "");
  }

  @Test
  @DisplayName("queryString이 없을때 Parameters는 빈값이다")
  void emptyQueryString() {
    RequestLine requestLine = RequestLine.parse("GET /users HTTP/1.1");

    assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
    assertThat(requestLine.getPath()).isEqualTo("/users");
    assertThat(requestLine.getVersion()).isEqualTo("HTTP/1.1");
    assertThat(requestLine.getParameters()).isEqualTo(Collections.EMPTY_MAP);
  }

}