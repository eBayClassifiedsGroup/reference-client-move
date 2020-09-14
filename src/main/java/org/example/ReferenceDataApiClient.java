package org.example;

import static java.lang.String.format;

import ecg.move.sellermodel.listing.Listing;
import ecg.move.sellermodel.listing.RefItem;
import ecg.move.sellermodel.listing.RefMake;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public class ReferenceDataApiClient {

    private final String moveBaseUrl;

    public ReferenceDataApiClient(String moveBaseUrl) {
        this.moveBaseUrl = moveBaseUrl;
    }

    public List<RefMake> getMakesAndModels(String marketplaceId, String vehicleClassId) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(moveBaseUrl);
        WebTarget getListingByPartnerAndForeignId = webTarget
            .path("/refdata/marketplaces/{marketplaceId}/vehicle-classes/{vehicleClassId}/makes-models")
            .resolveTemplate("marketplaceId", marketplaceId)
            .resolveTemplate("vehicleClassId", vehicleClassId);

        Response response = getListingByPartnerAndForeignId
            .request()
            .accept(MediaType.APPLICATION_JSON)
            .get();

        StatusType statusInfo = response.getStatusInfo();
        if (!statusInfo.getFamily().equals(Family.SUCCESSFUL)) {
            throw new IllegalStateException(format("Unexpected response while GETting a listing: %s", statusInfo));
        }

        return response.readEntity(new GenericType<>() {});
    }

}
