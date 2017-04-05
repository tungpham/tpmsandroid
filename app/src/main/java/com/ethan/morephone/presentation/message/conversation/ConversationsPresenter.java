package com.ethan.morephone.presentation.message.conversation;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.message.GetAllMessages;
import com.android.morephone.domain.usecase.message.GetMessagesIncoming;
import com.android.morephone.domain.usecase.message.GetMessagesOutgoing;
import com.ethan.morephone.model.ConversationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan on 2/17/17.
 */

public class ConversationsPresenter implements ConversationsContract.Presenter {

    private final ConversationsContract.View mView;
    private final UseCaseHandler mUseCaseHandler;
    private final GetAllMessages mGetAllMessages;
    private final GetMessagesIncoming mGetMessagesIncoming;
    private final GetMessagesOutgoing mGetMessagesOutgoing;

    private List<MessageItem> mMessageItems;
    private List<ConversationModel> mConversationModels;
    private ArrayMap<String, List<MessageItem>> mArrayMap;

    public ConversationsPresenter(@NonNull ConversationsContract.View view,
                                  @NonNull UseCaseHandler useCaseHandler,
                                  @NonNull GetAllMessages getAllMessages,
                                  @NonNull GetMessagesIncoming getMessagesIncoming,
                                  @NonNull GetMessagesOutgoing getMessagesOutgoing) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mGetAllMessages = getAllMessages;
        mGetMessagesIncoming = getMessagesIncoming;
        mGetMessagesOutgoing = getMessagesOutgoing;

        mMessageItems = new ArrayList<>();
        mConversationModels = new ArrayList<>();
        mArrayMap = new ArrayMap<>();

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadListMessageResource() {
        mView.showLoading(true);
        GetAllMessages.RequestValue requestValue = new GetAllMessages.RequestValue();
        mUseCaseHandler.execute(mGetAllMessages, requestValue, new UseCase.UseCaseCallback<GetAllMessages.ResponseValue>() {
            @Override
            public void onSuccess(GetAllMessages.ResponseValue response) {
                List<MessageItem> messageItems = response.getMessageItems();
                for (MessageItem messageItem : messageItems) {
                    DebugTool.logD("BODY: " + messageItem.body);
                }
//                mView.showListMessage(messageItems);
                mView.showLoading(false);
            }

            @Override
            public void onError() {
                DebugTool.logD("ERORR");
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void loadMessagesIncoming(String phoneNumberIncoming) {
        mView.showLoading(true);
        GetMessagesIncoming.RequestValue requestValue = new GetMessagesIncoming.RequestValue(phoneNumberIncoming);
        mUseCaseHandler.execute(mGetMessagesIncoming, requestValue, new UseCase.UseCaseCallback<GetMessagesIncoming.ResponseValue>() {
            @Override
            public void onSuccess(GetMessagesIncoming.ResponseValue response) {
                executeData(response.getMessageItems(), true);
                mView.showLoading(false);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void loadMessageOutgoing(String phoneNumberOutgoing) {
        mView.showLoading(true);
        GetMessagesOutgoing.RequestValue requestValue = new GetMessagesOutgoing.RequestValue(phoneNumberOutgoing);
        mUseCaseHandler.execute(mGetMessagesOutgoing, requestValue, new UseCase.UseCaseCallback<GetMessagesOutgoing.ResponseValue>() {
            @Override
            public void onSuccess(GetMessagesOutgoing.ResponseValue response) {
                executeData(response.getMessageItems(), false);
                mView.showLoading(false);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void clearData() {
        mArrayMap.clear();
    }

    @Override
    public void parseFakeData(FakeData fakeData, String phoneNumber) {
        mArrayMap.clear();
        List<MessageItem> messageItemsIncoming = parseMessageIncoming(fakeData, phoneNumber);
        List<MessageItem> messageItemsOutgoing = parseMessageOutgoing(fakeData, phoneNumber);
        executeData(messageItemsIncoming, true);
        executeData(messageItemsOutgoing, false);
    }

    private void executeData(List<MessageItem> messageItems, boolean isComing) {
        if (isComing) {
            for (MessageItem messageItem : messageItems) {
                if (mArrayMap.containsKey(messageItem.from)) {
                    mArrayMap.get(messageItem.from).add(messageItem);
                } else {
                    List<MessageItem> items = new ArrayList<>();
                    items.add(messageItem);
                    mArrayMap.put(messageItem.from, items);
                }
            }
        } else {
            for (MessageItem messageItem : messageItems) {
                if (mArrayMap.containsKey(messageItem.to)) {
                    mArrayMap.get(messageItem.to).add(messageItem);
                } else {
                    List<MessageItem> items = new ArrayList<>();
                    items.add(messageItem);
                    mArrayMap.put(messageItem.to, items);
                }
            }
        }
        mConversationModels.clear();
        for (Map.Entry entry : mArrayMap.entrySet()) {
            mConversationModels.add(new ConversationModel(entry.getKey().toString(), mArrayMap.get(entry.getKey())));
        }
        mView.showListMessage(mConversationModels);
    }

//    public String loadJSONFromAsset() {
//        String json;
//        try {
//            InputStream is = mContext.getAssets().open("fake_data.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }

    public List<MessageItem> parseMessageIncoming(FakeData mFakeData, String phoneNumberIncoming) {
        List<MessageItem> messageItems = new ArrayList<>();
        if (mFakeData != null) {
            for (FakeData.Message message : mFakeData.message) {
                if (message.to.equals(phoneNumberIncoming)) {
                    messageItems.add(new MessageItem(
                            message.sid,
                            message.date_created,
                            message.date_created,
                            message.date_created,
                            null,
                            message.to,
                            message.from,
                            null,
                            message.body,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            null));
                }
            }
        }
        return messageItems;
    }

    public List<MessageItem> parseMessageOutgoing(FakeData mFakeData, String phoneNumberOutgoind) {
        List<MessageItem> messageItems = new ArrayList<>();
        if (mFakeData != null) {
            for (FakeData.Message message : mFakeData.message) {
                if (message.from.equals(phoneNumberOutgoind)) {
                    messageItems.add(new MessageItem(
                            message.sid,
                            message.date_created,
                            message.date_created,
                            message.date_created,
                            null,
                            message.to,
                            message.from,
                            null,
                            message.body,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            message.status,
                            null));
                }
            }
        }
        return messageItems;
    }
}
