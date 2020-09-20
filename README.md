[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# Reference Client for MoVe Listing API

This repository contains a reference client implementation for the MoVe Listing API. It uses a simple Java Jersey doing REST calls and aims to be as simple and readable as possible.  

## Intended Audience

If you are planning to integrate with eCG Motors Vertical (MoVe) and are interested in technical details around Listing API usage, this project is for you.

## What's in the Box

This repository contains 2 parts:

1) a reference API client written in basic Java with JaxRs/Jersey
1) a number of tests that cover [common usage scenarios](./src/test/java/scenarios)

To inspect the [implementation](./src/main/java/org/example/moveclient/), run `./gradlew clean compileJava` first to generate necessary client classes out of [MoVe's Listing API spec](https://api.ecgmove.com/docs/spec/listings). Feel free to create own clients based on this spec as it fits your needs.

## Running the Scenario Tests

To run the scenario tests on the other hand, you need **credentials** for the MoVe sandbox environment, and an assigned **`partnerId`**. If you haven't received those, please reach out to MoVe.
Once you have these details, set them in [config.properties](src/test/resources/config.properties).

To verify your settings, run `./gradlew clean test` and ensure that the tests are green. Next feel free to check the [implementation](./src/main/java/org/example/moveclient/) itself and see how the [common usage scenarios](./src/test/java/scenarios) can be implemented.
