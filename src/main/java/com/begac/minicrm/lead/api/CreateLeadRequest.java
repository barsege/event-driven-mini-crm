package com.begac.minicrm.lead.api;

public record CreateLeadRequest(
        String firstName,
        String lastName,
        String email,
        String phone
) {}
