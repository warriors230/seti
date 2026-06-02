package com.acme.pedidos.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

	@Bean
	public WebClient webClient() throws SSLException {
	    SslContext sslContext = SslContextBuilder.forClient()
	            .trustManager(InsecureTrustManagerFactory.INSTANCE)
	            .build();

	    HttpClient httpClient = HttpClient.create()
	            .secure(spec -> spec.sslContext(sslContext));

	    return WebClient.builder()
	            .clientConnector(new ReactorClientHttpConnector(httpClient))
	            .codecs(configurer -> configurer
	                    .defaultCodecs()
	                    .maxInMemorySize(1024 * 1024))
	            .build();
	}

    @Bean
    public org.springframework.oxm.jaxb.Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.acme.pedidos.soap.schema");
        return marshaller;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ACME Pedidos API")
                        .description("API REST para el ciclo de abastecimiento ACME. " +
                                "Transforma peticiones JSON a SOAP/XML y viceversa.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ACME - Tienda Carrera 70")));
    }
}
