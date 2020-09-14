package org.example;

public class Main {

    private final ListingApiClient listingApiClient;

    public Main() {
        RuntimeConfig config = new RuntimeConfig();
        AuthTokenClient authTokenClient = new AuthTokenClient(config);
        listingApiClient = new ListingApiClient(authTokenClient, config.getPartnerId());
    }

    public ListingApiClient getListingApiClient() {
        return listingApiClient;
    }

}
