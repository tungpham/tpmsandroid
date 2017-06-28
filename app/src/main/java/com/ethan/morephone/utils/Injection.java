package com.ethan.morephone.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.repository.call.CallRepository;
import com.android.morephone.data.repository.call.source.remote.CallRemoteDataSource;
import com.android.morephone.data.repository.message.MessageRepository;
import com.android.morephone.data.repository.message.source.remote.MessageRemoteDataSource;
import com.android.morephone.data.repository.phonenumbers.incoming.IncomingPhoneNumberRepository;
import com.android.morephone.data.repository.phonenumbers.incoming.source.remote.IncomingPhoneNumberRemoteDataSource;
import com.android.morephone.data.repository.record.RecordRepository;
import com.android.morephone.data.repository.record.source.remote.RecordRemoteDataSource;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.call.CreateCall;
import com.android.morephone.domain.usecase.call.DeleteCall;
import com.android.morephone.domain.usecase.call.GetAllCalls;
import com.android.morephone.domain.usecase.call.GetCall;
import com.android.morephone.domain.usecase.call.GetCalls;
import com.android.morephone.domain.usecase.call.GetCallsIncoming;
import com.android.morephone.domain.usecase.call.GetCallsOutgoing;
import com.android.morephone.domain.usecase.message.CreateMessage;
import com.android.morephone.domain.usecase.message.DeleteMessage;
import com.android.morephone.domain.usecase.message.GetAllMessages;
import com.android.morephone.domain.usecase.message.GetMessages;
import com.android.morephone.domain.usecase.message.GetMessagesIncoming;
import com.android.morephone.domain.usecase.message.GetMessagesOutgoing;
import com.android.morephone.domain.usecase.number.DeleteIncomingPhoneNumber;
import com.android.morephone.domain.usecase.number.GetAvailableCountries;
import com.android.morephone.domain.usecase.number.incoming.ChangeFriendlyName;
import com.android.morephone.domain.usecase.record.DeleteRecord;
import com.android.morephone.domain.usecase.record.GetRecords;

/**
 * Created by Ethan on 3/3/17.
 */

public class Injection {

    public static UseCaseHandler providerUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    /*-----------------------------------------MESSAGE-----------------------------------------*/

    private static MessageRepository providerMessageRepository(@NonNull Context context) {
        return MessageRepository.getInstance(MessageRemoteDataSource.getInstance(context));
    }

    public static GetAllMessages providerGetMessages(@NonNull Context context) {
        return new GetAllMessages(providerMessageRepository(context));
    }

    public static GetMessages providerGetMessagesByToAndFrom(@NonNull Context context) {
        return new GetMessages(providerMessageRepository(context));
    }

    public static GetMessagesIncoming providerGetMessagesIncoming(@NonNull Context context) {
        return new GetMessagesIncoming(providerMessageRepository(context));
    }

    public static GetMessagesOutgoing providerGetMessagesOutgoing(@NonNull Context context) {
        return new GetMessagesOutgoing(providerMessageRepository(context));
    }

    public static CreateMessage providerCreateMessage(@NonNull Context context) {
        return new CreateMessage(providerMessageRepository(context));
    }


    public static DeleteMessage providerDeleteMessage(@NonNull Context context) {
        return new DeleteMessage(providerMessageRepository(context));
    }

    /*-----------------------------------------CALLS-----------------------------------------*/

    private static CallRepository providerVoiceRepository(@NonNull Context context) {
        return CallRepository.getInstance(CallRemoteDataSource.getInstance(context));
    }

    public static GetAllCalls providerGetAllCalls(@NonNull Context context) {
        return new GetAllCalls(providerVoiceRepository(context));
    }

    public static GetCalls providerGetCalls(@NonNull Context context) {
        return new GetCalls(providerVoiceRepository(context));
    }

    public static GetCall providerGetCall(@NonNull Context context) {
        return new GetCall(providerVoiceRepository(context));
    }

    public static GetCallsIncoming providerGetCallsIncoming(@NonNull Context context) {
        return new GetCallsIncoming(providerVoiceRepository(context));
    }

    public static GetCallsOutgoing providerGetCallsOutgoing(@NonNull Context context) {
        return new GetCallsOutgoing(providerVoiceRepository(context));
    }

    public static DeleteCall providerDeleteCall(@NonNull Context context) {
        return new DeleteCall(providerVoiceRepository(context));
    }

    public static CreateCall providerCreateCall(@NonNull Context context) {
        return new CreateCall(providerVoiceRepository(context));
    }


    /*--------------------------------Available Phone Numbers----------------------------------*/
    public static GetAvailableCountries providerAvailableCountries(@NonNull Context context) {
        return new GetAvailableCountries(context);
    }

    public static DeleteIncomingPhoneNumber providerDeleteIncomingPhoneNumber(@NonNull Context context) {
        return new DeleteIncomingPhoneNumber(context);
    }

    /*--------------------------------Incoming Phone Numbers----------------------------------*/
    private static IncomingPhoneNumberRepository providerIncomingPhoneNumberRepository(@NonNull Context context) {
        return IncomingPhoneNumberRepository.getInstance(IncomingPhoneNumberRemoteDataSource.getInstance(context));
    }

    public static ChangeFriendlyName providerChangeFriendlyName(@NonNull Context context) {
        return new ChangeFriendlyName(providerIncomingPhoneNumberRepository(context));
    }

    /*-----------------------------------------RECORD-----------------------------------------*/

    private static RecordRepository providerRecordRepository(@NonNull Context context) {
        return RecordRepository.getInstance(RecordRemoteDataSource.getInstance(context));
    }

    public static GetRecords providerGetCallRecords(@NonNull Context context) {
        return new GetRecords(providerRecordRepository(context));
    }

    public static DeleteRecord providerDeleteCallRecord(@NonNull Context context) {
        return new DeleteRecord(providerRecordRepository(context));
    }

}
