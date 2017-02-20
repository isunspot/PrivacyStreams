package com.github.privacystreams.location;

import java.util.List;

/**
 * Created by yuanchun on 29/12/2016.
 * A predicate that checks whether a location is at home.
 */

class LocationInAreaPredicate extends LocationProcessor<Boolean> {
    private final double center_latitude;
    private final double center_longitude;
    private final double radius;

    LocationInAreaPredicate(String coordinatesField, double center_latitude, double center_longitude,
                            double radius) {
        super(coordinatesField);
        this.center_latitude = center_latitude;
        this.center_longitude = center_longitude;
        this.radius = radius;
    }

    @Override
    protected Boolean processLocation(double latitude, double longitude) {
        double latitude_delta = latitude - this.center_latitude;
        double longitude_delta = longitude - this.center_longitude;
        double distance = Math.sqrt(latitude_delta*latitude_delta + longitude_delta*longitude_delta);
        return distance <= this.radius;
    }

    @Override
    protected List<Object> getParameters() {
        List<Object> parameters = super.getParameters();
        parameters.add(this.center_latitude);
        parameters.add(this.center_longitude);
        parameters.add(this.radius);
        return parameters;
    }
}
