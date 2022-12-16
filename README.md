This repo provides a set of sample applications to test out OTEL tracing on a TAS environment. The
apps communicate with each other via a rest call. Tracing is propagated via w3c headers.


Libraries
* Open Telemetry SDK
* Spring Boot
* Spring Micrometer Tracing

Prerequisites
Make sure to turn on w3c header propagation in the gorouter by setting the following property
in the BOSH manifest for the TAS deployment:

```
router:
  tracing:
    enable_w3c: true
    enable_zipkin: false
```


Installing.
1. Update `otel.app.name` in the `application.properties` file to give you overall application a unique name. 
2. Make sure to target an org and space to push your apps to.
3. Run `./push_apps`