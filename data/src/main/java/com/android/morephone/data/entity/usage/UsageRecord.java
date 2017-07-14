package com.android.morephone.data.entity.usage;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ethan on 7/14/17.
 */

public class UsageRecord {

    public String category;

    public String description;

    @SerializedName("account_sid")
    public String accountSid;

    @SerializedName("start_date")
    public String startDate;

    @SerializedName("end_date")
    public String endDate;

    public String count;

    @SerializedName("count_unit")
    public String countUnit;

    public String usage;

    @SerializedName("usage_unit")
    public String usageUnit;

    public String price;

    @SerializedName("price_unit")
    public String priceUnit;

    @SerializedName("api_version")
    public String apiVersion;

    public String uri;


    public class SubresourceUris{

        @SerializedName("all_time")
        public String allTime;

        public String today;

        public String yesterday;

        @SerializedName("this_month")
        public String thisMonth;

        @SerializedName("last_month")
        public String lastMonth;

        public String daily;

        public String monthly;

        public String yearly;

    }
}
