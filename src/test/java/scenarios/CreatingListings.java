package scenarios;

import static org.assertj.core.api.Assertions.assertThat;

import ecg.move.sellermodel.imageapi.UploadImageResponse;
import java.util.Optional;
import org.example.ImageApiClient;
import org.example.Main;
import org.junit.jupiter.api.Test;

class CreatingListings {

    Main main = new Main();

    @Test
    void create_listing() {
        // given
        ImageApiClient imageApiClient = main.getImageApiClient();

        // when
        Optional<UploadImageResponse> uploadImageResponse = imageApiClient.postPicture(
            main.getConfig().getPartnerId(),
            "example-car-picture.jpg");

        // then
        assertThat(uploadImageResponse).isPresent();
        assertThat(uploadImageResponse.get().getImageBaseUrl()).startsWith("i.ebayimg.com/");
    }
}