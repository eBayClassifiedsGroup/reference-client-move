//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
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

/*
    If your listings come with pictures (we recommend this), you will interact with the image API.
    This is a very straight forward process: you post a picture and receive back an "image base URL". These base URLs
    you then put into a listing under `listing.epsImages` as an array of baseUrls. Done!

    1) To create a listing, make sure to upload all images that come with it first, before putting the base URLs into the
    listing payload.
    2) If you later want to add additional picture, you again upload the pictures and (again) put the additional images
    into `listing.epsImages`.
    3) If you want to drop images from a listing, just remove the base URL from the listing and send the update.
 */
public class ImageApiClient {

    private final AuthTokenClient authTokenClient;
    private final String moveBaseUrl;
    private final String myPartnerId;

    public ImageApiClient(AuthTokenClient authTokenClient, String moveBaseUrl, String myPartnerId) {
        this.authTokenClient = authTokenClient;
        this.moveBaseUrl = moveBaseUrl;
        this.myPartnerId = myPartnerId;
    }

    public Optional<UploadImageResponse> postPicture(String resourceName) {
        Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
        WebTarget target = client.target(moveBaseUrl)
            .path("/partners/{partnerId}/images")
            .resolveTemplate("partnerId", myPartnerId);

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
