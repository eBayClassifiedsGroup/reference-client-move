package org.example;

public class Main {

    private final ListingLifecycleApiClient listingLifecycleApiClient;
    private final ImageApiClient imageApiClient;
    private final ReferenceDataApiClient referenceDataApiClient;
    private final RuntimeConfig config;

    public Main() {
        config = new RuntimeConfig();
        AuthTokenClient authTokenClient = new AuthTokenClient(config);
        listingLifecycleApiClient = new ListingLifecycleApiClient(authTokenClient, config.getPartnerId(), config.getMoveBaseUrl());
        imageApiClient = new ImageApiClient(authTokenClient, config.getMoveBaseUrl());
        referenceDataApiClient = new ReferenceDataApiClient(config.getMoveBaseUrl());
    }

    public ListingLifecycleApiClient getListingLifecycleApiClient() {
        return listingLifecycleApiClient;
    }

    public ImageApiClient getImageApiClient() {
        return imageApiClient;
    }

    public RuntimeConfig getConfig() {
        return config;
    }

    public ReferenceDataApiClient getReferenceDataApiClient() {
        return referenceDataApiClient;
    }
}
