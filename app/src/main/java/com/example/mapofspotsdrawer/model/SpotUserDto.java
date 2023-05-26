package com.example.mapofspotsdrawer.model;

public class SpotUserDto {
    private Long userId;

    private Long spotId;

    private Boolean liked;

    private Boolean favorite;

    public SpotUserDto(Long userId, Long spotId, Boolean liked, Boolean favorite) {
        this.userId = userId;
        this.spotId = spotId;
        this.liked = liked;
        this.favorite = favorite;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "SpotUserDto{" +
                "userId=" + userId +
                ", spotId=" + spotId +
                ", liked=" + liked +
                ", favorite=" + favorite +
                '}';
    }
}
