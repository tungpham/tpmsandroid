package com.android.morephone.data.entity.record.mapper;

import com.android.morephone.data.entity.record.Record;
import com.android.morephone.data.entity.twilio.record.RecordItem;

/**
 * Created by Ethan on 6/28/17.
 */

public class RecordDataMapper {

    public static Record transform(boolean isComing, String phoneNumber, RecordItem recordItem) {
        Record record = null;
        if (recordItem != null) {
            record = new Record(recordItem.sid,
                    recordItem.accountSid,
                    recordItem.callSid,
                    phoneNumber,
                    recordItem.duration,
                    recordItem.dateCreated,
                    recordItem.apiVersion,
                    recordItem.dateUpdated,
                    recordItem.status,
                    recordItem.source,
                    recordItem.channels,
                    recordItem.price,
                    recordItem.priceUnit,
                    recordItem.uri,
                    isComing);
        }
        return record;
    }
}
