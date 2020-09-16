package org.example.moveclient;

import static java.lang.String.format;

import ecg.move.sellermodel.listing.RefMake;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public class ReferenceDataApiClient {

    private final String moveBaseUrl;

    public ReferenceDataApiClient(String moveBaseUrl) {
        this.moveBaseUrl = moveBaseUrl;
    }

    public List<RefMake> getMakesAndModels(String partnerId, String vehicleClassId) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(moveBaseUrl);
        WebTarget getListingByPartnerAndForeignId = webTarget
            .path("/refdata/partners/{partnerId}/vehicle-classes/{vehicleClassId}/makes-models")
            .resolveTemplate("partnerId", partnerId)
            .resolveTemplate("vehicleClassId", vehicleClassId);

        Response response = getListingByPartnerAndForeignId
            .request()
            .accept(MediaType.APPLICATION_JSON)
            .get();

        StatusType statusInfo = response.getStatusInfo();
        if (!statusInfo.getFamily().equals(Family.SUCCESSFUL)) {
            throw new IllegalStateException(format("Unexpected response while GETting list of supported makes/models: %s", statusInfo));
        }

        return response.readEntity(new GenericType<>() {});
    }

}
