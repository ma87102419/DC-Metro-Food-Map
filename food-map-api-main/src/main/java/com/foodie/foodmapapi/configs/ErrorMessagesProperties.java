package com.foodie.foodmapapi.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ErrorMessagesProperties {

    @Value("${food-map-api.error-messages.not-found-by-username}")
    private String notFoundByUsername;
    @Value("${food-map-api.error-messages.not-found-by-role-value}")
    private String notFoundByRoleValue;
    @Value("${food-map-api.error-messages.not-found-by-line-code}")
    private String notFoundByLineCode;
    @Value("${food-map-api.error-messages.not-found-by-stationID}")
    private String notFoundByStationID;
    @Value("${food-map-api.error-messages.not-found-by-station-code}")
    private String notFoundByStationCode;
    @Value("${food-map-api.error-messages.not-found-by-restaurantID}")
    private String notFoundByRestaurantID;
    @Value("${food-map-api.error-messages.invalid-username}")
    private String invalidUsername;
    @Value("${food-map-api.error-messages.invalid-email}")
    private String invalidEmail;
    @Value("${food-map-api.error-messages.missing-password}")
    private String missingPassword;
    @Value("${food-map-api.error-messages.taken-username}")
    private String takenUsername;
    @Value("${food-map-api.error-messages.taken-email}")
    private String takenEmail;
    @Value("${food-map-api.error-messages.bad-credentials}")
    private String badCredentials;
    @Value("${food-map-api.error-messages.failed-authentication}")
    private String failedAuthentication;

}
