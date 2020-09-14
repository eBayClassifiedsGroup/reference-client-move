package scenarios;

import static org.assertj.core.api.Assertions.assertThat;

import ecg.move.sellermodel.listing.Listing;
import java.util.Optional;
import java.util.UUID;
import org.example.ListingApiClient;
import org.example.Main;
import org.junit.jupiter.api.Test;

class FetchingListings {

    Main main = new Main();

    @Test
    void reading_nonexistent_listing_yields_404() {
        String madeUpForeignId = UUID.randomUUID().toString();
        ListingApiClient listingApiClient = main.getListingApiClient();

        Optional<Listing> listing = listingApiClient.getListing(madeUpForeignId);

        assertThat(listing).isEmpty();
    }
}