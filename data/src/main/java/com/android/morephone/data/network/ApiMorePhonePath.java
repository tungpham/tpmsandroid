package com.android.morephone.data.network;


import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.data.entity.record.Records;
import com.android.morephone.data.entity.usage.Usage;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by AnPEthan on 8/11/2016.
 */
interface ApiMorePhonePath {

    /*-----------------------------------------CALL LOGS-----------------------------------------*/
    @GET("Accounts/CallLogs")
    Call<Calls> getCallLogs();

    @GET("Accounts/Voices")
    Call<Records> getRecords();

    @GET("Accounts/Usage")
    Call<Usage> getUsage();

}
