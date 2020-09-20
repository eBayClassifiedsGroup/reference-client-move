//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package scenarios;

import static org.assertj.core.api.Assertions.assertThat;

import ecg.move.sellermodel.dealer.DealerOverviewResponse;
import java.util.List;
import org.example.moveclient.DealerCatalogApiClient;
import org.example.moveclient.Main;
import org.junit.jupiter.api.Test;

class CreatingProfessionalSellerListing {

    Main main = new Main();
    DealerCatalogApiClient dealerCatalogApiClient = main.getDealerCatalogApiClient();


    @Test
    void create_listing() {
        List<DealerOverviewResponse> dealerCatalog = dealerCatalogApiClient.getDealerCatalog();

        assertThat(dealerCatalog).isNotEmpty();
    }

}