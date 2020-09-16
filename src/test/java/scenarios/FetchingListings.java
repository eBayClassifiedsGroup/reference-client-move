package scenarios;

import static org.assertj.core.api.Assertions.assertThat;

import ecg.move.sellermodel.listing.Listing;
import java.util.Optional;
import java.util.UUID;
import org.example.moveclient.ListingLifecycleApiClient;
import org.example.moveclient.Main;
import org.junit.jupiter.api.Test;

class FetchingListings {

    Main main = new Main();

    @Test
    void reading_nonexistent_listing_yields_404() {
        String madeUpForeignId = UUID.randomUUID().toString();
        ListingLifecycleApiClient listingLifecycleApiClient = main.getListingLifecycleApiClient();

        Optional<Listing> listing = listingLifecycleApiClient.getListing(madeUpForeignId);

        assertThat(listing).isEmpty();
    }
}