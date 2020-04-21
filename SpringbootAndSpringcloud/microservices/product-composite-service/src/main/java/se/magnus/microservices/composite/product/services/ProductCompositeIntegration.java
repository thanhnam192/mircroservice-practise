package se.magnus.microservices.composite.product.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.magnus.api.core.product.Product;
import se.magnus.api.core.product.ProductService;
import se.magnus.api.core.recommendation.Recommendation;
import se.magnus.api.core.recommendation.RecommendationService;
import se.magnus.api.core.review.Review;
import se.magnus.api.core.review.ReviewService;
import se.magnus.api.event.Event;
import se.magnus.util.exceptions.InvalidInputException;
import se.magnus.util.exceptions.NotFoundException;
import se.magnus.util.http.HttpErrorInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static reactor.core.publisher.Flux.empty;
import static se.magnus.api.event.Event.Type.CREATE;
import static se.magnus.api.event.Event.Type.DELETE;

@EnableBinding(ProductCompositeIntegration.MessageSources.class)
@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;
    private final String productServiceHost;
    private final String recommendationServiceHost;
    private final String reviewServiceHost;
    private final WebClient webClient;
    private MessageSources messageSources;

    public interface MessageSources {

        String OUTPUT_PRODUCTS = "output-products";
        String OUTPUT_RECOMMENDATIONS = "output-recommendations";
        String OUTPUT_REVIEWS = "output-reviews";

        @Output(OUTPUT_PRODUCTS)
        MessageChannel outputProducts();

        @Output(OUTPUT_RECOMMENDATIONS)
        MessageChannel outputRecommendations();

        @Output(OUTPUT_REVIEWS)
        MessageChannel outputReviews();
    }

    @Autowired
    public ProductCompositeIntegration(
            MessageSources messageSources,
            WebClient.Builder webClient,
        RestTemplate restTemplate,
        ObjectMapper mapper,

        @Value("${app.product-service.host}") String productServiceHost,
        @Value("${app.product-service.port}") int    productServicePort,

        @Value("${app.recommendation-service.host}") String recommendationServiceHost,
        @Value("${app.recommendation-service.port}") int    recommendationServicePort,

        @Value("${app.review-service.host}") String reviewServiceHost,
        @Value("${app.review-service.port}") int    reviewServicePort
    ) {

        this.messageSources = messageSources;
        this.webClient = webClient.build();
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        productServiceUrl        = "http://" + productServiceHost + ":" + productServicePort + "/product";
        recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation";
        reviewServiceUrl         = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review";

        this.productServiceHost        = "http://" + productServiceHost + ":" + productServicePort;
        this.recommendationServiceHost = "http://" + recommendationServiceHost + ":" + recommendationServicePort ;
        this.reviewServiceHost        = "http://" + reviewServiceHost + ":" + reviewServicePort ;
    }

    @Override
    public Product createProduct(Product body) {
        messageSources.outputProducts().send(MessageBuilder.withPayload(new Event(CREATE, body.getProductId(), body)).build());
        return body;

//        try {
//            String url = productServiceUrl;
//            LOG.debug("Will post a new product to URL: {}", url);
//
//            Product product = restTemplate.postForObject(url, body, Product.class);
//            LOG.debug("Created a product with id: {}", product.getProductId());
//
//            return product;
//
//        } catch (HttpClientErrorException ex) {
//            throw handleHttpClientException(ex);
//        }
    }

    @Override
    public Mono<Product> getProduct(int productId) {
        String url = productServiceUrl + "/" + productId;
        LOG.debug("Will call the getProduct API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToMono(Product.class).log()
                .onErrorMap(HttpClientErrorException.class, ex -> handleHttpClientException(ex));


    }

    @Override
    public void deleteProduct(int productId) {

        messageSources.outputProducts()
                .send(MessageBuilder.withPayload(new Event(Event.Type.DELETE, productId, null)).build());
//        try {
//            String url = productServiceUrl + "/" + productId;
//            LOG.debug("Will call the deleteProduct API on URL: {}", url);
//
//            restTemplate.delete(url);
//
//        } catch (HttpClientErrorException ex) {
//            throw handleHttpClientException(ex);
//        }
    }

    @Override
    public Recommendation createRecommendation(Recommendation body) {
        messageSources.outputRecommendations().send(MessageBuilder.withPayload(new Event(CREATE, body.getProductId(), body)).build());
        return body;

//        try {
//            String url = recommendationServiceUrl;
//            LOG.debug("Will post a new recommendation to URL: {}", url);
//
//            Recommendation recommendation = restTemplate.postForObject(url, body, Recommendation.class);
//            LOG.debug("Created a recommendation with id: {}", recommendation.getProductId());
//
//            return recommendation;
//
//        } catch (HttpClientErrorException ex) {
//            throw handleHttpClientException(ex);
//        }
    }

    @Override
    public Flux<Recommendation> getRecommendations(int productId) {
            String url = recommendationServiceUrl + "?productId=" + productId;
            LOG.debug("Will call the getRecommendations API on URL: {}", url);
            return webClient.get().uri(url).retrieve().bodyToFlux(Recommendation.class).log().onErrorResume(error -> empty());
    }

    @Override
    public void deleteRecommendations(int productId) {
        messageSources.outputRecommendations().send(MessageBuilder.withPayload(new Event(DELETE, productId, null)).build());

//        try {
//            String url = recommendationServiceUrl + "?productId=" + productId;
//            LOG.debug("Will call the deleteRecommendations API on URL: {}", url);
//
//            restTemplate.delete(url);
//
//        } catch (HttpClientErrorException ex) {
//            throw handleHttpClientException(ex);
//        }
    }

    @Override
    public Review createReview(Review body) {
        messageSources.outputReviews().send(MessageBuilder.withPayload(new Event(CREATE, body.getProductId(), body)).build());
        return body;

//        try {
//            String url = reviewServiceUrl;
//            LOG.debug("Will post a new review to URL: {}", url);
//
//            Review review = restTemplate.postForObject(url, body, Review.class);
//            LOG.debug("Created a review with id: {}", review.getProductId());
//
//            return review;
//
//        } catch (HttpClientErrorException ex) {
//            throw handleHttpClientException(ex);
//        }
    }

    @Override
    public Flux<Review> getReviews(int productId) {
            String url = reviewServiceUrl + "?productId=" + productId;

            LOG.debug("Will call the getReviews API on URL: {}", url);

            return webClient.get().uri(url).retrieve().bodyToFlux(Review.class).log().onErrorResume(error ->  empty());
    }

    @Override
    public void deleteReviews(int productId) {
        messageSources.outputReviews().send(MessageBuilder.withPayload(new Event(DELETE, productId, null)).build());

//        try {
//            String url = reviewServiceUrl + "?productId=" + productId;
//            LOG.debug("Will call the deleteReviews API on URL: {}", url);
//
//            restTemplate.delete(url);
//
//        } catch (HttpClientErrorException ex) {
//            throw handleHttpClientException(ex);
//        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        switch (ex.getStatusCode()) {

        case NOT_FOUND:
            return new NotFoundException(getErrorMessage(ex));

        case UNPROCESSABLE_ENTITY :
            return new InvalidInputException(getErrorMessage(ex));

        default:
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            return ex;
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
    public Mono<Health> getProductHealth() {
        return getHealth(productServiceHost );
    }
    public Mono<Health> getRecommendationHealth() {
        return getHealth(recommendationServiceHost);
    }
    public Mono<Health> getReviewHealth() {
        return getHealth(reviewServiceHost);
    }
    private Mono<Health> getHealth(String url) {
        url += "/actuator/health";
        LOG.debug("Will call the Health API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToMono(String.class)
                .map(s -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new
                        Health.Builder().down(ex).build()))
                .log();
    }
}
