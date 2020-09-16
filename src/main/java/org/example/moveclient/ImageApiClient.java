package org.example.moveclient;

import static java.lang.String.format;

import ecg.move.sellermodel.imageapi.UploadImageResponse;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

public class ImageApiClient {

    private final AuthTokenClient authTokenClient;
    private final String moveBaseUrl;

    public ImageApiClient(AuthTokenClient authTokenClient, String moveBaseUrl) {
        this.authTokenClient = authTokenClient;
        this.moveBaseUrl = moveBaseUrl;
    }

    public Optional<UploadImageResponse> postPicture(String partnerId, String resourceName) {
        Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
        WebTarget webTarget = client.target(moveBaseUrl);
        WebTarget target = webTarget
            .path("/partners/{partnerId}/images")
            .resolveTemplate("partnerId", partnerId);

        String bearerToken = authTokenClient.getJwt();
        Response response = target
            .request()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
            .accept(MediaType.APPLICATION_JSON)
            .post(Entity.entity(createFormData(resourceName), MediaType.MULTIPART_FORM_DATA));

        StatusType statusInfo = response.getStatusInfo();
        if (!statusInfo.getFamily().equals(Family.SUCCESSFUL)) {
            throw new IllegalStateException(
                format("Unexpected response while POSTing an image: %d, %s",
                    statusInfo.getStatusCode(),
                    statusInfo.getReasonPhrase())
            );
        }

        return Optional.ofNullable(response.readEntity(UploadImageResponse.class));
    }

    private FormDataMultiPart createFormData(String resourceName) {
        final FileDataBodyPart filePart;
        try (FormDataMultiPart formDataMultiPart = new FormDataMultiPart()) {
            filePart = new FileDataBodyPart(
                "file",
                new File(getClass().getClassLoader().getResource(resourceName).toURI()));
            return (FormDataMultiPart) formDataMultiPart.bodyPart(filePart);
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Problem preparing image for upload.", e);
        }
    }
}
