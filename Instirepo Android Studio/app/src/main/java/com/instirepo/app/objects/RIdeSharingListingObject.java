package com.instirepo.app.objects;

import java.util.List;

/**
 * Created by ashish on 12/04/16.
 */
public class RIdeSharingListingObject {

    public List<RideShareListSingleObj> rides;

    public class RideShareListSingleObj {
        public int seats, id;
        public String longitude, latitude, car, time;
    }
}
