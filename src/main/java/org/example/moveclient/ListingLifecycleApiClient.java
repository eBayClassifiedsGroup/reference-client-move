//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.moveclient;

import static java.lang.String.format;

import ecg.move.sellermodel.listing.CreatedAdResponse;
import ecg.move.sellermodel.listing.Listing;
import java.util.Optional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

/*
    The Listing API is the main API you likely will interact with. It offers a number of resources allowing you do basic
    CRUD operations. It furthermore supports basic state transitions like "pause a listing", "make a listing visible"
    and so on.
    Whenever the content of a listing is affected, the listing is subject to validation, which ensures that the content
    provided by the caller is sufficient and understandable. Validation errors result in a 400 response with errors
    description(s) in the response body.
 */
public class ListingLifecycleApiClient {

    private final String bearerToken;
    private final String myPartnerId;
    private final String moveBaseUrl;

    public ListingLifecycleApiClient(
        String bearerToken,
        String myPartnerId,
        String moveBaseUrl) {
        this.bearerToken = bearerToken;

        this.myPartnerId = myPartnerId;
        this.moveBaseUrl = moveBaseUrl;
    }

    public Optional<Listing> getListing(String foreignId) {
        WebTarget webTarget = createWebtarget()
            .path("partners/{partnerId}/listings/{foreignId}")
            .resolveTemplate("partnerId", myPartnerId)
            .resolveTemplate("foreignId", foreignId);

        Response response = webTarget
            .request()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
            .accept(MediaType.APPLICATION_JSON)
            .get();

        StatusType statusInfo = response.getStatusInfo();
        if (!statusInfo.getFamily().equals(Family.SUCCESSFUL) && statusInfo.getStatusCode() != 404) {
            throw new IllegalStateException(format("Unexpected response while GETting a listing: %s", statusInfo));
        }

        return Optional.ofNullable(response.readEntity(Listing.class));
    }

    public String publishListing(Listing listing, String myListingId) {
        WebTarget webTarget = createWebtarget()
            .path("partners/{partnerId}/listings/{myListingId}")
            .resolveTemplate("partnerId", myPartnerId)
            .resolveTemplate("myListingId", myListingId);

        Response response = webTarget
            .request()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
            .accept(MediaType.APPLICATION_JSON)
            .put(Entity.entity(listing, MediaType.APPLICATION_JSON));

        StatusType statusInfo = response.getStatusInfo();
        if (!statusInfo.getFamily().equals(Family.SUCCESSFUL)) {
            throw new IllegalStateException(format("Unexpected response while PUTting a listing: '%s', response: '%s'", statusInfo, response.readEntity(String.class)));
        }

        return Optional.ofNullable(response.readEntity(CreatedAdResponse.class))
            .map(CreatedAdResponse::getMoveListingId)
            .orElseThrow();
    }

    private WebTarget createWebtarget() {
        Client client = ClientBuilder.newClient();
        return client.target(moveBaseUrl);
    }

}
