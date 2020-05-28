package com.geovengers.redzone;

public class ApiUtils {

    public static final String BASE_URL = "http://3.34.46.209:3500";

    public static Service getService() {
        return RetrofitClient.getClient(BASE_URL).create(Service.class);
    }
}