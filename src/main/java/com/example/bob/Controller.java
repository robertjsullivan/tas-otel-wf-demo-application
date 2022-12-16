package com.example.bob;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import java.util.Iterator;

@RestController
public class Controller {
    private static final Log logger = LogFactory.getLog(DemoBobApplication.class);

    @Autowired
    private Tracer tracer;

    @Autowired
    private OpenTelemetrySdk openTelemetrySdk;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.service.address}")
    private String backendServiceAddress;

    @Autowired
    private MyUserService myUserService;

    @RequestMapping("/frontpage")
    String frontend() {
        logger.info("home() has been called");

        myUserService.someService2();
        String fooResourceUrl
                = "http://" + backendServiceAddress + "/backendservice";
        ResponseEntity<String> response
                = restTemplate.getForEntity(fooResourceUrl, String.class);
        logger.info("received response: " + response);
        return "Hello World! from the frontpage";
    }

    @RequestMapping("/backendservice")
    String backend(WebRequest request) {
        logger.info("backend() has been called");
        for (Iterator i = request.getHeaderNames(); i.hasNext(); ) {
            String header = (String) i.next();
            logger.info("got header: " + header + " value: " + request.getHeader(header));
        }
        myUserService.userName("bob");
        myUserService.someService2();
        return "Hello from the backend service!";
    }

}
