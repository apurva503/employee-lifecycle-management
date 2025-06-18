package com.pepsico.apigateway.filter;

import org.springframework.stereotype.Component;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Global filter to validate the presence of the X-Correlation-ID header in all requests.
 * Responds with 400 Bad Request if the header is missing.
 */
@Component
public class HeaderValidationFilter implements GlobalFilter, Ordered {
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String MISSING_HEADER_MESSAGE = "Missing X-Correlation-ID";

    /**
     * Checks for the X-Correlation-ID header in incoming requests.
     *
     * @param exchange the current server exchange
     * @param chain the gateway filter chain
     * @return a Mono that completes when the filter chain is finished
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        // Check for mandatory header
        if (!headers.containsKey(CORRELATION_ID_HEADER)) {
            // Respond with 400 Bad Request
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            byte[] bytes = MISSING_HEADER_MESSAGE.getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }
        // Header present → proceed
        return chain.filter(exchange);
    }

    /**
     * Sets the order of this filter to high precedence.
     * @return the order value
     */
    @Override
    public int getOrder() {
        return -1; // High precedence: run before most other filters
    }
}