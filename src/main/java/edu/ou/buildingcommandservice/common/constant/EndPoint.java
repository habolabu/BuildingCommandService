package edu.ou.buildingcommandservice.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndPoint {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Area {
        public static final String BASE = "/area";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Apartment {
        public static final String BASE = "/apartment";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Room {
        public static final String BASE = "/room";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Parking {
        public static final String BASE = "/parking";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PriceTag {
        public static final String BASE = "/price-tag";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ParkingType {
        public static final String BASE = "/parking-type";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ParkingSpace {
        public static final String BASE = "/parking-space";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class OwnerHistory {
        public static final String BASE = "/owner-history";
    }
}
