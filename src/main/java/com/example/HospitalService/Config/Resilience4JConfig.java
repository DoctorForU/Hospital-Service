package com.example.HospitalService.Config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

// Resilience4J 설정을 위한 Configuration 클래스
@Configuration
public class Resilience4JConfig {

    // 글로벌 커스텀 설정 Bean
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
        // Circuit Breaker의 글로벌 설정
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(4)  // 실패율 임계값을 4%로 설정
                .waitDurationInOpenState(Duration.ofMillis(1000))  // Circuit Breaker가 열려 있는 상태의 지속 시간 설정
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)  // 슬라이딩 윈도우 타입을 COUNT_BASED로 설정
                .slidingWindowSize(2)  // 슬라이딩 윈도우 크기를 2로 설정
                .build();

        // Time Limiter의 글로벌 설정
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))  // 타임아웃 기간을 4초로 설정
                .build();

        // 설정된 Circuit Breaker와 Time Limiter를 Resilience4JConfigBuilder에 적용
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build()
        );
    }

    // 특정 Circuit Breaker 'circuitBreaker1'에 대한 커스텀 설정 Bean
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> specificCustomConfiguration1() {
        // Circuit Breaker의 특정 설정
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(6)  // 실패율 임계값을 6%로 설정
                .waitDurationInOpenState(Duration.ofMillis(1000))  // Circuit Breaker가 열려 있는 상태의 지속 시간 설정
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)  // 슬라이딩 윈도우 타입을 COUNT_BASED로 설정
                .slidingWindowSize(3)  // 슬라이딩 윈도우 크기를 3으로 설정
                .build();

        // Time Limiter의 특정 설정
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))  // 타임아웃 기간을 4초로 설정
                .build();

        // 설정된 Circuit Breaker와 Time Limiter를 특정 Circuit Breaker 'circuitBreaker1'에 적용
        return factory -> factory.configure(builder -> builder.circuitBreakerConfig(circuitBreakerConfig)
                .timeLimiterConfig(timeLimiterConfig).build(), "circuitBreaker1");
    }

    // 특정 Circuit Breaker 'circuitBreaker2'에 대한 커스텀 설정 Bean
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> specificCustomConfiguration2() {
        // Circuit Breaker의 특정 설정
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(8)  // 실패율 임계값을 8%로 설정
                .waitDurationInOpenState(Duration.ofMillis(1000))  // Circuit Breaker가 열려 있는 상태의 지속 시간 설정
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)  // 슬라이딩 윈도우 타입을 COUNT_BASED로 설정
                .slidingWindowSize(4)  // 슬라이딩 윈도우 크기를 4로 설정
                .build();

        // Time Limiter의 특정 설정
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))  // 타임아웃 기간을 4초로 설정
                .build();

        // 설정된 Circuit Breaker와 Time Limiter를 특정 Circuit Breaker 'circuitBreaker2'에 적용
        return factory -> factory.configure(builder -> builder.circuitBreakerConfig(circuitBreakerConfig)
                        .timeLimiterConfig(timeLimiterConfig).build(),
                "circuitBreaker2");
    }
}