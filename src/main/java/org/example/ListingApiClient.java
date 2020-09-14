package org.example;

import static java.lang.String.format;

import ecg.move.sellermodel.listing.Listing;
import java.util.Optional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public class ListingApiClient {

    private final AuthTokenClient authTokenClient;
    private String ownPartnerId;

    public ListingApiClient(AuthTokenClient authTokenClient,
                            String ownPartnerId) {
        this.authTokenClient = authTokenClient;
        this.ownPartnerId = ownPartnerId;
    }

    public Optional<Listing> getListing(String foreignId) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("https://sandbox.ecgmove.com/");
        WebTarget getListingByPartnerAndForeignId = webTarget.path("partners/{partnerName}/listings/{foreignId}");

        String bearerToken = authTokenClient.getJwt();
        Response response = getListingByPartnerAndForeignId
            .resolveTemplate("partnerName", ownPartnerId)
            .resolveTemplate("foreignId", foreignId)
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

}
