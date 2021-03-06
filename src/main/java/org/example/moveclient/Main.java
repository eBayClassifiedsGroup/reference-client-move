//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.moveclient;

public class Main {

    private final ListingLifecycleApiClient listingLifecycleApiClient;
    private final ImageApiClient imageApiClient;
    private final ReferenceDataApiClient referenceDataApiClient;
    private final DealerCatalogApiClient dealerCatalogApiClient;
    private final RuntimeConfig config;

    public Main() {
        config = new RuntimeConfig();
        AuthTokenClient authTokenClient = new AuthTokenClient(config);
        String bearerToken = authTokenClient.getJwt();

        listingLifecycleApiClient = new ListingLifecycleApiClient(bearerToken, config.getPartnerId(), config.getMoveBaseUrl());
        imageApiClient = new ImageApiClient(authTokenClient, config.getMoveBaseUrl(), config.getPartnerId());
        referenceDataApiClient = new ReferenceDataApiClient(config.getMoveBaseUrl());
        dealerCatalogApiClient = new DealerCatalogApiClient(bearerToken, config.getMoveBaseUrl());
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

    public DealerCatalogApiClient getDealerCatalogApiClient() {
        return dealerCatalogApiClient;
    }
}
