/*
 * package com.pepsico.apigateway.filter;
 * 
 * import org.springframework.stereotype.Component; import
 * org.springframework.core.Ordered; import
 * org.springframework.core.io.buffer.DataBuffer; import
 * org.springframework.http.HttpHeaders; import
 * org.springframework.http.HttpStatus; import
 * org.springframework.http.server.reactive.ServerHttpRequest; import
 * org.springframework.stereotype.Component; import
 * org.springframework.web.server.ServerWebExchange; import
 * org.springframework.cloud.gateway.filter.GlobalFilter; import
 * org.springframework.cloud.gateway.filter.GatewayFilterChain; import
 * reactor.core.publisher.Mono;
 * 
 * import java.nio.charset.StandardCharsets;
 * 
 * @Component public class HeaderValidationFilter implements GlobalFilter,
 * Ordered {
 * 
 * @Override public Mono<Void> filter(ServerWebExchange exchange,
 * GatewayFilterChain chain) { ServerHttpRequest request =
 * exchange.getRequest(); HttpHeaders headers = request.getHeaders();
 * 
 * // Check for mandatory header if (!headers.containsKey("X-Correlation-ID")) {
 * // Respond with 400 Bad Request
 * exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
 * 
 * // Optional: Add error message in body byte[] bytes =
 * "Missing X-Correlation-ID".getBytes(StandardCharsets.UTF_8); DataBuffer
 * buffer = exchange.getResponse().bufferFactory().wrap(bytes); return
 * exchange.getResponse().writeWith(Mono.just(buffer)); }
 * 
 * // Header present → proceed return chain.filter(exchange); }
 * 
 * @Override public int getOrder() { return -1; // High precedence: run before
 * most other filters } }
 */