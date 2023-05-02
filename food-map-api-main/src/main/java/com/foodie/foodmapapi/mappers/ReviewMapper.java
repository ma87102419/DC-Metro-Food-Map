package com.foodie.foodmapapi.mappers;

import com.foodie.foodmapapi.dtos.ReviewDTO;
import com.foodie.foodmapapi.models.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDTO map(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setRestaurantID(review.getRestaurant().getRestaurantID());
        dto.setSource(review.getSource());
        dto.setName(review.getName());
        dto.setComment(review.getComment());
        dto.setPostTime(review.getPostTime());
        dto.setStarRating(review.getStarRating());
        return dto;
    }
}
