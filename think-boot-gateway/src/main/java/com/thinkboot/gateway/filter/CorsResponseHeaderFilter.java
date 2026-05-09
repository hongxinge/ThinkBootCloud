package com.thinkboot.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Component
public class CorsResponseHeaderFilter implements GlobalFilter, Ordered {

    private static final String CORS_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String CORS_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private static final String CORS_HEADER_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String CORS_HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String CORS_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    private static final String CORS_HEADER_ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.defer(() -> {
            HttpHeaders headers = exchange.getResponse().getHeaders();
            
            deduplicateHeader(headers, CORS_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN);
            deduplicateHeader(headers, CORS_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS);
            deduplicateHeader(headers, CORS_HEADER_ACCESS_CONTROL_ALLOW_METHODS);
            deduplicateHeader(headers, CORS_HEADER_ACCESS_CONTROL_ALLOW_HEADERS);
            deduplicateHeader(headers, CORS_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS);
            deduplicateHeader(headers, CORS_HEADER_ACCESS_CONTROL_MAX_AGE);

            return Mono.empty();
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private void deduplicateHeader(HttpHeaders headers, String headerName) {
        if (headers.containsKey(headerName)) {
            headers.put(headerName, new ArrayList<>(headers.get(headerName)));
        }
    }
}
