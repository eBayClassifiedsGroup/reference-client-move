package org.example.moveclient;

import static java.lang.String.format;

import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

public class AuthTokenClient {

    private final String userName;
    private final String password;
    private final String moveBaseUrl;

    public AuthTokenClient(RuntimeConfig config) {
        this.userName = config.getUserName();
        this.password = config.getPassword();
        this.moveBaseUrl = config.getMoveBaseUrl();
    }

    public String getJwt() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(moveBaseUrl);
        WebTarget getListingByPartnerAndForeignId = webTarget.path("auth/realms/move/protocol/openid-connect/token");

        Builder request = getListingByPartnerAndForeignId
            .request(MediaType.APPLICATION_FORM_URLENCODED);

        MultivaluedStringMap map = new MultivaluedStringMap();
        map.putSingle("grant_type", "client_credentials");
        map.putSingle("client_id", userName);
        map.putSingle("client_secret", password);

        Response put = request.post(Entity.form(map));

        StatusType httpStatus = put.getStatusInfo();
        if (httpStatus.getFamily().equals(Family.SUCCESSFUL)) {
            Map<String, String> jwt = put.readEntity(new GenericType<>() {});
            if (jwt == null || !jwt.containsKey("access_token")) {
                throw new IllegalStateException("Failed to extract JWT while performing auth.");
            }
            return jwt.get("access_token");
        } else {
            throw new IllegalStateException(format("Unexpected response while performing auth: %s", put.getStatus()));
        }
    }

}
