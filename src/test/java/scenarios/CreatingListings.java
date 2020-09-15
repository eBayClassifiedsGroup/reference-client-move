package scenarios;

import static org.assertj.core.api.Assertions.assertThat;

import ecg.move.sellermodel.listing.Contact;
import ecg.move.sellermodel.listing.EpsImage;
import ecg.move.sellermodel.listing.Listing;
import ecg.move.sellermodel.listing.Listing.CategoryEnum;
import ecg.move.sellermodel.listing.Listing.ConditionEnum;
import ecg.move.sellermodel.listing.Listing.FuelEnum;
import ecg.move.sellermodel.listing.Listing.VehicleClassEnum;
import ecg.move.sellermodel.listing.Location;
import ecg.move.sellermodel.listing.PriceNg;
import ecg.move.sellermodel.listing.Prices;
import ecg.move.sellermodel.listing.RefMake;
import ecg.move.sellermodel.listing.Seller;
import ecg.move.sellermodel.listing.Seller.TypeEnum;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.example.ImageApiClient;
import org.example.Main;
import org.example.ReferenceDataApiClient;
import org.junit.jupiter.api.Test;

class CreatingListings {

    Main main = new Main();
    ReferenceDataApiClient referenceDataApiClient = main.getReferenceDataApiClient();
    ImageApiClient imageApiClient = main.getImageApiClient();

    @Test
    void create_listing() {
        EpsImage image = uploadListingPicture();
        RefMake carMake = pickMake();
        Listing listing = assembleListing(image, carMake);
        String listingId = postListing(listing);

        System.out.println("You created a listing on MoVe: " + listingId);
    }

    private String postListing(Listing listing) {

        // Listings usually have an id in the system of the caller, e.g. assigned by the local database.
        // As the caller, you have to provide your own listing id, which on the MoVe side will be called
        // "foreignId of the partner".
        String madeUpListingId = UUID.randomUUID().toString();
        String moveListingId = main.getListingLifecycleApiClient().publishListing(listing, madeUpListingId);

        assertThat(moveListingId).isNotBlank();
        return moveListingId;
    }

    private Listing assembleListing(EpsImage image, RefMake carMake) {
        // the following listing contains only the most basic, mandatory fields (plus few extras)
        Listing listing = new Listing();
        listing.setEpsImages(List.of(image));
        listing.setTitle("Awesome Car For Sale!");
        listing.setHtmlDescription("<h1>Check this out!</h1><p>And here is some description</p>");
        listing.setSeller(exampleFsboSeller());
        listing.setPrices(examplePrices());
        listing.setMake(carMake.getId());
        listing.setFuel(FuelEnum.PETROL_PREMIUM); // fuel is defined as part of the API spec
        listing.setVehicleClass(VehicleClassEnum.CAR);
        listing.setCategory(CategoryEnum.LIMOUSINE);
        listing.setCondition(ConditionEnum.NEW);
        listing.setContact(exampleContact());
        listing.setLocation(exampleLocation());

        // feel free to extend this and set more details or a different vehicle class.
        // Check the API spec and the response for validation errors. In some cases, the reference data API is
        // able to provide allowed values ranges.

        return listing;
    }

    private Contact exampleContact() {
        Contact contact = new Contact();
        contact.setLanguages(List.of("en"));
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setEmail("john@doe.com");

        return contact;
    }

    private RefMake pickMake() {
        // see what makes/models are allowed - this is subject to changes so make sure to validate often!
        List<RefMake> allModels = referenceDataApiClient.getMakesAndModels(main.getConfig().getPartnerId(), "Car");

        assertThat(allModels)
            .describedAs("Move should return a (large) list of supported makes and models.")
            .isNotEmpty();

        // in reality you do not randomize this of course
        return allModels.get(new Random().nextInt(allModels.size()));
    }

    private EpsImage uploadListingPicture() {
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
        price.setAmount("10000");

        Prices prices = new Prices();
        prices.setConsumerPrice(price);

        return prices;
    }

    private Location exampleLocation() {
        Location location = new Location();
        location.setCity("Toronto");
        location.setCountry("CA");
        location.setStreet("10 Main St");
        location.setZip("H0H 0H0");

        return location;
    }
}