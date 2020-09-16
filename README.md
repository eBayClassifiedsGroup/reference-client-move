[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# Reference Client for MoVe Listing API

This repository contains a reference client implementation for the MoVe Listing API. It uses a simple Java Jersey doing REST calls and aims to be as simple and readable as possible.  

## Intended Audience

If you are planning to integrate with eCG Motors Vertical (MoVe) and are interested in technical details around API usage, this project is for you.

## How To Use This

The implementation contains 2 parts:

1) the client itself, utilizing JaxRs/Jersey
1) a number of tests that cover common usage scenarios

To run the scenario tests, you need **credentials** for the MoVe sandbox environment, and an assigned **`partnerId`**. If you haven't received those, please reach out to MoVe.
Once you have these details, set them in [config.properties](src/test/resources/config.properties).

To verify your settings, run `./gradlew clean test` and ensure that the tests are green. Next feel free to check the implementation itself and see how the basic usage scenarios can be implemented.
