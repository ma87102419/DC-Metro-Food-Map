package com.foodie.foodmapapi.test;

import com.foodie.foodmapapi.models.*;
import com.foodie.foodmapapi.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.OffsetDateTime;

//@Component
public class ReadDatabase {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public ApplicationRunner readDatabaseMethod(AccountRepository accountRepository,
                                                StationRepository stationRepository,
                                                LineRepository lineRepository,
                                                PlatformRepository platformRepository,
                                                RestaurantRepository restaurantRepository,
                                                ReviewRepository reviewRepository) {
        return args -> {
            accountRepository.findAll().forEach(account -> {
                account.getFavoriteRestaurants().clear();
                accountRepository.save(account);
            });
            reviewRepository.deleteAll();
            restaurantRepository.deleteAll();
            platformRepository.deleteAll();
            lineRepository.deleteAll();
            stationRepository.deleteAll();

            Line l1 = new Line();
            l1.setLineCode("RD");
            l1.setName("Red");
            l1 = lineRepository.save(l1);

            Station s1 = new Station();
            s1.setName("Vienna");
            s1.setLatitude(1.2);
            s1.setLongitude(3.4);
            s1.setAddressStreet("9550 Saintsbury Dr");
            s1.setAddressCity("Fairfax");
            s1.setAddressState("VA");
            s1.setAddressZip("22031");
            s1 = stationRepository.save(s1);

            platformRepository.save(new Platform(s1, l1, "K08"));

            stationRepository.findAll().forEach(station -> {
                logger.info("Station: {}", station.getName());
                station.getPlatforms().forEach(p -> {
                    logger.info("Platform for {}: {} {} {}", station.getName(), p.getStationCode(), p.getLine().getLineCode(), p.getLine().getName());
                });
            });

            Restaurant r1 = new Restaurant();
            r1.setName("Glory Days Grill");
            r1.setThirdPartyID("Glory Days Grill");
            r1.setThirdPartySource("Google Maps");
            r1.setOpenTime(LocalTime.of(11, 0));
            r1.setCloseTime(LocalTime.of(23, 0));
            r1.setStarRating(4.0);
            r1.setPriceRating(2);
            r1.setLatitude(38.8702167);
            r1.setLongitude(-77.2609551);
            r1.setAddressStreet("3059 Nutley St");
            r1.setAddressCity("Fairfax");
            r1.setAddressState("VA");
            r1.setAddressZip("22031");
            r1.setNearbyStation(s1);
            r1 = restaurantRepository.save(r1);

            Review v1 = new Review();
            v1.setRestaurant(r1);
            v1.setSource("Google Maps");
            v1.setName("Fernando Jose");
            v1.setComment("Wow, where to even begin? Entrance is well taken care of with outdoor seating" +
                    " available in the shade. Quite a large outdoor space, they did put the space to great use!" +
                    " Great menu items, I was left with many thins to chose from!! Our host was nice," +
                    " he was very friendly. The downfall was the arrival for the food to come to our table." +
                    " It took approximately 40 minutes to get our food, the bacon and cheese burger with seasoned fries." +
                    " It just the two of us, and it wasn’t that busy. I loved the fact that each table did have their" +
                    " individual TV the watch the show one wished for. The burger was well cooked, juicy and tender." +
                    " Nicely seasoned bacon, and fries. Mango Ice Tea was flavourful. I will be back again but" +
                    " I hope that our wait time isn’t long.");
            v1.setPostTime(OffsetDateTime.now().minusMonths(7));
            v1.setStarRating(4);

            Review v2 = new Review();
            v2.setRestaurant(r1);
            v2.setSource("Test");
            v2.setName("Christian Dufrois");
            v2.setComment("This is just a restaurant I found on Google Maps");
            v2.setPostTime(OffsetDateTime.now());
            v2.setStarRating(5);

            // Save/Retrieve reviews separately
            reviewRepository.save(v1);
            reviewRepository.save(v2);

            reviewRepository.findAllByRestaurant(r1).forEach(review -> {
                logger.info("Review: {} {} {}", review.getRestaurant().getName(), review.getName(), review.getStarRating() + "*");
            });
        };
    }
}
