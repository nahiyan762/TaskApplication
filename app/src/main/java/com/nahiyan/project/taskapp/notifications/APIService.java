package com.nahiyan.project.taskapp.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
        {
            "Content-Type:application/json",
            "Authorization: key = AAAAlflyBE4:APA91bGkMNEFShhnWwr_AA6ApM6Lz0m6UfHqqyu1ywPF8sB61dSJ90iaSeNEe0vnHAm5_LUScoBiLHM9NQPilrDzSQ7HK2Vifaq2l23QuRQv5LFt4aZdyJXnNlKeWiHEiZlpDvIUfsdR"
        }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
