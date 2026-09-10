package com.hrms.gateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.gateway.dto.ApiResponse;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class GatewayResponseUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private GatewayResponseUtil() {
    }

    public static Mono<Void> writeErrorResponse(
            ServerWebExchange exchange,
            int status,
            String message
    ) {

        ApiResponse<String> response =
                new ApiResponse<>(status, message, null);

        try {
            String json = OBJECT_MAPPER.writeValueAsString(response);

            exchange.getResponse().setStatusCode(
                    org.springframework.http.HttpStatus.valueOf(status)
            );

            exchange.getResponse().getHeaders()
                    .setContentType(MediaType.APPLICATION_JSON);

            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(json.getBytes(StandardCharsets.UTF_8));

            return exchange.getResponse().writeWith(
                    Mono.just(buffer)
            );

        } catch (JsonProcessingException exception) {
            return Mono.error(exception);
        }
    }
}