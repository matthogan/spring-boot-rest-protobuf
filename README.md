# spring-boot-rest-protobuf

Using protobuf messages as request and response bodies. This contains a kubernetes admission
 controller and uses the io.kubernetes:client-java-extended dependendency
for the request/response definitions, which are provided as generated protobuf messages in a
transitive dependency.

Spring boot fails with this HTTP/415 error if the following configuration is missing:

```json
{"timestamp":"2020-09-09T02:49:01.823+00:00","path":"/pod/valid","status":415,"error":"Unsupported Media Type","requestId":"bedd0a5f-1, L:/127.0.0.1:8080 - R:/127.0.0.1:63443"}
```

Add the pom dependencies:

```xml
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
    <version>4.0.0-rc-2</version>
</dependency>
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java-util</artifactId>
    <version>4.0.0-rc-2</version>
</dependency>
<dependency>
    <groupId>com.googlecode.protobuf-java-format</groupId>
    <artifactId>protobuf-java-format</artifactId>
    <version>1.4</version>
</dependency>
```

Define a HttpMessageConverter bean that will automagically convert the http to the protobuf message:

```java
@Bean
ProtobufHttpMessageConverter protobufHttpMessageConverter() {
    return new ProtobufHttpMessageConverter();
}
```

And define the protobuf messages in the rest controller:

```java

import static io.kubernetes.client.proto.V1beta1Admission.AdmissionResponse;
import static io.kubernetes.client.proto.V1beta1Admission.AdmissionReview;

@Slf4j
@RestController
@RequestMapping("/pod")
class ValidatorController {

	@PostMapping(value = "/valid")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public AdmissionReview validate(@RequestBody AdmissionReview admissionReview) {
		log.info("AdmissionReview: {}", admissionReview);
		return AdmissionReview.newBuilder(admissionReview)
				.setResponse(AdmissionResponse.newBuilder()
						.setAllowed(false)
						.setStatus(Meta.Status.newBuilder().setStatus("FOOKED").build())
						.build())
				.build();
	}
}
```

## Ref

* https://spring.io/blog/2015/03/22/using-google-protocol-buffers-with-spring-mvc-based-rest-services
* https://developers.google.com/protocol-buffers/docs/javatutorial
* https://www.baeldung.com/spring-rest-api-with-protocol-buffers
