package com.ethan.morephone.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.repository.call.CallRepository;
import com.android.morephone.data.repository.call.source.remote.CallRemoteDataSource;
import com.android.morephone.data.repository.contact.ContactRepository;
import com.android.morephone.data.repository.contact.source.local.ContactLocalDataSource;
import com.android.morephone.data.repository.contact.source.remote.ContactRemoteDataSource;
import com.android.morephone.data.repository.message.MessageRepository;
import com.android.morephone.data.repository.message.source.remote.MessageRemoteDataSource;
import com.android.morephone.data.repository.group.GroupRepository;
import com.android.morephone.data.repository.group.source.local.GroupLocalDataSource;
import com.android.morephone.data.repository.group.source.remote.GroupRemoteDataSource;
import com.android.morephone.data.repository.phonenumbers.incoming.IncomingPhoneNumberRepository;
import com.android.morephone.data.repository.phonenumbers.incoming.source.remote.IncomingPhoneNumberRemoteDataSource;
import com.android.morephone.data.repository.record.RecordRepository;
import com.android.morephone.data.repository.record.source.remote.RecordRemoteDataSource;
import com.android.morephone.data.repository.usage.UsageRepository;
import com.android.morephone.data.repository.usage.source.remote.UsageRemoteDataSource;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.call.CreateCall;
import com.android.morephone.domain.usecase.call.DeleteCall;
import com.android.morephone.domain.usecase.call.GetAllCalls;
import com.android.morephone.domain.usecase.call.GetCall;
import com.android.morephone.domain.usecase.call.GetCalls;
import com.android.morephone.domain.usecase.call.GetCallsIncoming;
import com.android.morephone.domain.usecase.call.GetCallsOutgoing;
import com.android.morephone.domain.usecase.contact.ContactFactory;
import com.android.morephone.domain.usecase.contact.CreateContact;
import com.android.morephone.domain.usecase.contact.DeleteContact;
import com.android.morephone.domain.usecase.contact.GetContact;
import com.android.morephone.domain.usecase.contact.GetContactByPhoneNumber;
import com.android.morephone.domain.usecase.contact.GetContacts;
import com.android.morephone.domain.usecase.contact.UpdateContact;
import com.android.morephone.domain.usecase.message.CreateMessage;
import com.android.morephone.domain.usecase.message.DeleteMessage;
import com.android.morephone.domain.usecase.message.GetAllMessages;
import com.android.morephone.domain.usecase.message.GetMessages;
import com.android.morephone.domain.usecase.message.GetMessagesIncoming;
import com.android.morephone.domain.usecase.message.GetMessagesOutgoing;
import com.android.morephone.domain.usecase.group.CreateGroup;
import com.android.morephone.domain.usecase.group.GetGroupsByUserId;
import com.android.morephone.domain.usecase.group.UpdateGroup;
import com.android.morephone.domain.usecase.number.BuyIncomingPhoneNumber;
import com.android.morephone.domain.usecase.number.DeleteIncomingPhoneNumber;
import com.android.morephone.domain.usecase.number.GetAvailableCountries;
import com.android.morephone.domain.usecase.number.incoming.ChangeFriendlyName;
import com.android.morephone.domain.usecase.record.DeleteRecord;
import com.android.morephone.domain.usecase.record.GetRecords;
import com.android.morephone.domain.usecase.usage.GetUsageAllTime;

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

    public static BuyIncomingPhoneNumber providerBuyIncomingPhoneNumber(@NonNull Context context) {
        return new BuyIncomingPhoneNumber(providerIncomingPhoneNumberRepository(context));
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

    /*-------------------------------------------USAGE-----------------------------------------*/

    private static UsageRepository providerUsageRepository(@NonNull Context context) {
        return UsageRepository.getInstance(UsageRemoteDataSource.getInstance(context));
    }

    public static GetUsageAllTime providerGetUsageAllTime(@NonNull Context context) {
        return new GetUsageAllTime(providerUsageRepository(context));
    }

    /*-------------------------------------------CONTACT-----------------------------------------*/
    private static ContactRepository providerContactRepository(@NonNull Context context) {
        return ContactRepository.getInstance(ContactRemoteDataSource.getInstance(context), ContactLocalDataSource.getInstance(context));
    }

    public static GetContacts providerGetContacts(@NonNull Context context) {
        return new GetContacts(providerContactRepository(context), new ContactFactory());
    }

    public static GetContact providerGetContact(@NonNull Context context) {
        return new GetContact(providerContactRepository(context));
    }

    public static GetContactByPhoneNumber providerGetContactByPhoneNumber(@NonNull Context context) {
        return new GetContactByPhoneNumber(providerContactRepository(context));
    }

    public static CreateContact providerCreateContact(@NonNull Context context) {
        return new CreateContact(providerContactRepository(context));
    }

    public static UpdateContact providerUpdateContact(@NonNull Context context) {
        return new UpdateContact(providerContactRepository(context));
    }

    public static DeleteContact providerDeleteContact(@NonNull Context context) {
        return new DeleteContact(providerContactRepository(context));
    }

    /*-------------------------------------------MESSAGE GROUP-----------------------------------------*/
    private static GroupRepository providerGroupRepository(@NonNull Context context) {
        return GroupRepository.getInstance(GroupRemoteDataSource.getInstance(context), GroupLocalDataSource.getInstance(context));
    }

    public static CreateGroup providerCreateGroup(@NonNull Context context) {
        return new CreateGroup(providerGroupRepository(context));
    }

    public static GetGroupsByUserId providerGetMessageGroupById(@NonNull Context context) {
        return new GetGroupsByUserId(providerGroupRepository(context));
    }

    public static UpdateGroup providerUpdateMessageGroup(@NonNull Context context) {
        return new UpdateGroup(providerGroupRepository(context));
    }
}
