package com.example.bob;

//import io.opentelemetry.api.OpenTelemetry;
//import io.opentelemetry.api.common.Attributes;
//import io.opentelemetry.api.trace.Span;
//import io.opentelemetry.api.trace.StatusCode;
//import io.opentelemetry.api.trace.Tracer;
//import io.opentelemetry.context.Context;
//import io.opentelemetry.context.Scope;
//import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
//import io.opentelemetry.sdk.OpenTelemetrySdk;
//import io.opentelemetry.sdk.resources.Resource;
//import io.opentelemetry.sdk.trace.SdkTracerProvider;
//import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
//import org.springframework.boot.CommandLineRunner;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author Sumit Deo (deosu@vmware.com)
 */

@Configuration
public class OTelConfig {//implements CommandLineRunner {
    private static final Log logger = LogFactory.getLog(DemoBobApplication.class);
    private final String INSTRUMENTATION_LIBRARY_NAME = "instrumentation-library-name";
    private final String INSTRUMENTATION_VERSION = "1.0.0";
    private final String SERVICE_NAME_KEY = "service.name";
    private final String APP_NAME_KEY = "application";

    @Value("${collector.endpoint}")
    private String collectorEndpoint;

    @Value("${spring.application.name}")
    private String springAppName;

    @Value("${otel.app.name}")
    private String otelAppName;


    // Adds a BatchSpanProcessor initialized with OtlpGrpcSpanExporter to the TracerSdkProvider.
    @Bean
    public Tracer tracer() {
        logger.info("initing open telemetry tracer");
        return openTelemetrySdk().getTracer(INSTRUMENTATION_LIBRARY_NAME, INSTRUMENTATION_VERSION);
    }

    @Bean
    public OpenTelemetrySdk openTelemetrySdk() {
        logger.info("initing open telemetry");
        OtlpGrpcSpanExporter spanExporter = getOtlpGrpcSpanExporter();
        BatchSpanProcessor spanProcessor = getBatchSpanProcessor(spanExporter);
        SdkTracerProvider tracerProvider = getSdkTracerProvider(spanProcessor);
        OpenTelemetrySdk openTelemetrySdk = getOpenTelemetrySdk(tracerProvider);
        Runtime.getRuntime().addShutdownHook(new Thread(tracerProvider::shutdown));
        return openTelemetrySdk;
    }

    public Resource resource() {
        logger.info("Using app name: " + otelAppName);
        logger.info("Using service name: " + springAppName);
        return Resource.getDefault().merge(Resource
                .create(Attributes.builder().put(SERVICE_NAME_KEY, springAppName).put(APP_NAME_KEY, otelAppName).build()));
    }

    //
    private OpenTelemetrySdk getOpenTelemetrySdk(SdkTracerProvider tracerProvider) {
        return OpenTelemetrySdk.builder().setTracerProvider(tracerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();
    }

    //
    private SdkTracerProvider getSdkTracerProvider(BatchSpanProcessor spanProcessor) {
        return SdkTracerProvider.builder().addSpanProcessor(spanProcessor)
                .setResource(resource()).build();
    }

    //
    private BatchSpanProcessor getBatchSpanProcessor(OtlpGrpcSpanExporter spanExporter) {
        return BatchSpanProcessor.builder(spanExporter)
                .setScheduleDelay(100, TimeUnit.MILLISECONDS).build();
    }

    @Bean
    public OtlpGrpcSpanExporter getOtlpGrpcSpanExporter() {
        logger.info("sending to enpoint: " + collectorEndpoint);
        return OtlpGrpcSpanExporter.builder()
                .setEndpoint(collectorEndpoint)
                .setTimeout(2, TimeUnit.SECONDS)
                .build();
    }

}
