package org.example;

public class Main {

    private final ListingApiClient listingApiClient;
    private final ImageApiClient imageApiClient;
    private final RuntimeConfig config;

    public Main() {
        config = new RuntimeConfig();
        AuthTokenClient authTokenClient = new AuthTokenClient(config);
        listingApiClient = new ListingApiClient(authTokenClient, config.getPartnerId(), config.getMoveBaseUrl());
        imageApiClient = new ImageApiClient(authTokenClient, config.getMoveBaseUrl());
    }

    public ListingApiClient getListingApiClient() {
        return listingApiClient;
    }

    public ImageApiClient getImageApiClient() {
        return imageApiClient;
    }

    public RuntimeConfig getConfig() {
        return config;
    }
}
