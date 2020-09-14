package scenarios;

import static org.assertj.core.api.Assertions.assertThat;

import ecg.move.sellermodel.listing.EpsImage;
import ecg.move.sellermodel.listing.Listing;
import ecg.move.sellermodel.listing.PriceNg;
import ecg.move.sellermodel.listing.Prices;
import ecg.move.sellermodel.listing.RefMake;
import ecg.move.sellermodel.listing.Seller;
import ecg.move.sellermodel.listing.Seller.TypeEnum;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import jersey.repackaged.com.google.common.base.MoreObjects.ToStringHelper;
import org.example.ImageApiClient;
import org.example.Main;
import org.example.ReferenceDataApiClient;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ToStringBuilder;

class CreatingListings {

    Main main = new Main();

    @Test
    void create_listing() {
        EpsImage image = uploadListingPicture();

        RefMake carMake = pickMake();

        Listing listing = assembleListing(image, carMake);

    }

    private Listing assembleListing(EpsImage image, RefMake carMake) {
        Listing listing = new Listing();
        listing.setEpsImages(List.of(image));
        listing.setTitle("Awesome Car For Sale!");
        listing.setHtmlDescription("<h1>Check this out!</h1><p>And here is some description</p>");
        listing.setSeller(exampleFsboSeller());
        listing.setPrices(examplePrices());
        listing.setMake(carMake.getId());
        return listing;
    }

    private RefMake pickMake() {
        ReferenceDataApiClient referenceDataApiClient = main.getReferenceDataApiClient();
        // see what makes/models are allowed - this is subject to changes so make sure to validate often!
        List<RefMake> allModels = referenceDataApiClient.getMakesAndModels("kijiji-autos-ca", "Car");
        if (allModels.isEmpty()) {
            throw new IllegalStateException("List of allowed makes/models is empty, cannot pick a make.");
        }
        // in reality you do not randomize this of course
        return allModels.get(new Random().nextInt(allModels.size()));
    }

    private EpsImage uploadListingPicture() {
        ImageApiClient imageApiClient = main.getImageApiClient();

        return imageApiClient.postPicture(
            main.getConfig().getPartnerId(),
            "example-car-picture.jpg").map(rsp -> {
            EpsImage epsImage = new EpsImage();
            epsImage.setBaseUrl(rsp.getImageBaseUrl());
            return epsImage;
        }).orElseThrow();
    }

    private Seller exampleFsboSeller() {
        Seller seller = new Seller();
        seller.setType(TypeEnum.FSBO); // watch out: if you provide a dealer listing, the foreignId must be known, see TODO article link
        seller.setForeignId(UUID.randomUUID().toString());
        return seller;
    }

    private Prices examplePrices() {
        PriceNg price = new PriceNg();
        price.setCurrency("CAD");
        price.setIsNet(false);
        price.setNegotiable(false);

        Prices prices = new Prices();
        prices.setConsumerPrice(price);

        return prices;
    }
}