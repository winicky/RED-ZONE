package com.example.redzone;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Service {
    @POST("/disasterCountInfo")
    Call<CircleResponse> getCircleAPI(
            @Body CircleRequest circleRequest);

    @POST("/disasterInfo")
    Call<CircleResponse> getMsgAPI(
            @Body MsgRequest msgRequest);
}