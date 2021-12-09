package com.codejago.labs.validator.controller;

import io.kubernetes.client.proto.Meta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
