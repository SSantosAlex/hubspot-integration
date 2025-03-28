package com.hubspot.service;

import com.hubspot.resources.request.ContactRequest;
import com.hubspot.resources.response.ContactResponse;

public interface ContactService {

    ContactResponse createContact(ContactRequest request);
}
