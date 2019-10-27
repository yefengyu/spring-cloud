package com.yefengyu.gateway.local;


import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

@Component
public class ResponseBodyGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {

        return new ModifyResponseGatewayFilter();
    }


    public class ModifyResponseGatewayFilter implements GatewayFilter, Ordered {

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            return chain.filter(exchange.mutate().response(decorate(exchange)).build());
        }

        @SuppressWarnings("unchecked")
        ServerHttpResponse decorate(ServerWebExchange exchange) {
            return new ServerHttpResponseDecorator(exchange.getResponse()) {

                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                    Class inClass = String.class;
                    Class outClass = String.class;

                    String originalResponseContentType = exchange
                            .getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    HttpHeaders httpHeaders = new HttpHeaders();

                    httpHeaders.add(HttpHeaders.CONTENT_TYPE,
                            originalResponseContentType);

                    ClientResponse clientResponse = ClientResponse
                            .create(exchange.getResponse().getStatusCode())
                            .headers(headers -> headers.putAll(httpHeaders))
                            .body(Flux.from(body)).build();

                    Mono modifiedBody = clientResponse.bodyToMono(inClass)
                            .flatMap(originalBody -> {
                                //TODO:此次可以对返回的body进行操作
                                System.out.println(originalBody);
                                return Mono.just(originalBody);
                            });

                    BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody,
                            outClass);
                    CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(
                            exchange, exchange.getResponse().getHeaders());
                    return bodyInserter.insert(outputMessage, new BodyInserterContext())
                            .then(Mono.defer(() -> {
                                Flux<DataBuffer> messageBody = outputMessage.getBody();
                                HttpHeaders headers = getDelegate().getHeaders();
                                if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING)) {
                                    messageBody = messageBody.doOnNext(data -> headers
                                            .setContentLength(data.readableByteCount()));
                                }
                                return getDelegate().writeWith(messageBody);
                            }));
                }

                @Override
                public Mono<Void> writeAndFlushWith(
                        Publisher<? extends Publisher<? extends DataBuffer>> body) {
                    return writeWith(Flux.from(body).flatMapSequential(p -> p));
                }
            };
        }

        @Override
        public int getOrder() {
            return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
        }
    }
}