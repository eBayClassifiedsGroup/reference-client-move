//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.moveclient;

import static java.lang.String.format;

import ecg.move.sellermodel.dealer.DealerOverviewResponse;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public class DealerCatalogApiClient {

    private final String bearerToken;
    private final String moveBaseUrl;

    public DealerCatalogApiClient(
        String bearerToken,
        String moveBaseUrl) {
        this.bearerToken = bearerToken;
        this.moveBaseUrl = moveBaseUrl;
    }

    public List<DealerOverviewResponse> getDealerCatalog() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(moveBaseUrl)
            .path("/v2/dealers")
            .queryParam("page", 0)
            .queryParam("pageSize", 1000); // using hard coded page sizes here for simplicity

        Response response = webTarget
            .request()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
            .accept(MediaType.APPLICATION_JSON)
            .get();

        StatusType statusInfo = response.getStatusInfo();
        if (!statusInfo.getFamily().equals(Family.SUCCESSFUL)) {
            throw new IllegalStateException(format("Unexpected response while GETting list of known dealers: %s, reason: %s", statusInfo, response.readEntity(String.class)));
        }

        List<DealerOverviewResponse> dealerOverviewResponses = response.readEntity(new GenericType<>() {});
        return dealerOverviewResponses;
    }

}
