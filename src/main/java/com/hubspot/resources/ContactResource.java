package com.hubspot.resources;

import com.hubspot.resources.request.ContactRequest;
import com.hubspot.resources.response.ContactResponse;
import com.hubspot.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
@Validated
@Slf4j
public class ContactResource {
    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponse> createContact(@Valid @RequestBody ContactRequest request) {

        ContactResponse response =  contactService.createContact(request);

        log.info("c={}, m=createContact response={}",getClass().getSimpleName(), response);

        return ResponseEntity.ok(response);
    }
}
