package webserver.resolver;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.request.RequestHeaders;
import webserver.request.RequestLine;
import webserver.resolver.resource.ResourceResolver;
import webserver.response.HttpResponse;
import webserver.response.HttpStatus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

/**
 * Created by hspark on 2019-08-05.
 */
class ResourceResolverTest {
    private static final Logger log = LoggerFactory.getLogger(ResourceResolverTest.class);
    public ResourceResolver resourceResolver = new ResourceResolver();

    @ParameterizedTest(name = "httpRequest : {0}")
    @MethodSource("getTestSources")
    void test_file_resolve(HttpRequest httpRequest) throws IOException, URISyntaxException {
        HttpResponse httpResponse = new HttpResponse(httpRequest);
        resourceResolver.resolve(httpRequest, httpResponse);
        log.debug("file : {}", new String(httpResponse.getBody()));
    }

    void test_resolver_notfound() {
        HttpRequest httpRequest = HttpRequest.builder().requestLine(RequestLine.parse("GET /test/test.css HTTP/1.1")).build();
        HttpResponse httpResponse = new HttpResponse(httpRequest);
        resourceResolver.resolve(httpRequest, httpResponse);
        Assertions.assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    void test_notfound_resource_registration() {
        HttpRequest httpRequest = HttpRequest.builder().requestLine(RequestLine.parse("GET /test/test.jsx HTTP/1.1")).build();
        HttpResponse httpResponse = new HttpResponse(httpRequest);
        Assertions.assertThatThrownBy(() -> resourceResolver.resolve(httpRequest, httpResponse))
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> getTestSources() {
        RequestHeaders headers = new RequestHeaders();
        headers.add("Accept: text/html");

        return Stream.of(
                Arguments.of(HttpRequest.builder().requestLine(RequestLine.parse("GET /css/bootstrap.min.css HTTP/1.1")).requestHeaders(headers).build()),
                Arguments.of(HttpRequest.builder().requestLine(RequestLine.parse("GET /js/bootstrap.min.js HTTP/1.1")).requestHeaders(headers).build()),
                Arguments.of(HttpRequest.builder().requestLine(RequestLine.parse("GET /index.html HTTP/1.1")).requestHeaders(headers).build())
        );
    }
}