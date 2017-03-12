package com.ethan.morephone.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.repository.message.MessageRepository;
import com.android.morephone.data.repository.message.source.remote.MessageRemoteDataSource;
import com.android.morephone.data.repository.voice.VoiceRepository;
import com.android.morephone.data.repository.voice.source.remote.VoiceRemoteDataSource;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.message.CreateMessage;
import com.android.morephone.domain.usecase.message.DeleteMessage;
import com.android.morephone.domain.usecase.message.GetAllMessages;
import com.android.morephone.domain.usecase.message.GetMessages;
import com.android.morephone.domain.usecase.message.GetMessagesIncoming;
import com.android.morephone.domain.usecase.message.GetMessagesOutgoing;
import com.android.morephone.domain.usecase.voice.CreateVoice;
import com.android.morephone.domain.usecase.voice.DeleteVoice;
import com.android.morephone.domain.usecase.voice.GetAllVoices;
import com.android.morephone.domain.usecase.voice.GetVoices;
import com.android.morephone.domain.usecase.voice.GetVoicesIncoming;
import com.android.morephone.domain.usecase.voice.GetVoicesOutgoing;

/**
 * Created by Ethan on 3/3/17.
 */

public class Injection {

    private static MessageRepository providerMessageRepository(@NonNull Context context) {
        return MessageRepository.getInstance(MessageRemoteDataSource.getInstance(context));
    }

    public static UseCaseHandler providerUseCaseHandler() {
        return UseCaseHandler.getInstance();
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


    private static VoiceRepository providerVoiceRepository(@NonNull Context context) {
        return VoiceRepository.getInstance(VoiceRemoteDataSource.getInstance(context));
    }

    public static GetAllVoices providerGetAllVoice(@NonNull Context context) {
        return new GetAllVoices(providerVoiceRepository(context));
    }

    public static GetVoices providerGetVoices(@NonNull Context context) {
        return new GetVoices(providerVoiceRepository(context));
    }

    public static GetVoicesIncoming providerGetVoicesIncoming(@NonNull Context context) {
        return new GetVoicesIncoming(providerVoiceRepository(context));
    }

    public static GetVoicesOutgoing providerGetVoicesOutgoing(@NonNull Context context) {
        return new GetVoicesOutgoing(providerVoiceRepository(context));
    }

    public static DeleteVoice providerDeleteVoice(@NonNull Context context) {
        return new DeleteVoice(providerVoiceRepository(context));
    }

    public static CreateVoice providerCreateVoice(@NonNull Context context) {
        return new CreateVoice(providerVoiceRepository(context));
    }

}
