package com.ethan.morephone.presentation.message.conversation;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.call.Call;
import com.android.morephone.data.entity.call.ResourceCall;
import com.android.morephone.data.entity.conversation.ConversationModel;
import com.android.morephone.data.entity.conversation.ResourceMessage;
import com.android.morephone.data.entity.messagegroup.MessageGroup;
import com.android.morephone.data.entity.twilio.MessageListResourceResponse;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiManager;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.message.CreateMessage;
import com.android.morephone.domain.usecase.message.GetAllMessages;
import com.android.morephone.domain.usecase.message.GetMessagesIncoming;
import com.android.morephone.domain.usecase.message.GetMessagesOutgoing;
import com.android.morephone.domain.usecase.messagegroup.CreateMessageGroups;
import com.ethan.morephone.Constant;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
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
    private final CreateMessage mCreateMessage;
    private final CreateMessageGroups mCreateMessageGroups;

    private List<MessageItem> mMessageItems;
//    private List<ConversationModel> mConversationModels;
//    private ArrayMap<String, List<MessageItem>> mArrayMap;

    private ResourceMessage mResourceMessage;

    public ConversationsPresenter(@NonNull ConversationsContract.View view,
                                  @NonNull UseCaseHandler useCaseHandler,
                                  @NonNull GetAllMessages getAllMessages,
                                  @NonNull GetMessagesIncoming getMessagesIncoming,
                                  @NonNull GetMessagesOutgoing getMessagesOutgoing,
                                  @NonNull CreateMessage createMessage,
                                  @NonNull CreateMessageGroups createMessageGroups) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mGetAllMessages = getAllMessages;
        mGetMessagesIncoming = getMessagesIncoming;
        mGetMessagesOutgoing = getMessagesOutgoing;
        mCreateMessage = createMessage;
        mCreateMessageGroups = createMessageGroups;

        mMessageItems = new ArrayList<>();
//        mConversationModels = new ArrayList<>();
//        mArrayMap = new ArrayMap<>();

        mResourceMessage = new ResourceMessage(new ArrayList<ConversationModel>(), "", Constant.FIRST_PAGE, "", "", "",Constant.FIRST_PAGE, "", "", 0);
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadListMessageResource(Context context, String phoneNumber, boolean isShowLoading) {

        String pageIncoming = "";
        String pageOutgoing = "";
        if (mResourceMessage != null) {

            pageIncoming = mResourceMessage.incomingNextPageUri;
            pageOutgoing = mResourceMessage.outgoingNextPageUri;

            mResourceMessage.incomingNextPageUri = "";
            mResourceMessage.outgoingNextPageUri = "";
        }

        new DataAsync(context, this, phoneNumber, isShowLoading, pageIncoming, pageOutgoing).execute();
    }

    @Override
    public void loadMessagesIncoming(String phoneNumberIncoming) {
        mView.showLoading(true);
        GetMessagesIncoming.RequestValue requestValue = new GetMessagesIncoming.RequestValue(phoneNumberIncoming);
        mUseCaseHandler.execute(mGetMessagesIncoming, requestValue, new UseCase.UseCaseCallback<GetMessagesIncoming.ResponseValue>() {
            @Override
            public void onSuccess(GetMessagesIncoming.ResponseValue response) {
                response.getMessages();
                //  executeData(response.getMessageItems(), true);

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
//                executeData(response.getMessageItems(), false);
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
//        mArrayMap.clear();

        mResourceMessage = new ResourceMessage(new ArrayList<ConversationModel>(), "", Constant.FIRST_PAGE, "", "", "",Constant.FIRST_PAGE, "", "", 0);
    }

    @Override
    public void parseFakeData(FakeData fakeData, String phoneNumber) {
//        mArrayMap.clear();
        List<MessageItem> messageItemsIncoming = parseMessageIncoming(fakeData, phoneNumber);
        List<MessageItem> messageItemsOutgoing = parseMessageOutgoing(fakeData, phoneNumber);
    }

//    private void executeData(List<MessageItem> messageItems, boolean isComing) {
//        if (isComing) {
//            for (MessageItem messageItem : messageItems) {
//                if(!TextUtils.isEmpty(messageItem.status) && messageItem.status.equals("received")){
//                    if (mArrayMap.containsKey(messageItem.from)) {
//                        mArrayMap.get(messageItem.from).add(messageItem);
//                    } else {
//                        List<MessageItem> items = new ArrayList<>();
//                        items.add(messageItem);
//                        mArrayMap.put(messageItem.from, items);
//                    }
//                }
//
//            }
//        } else {
//            for (MessageItem messageItem : messageItems) {
//                if(!TextUtils.isEmpty(messageItem.status) && messageItem.status.equals("delivered")){
//                    if (mArrayMap.containsKey(messageItem.to)) {
//                        mArrayMap.get(messageItem.to).add(messageItem);
//                    } else {
//                        List<MessageItem> items = new ArrayList<>();
//                        items.add(messageItem);
//                        mArrayMap.put(messageItem.to, items);
//                    }
//                }
//            }
//        }
//        mConversationModels.clear();
//        for (Map.Entry entry : mArrayMap.entrySet()) {
//            List<MessageItem> items = mArrayMap.get(entry.getKey());
//            if (items != null && !items.isEmpty()) {
//                Collections.sort(items);
//                String dateCreated = items.get(items.size() - 1).dateCreated;
//                DebugTool.logD("DATE CREATED: " + dateCreated);
//                mConversationModels.add(new ConversationModel(entry.getKey().toString(), dateCreated, items));
//            }
//
//        }
//
//        DebugTool.logD("SIZE CONVERSATION: " + mConversationModels.size());
//
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

    @Override
    public void createMessage(String userId, String to, String from, String body, int position) {
        mView.showLoading(true);
        CreateMessage.RequestValue requestValue = new CreateMessage.RequestValue(userId, to, from, body);
        mUseCaseHandler.execute(mCreateMessage, requestValue, new UseCase.UseCaseCallback<CreateMessage.ResponseValue>() {
            @Override
            public void onSuccess(CreateMessage.ResponseValue response) {
                mView.showLoading(false);
                mView.createMessageSuccess(response.getMessageItem());
            }

            @Override
            public void onError() {
//                mView.showProgress(false, position);
                mView.showLoading(false);
                mView.createMessageError();
            }
        });
    }

    @Override
    public void createMessageGroup(Context context, MessageGroup messageGroup) {
        CreateMessageGroups.RequestValues requestValue = new CreateMessageGroups.RequestValues(messageGroup);
        mUseCaseHandler.execute(mCreateMessageGroups, requestValue, new UseCase.UseCaseCallback<CreateMessageGroups.ResponseValue>() {
            @Override
            public void onSuccess(CreateMessageGroups.ResponseValue response) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public boolean hasNextPage() {
        if(mResourceMessage != null && (!TextUtils.isEmpty(mResourceMessage.incomingNextPageUri) || !TextUtils.isEmpty(mResourceMessage.outgoingNextPageUri))){
            return true;
        }
        return false;
    }

    private static class DataAsync extends AsyncTask<Void, Integer, Void> {
        private final WeakReference<ConversationsPresenter> mWeakReference;
        private final String mPhoneNumber;
        private final Context mContext;
        private final String mPageIncoming;
        private final String mPageOutgoing;
        private final boolean isShowLoading;

        public DataAsync(Context context, ConversationsPresenter presenter, String phoneNumber, boolean isShowLoading, String pageIncoming, String pageOutgoing) {
            mWeakReference = new WeakReference<>(presenter);
            this.mPhoneNumber = phoneNumber;
            mContext = context;
            this.isShowLoading = isShowLoading;
            mPageIncoming = pageIncoming;
            mPageOutgoing = pageOutgoing;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ConversationsPresenter presenter = mWeakReference.get();
            if(presenter != null && isShowLoading){
                presenter.mView.showLoading(true);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            ConversationsPresenter presenter = mWeakReference.get();
            if (presenter != null) {

                BaseResponse<ResourceMessage> baseResponse = ApiMorePhone.getMessage(mContext, mPhoneNumber, mPageIncoming, mPageOutgoing);
                if(baseResponse != null && baseResponse.getResponse() != null){
                    presenter.mResourceMessage = baseResponse.getResponse();
                }

//                MessageListResourceResponse messageIncoming = ApiManager.getMessagesIncoming(mContext, mPhoneNumber);
//                MessageListResourceResponse messageOutgoing = ApiManager.getMessagesOutgoing(mContext, mPhoneNumber);
//
//                if (messageIncoming != null && messageIncoming.messages != null && !messageIncoming.messages.isEmpty()) {
//                    presenter.executeData(messageIncoming.messages, true);
//                }
//
//                if (messageOutgoing != null && messageOutgoing.messages != null && !messageOutgoing.messages.isEmpty()) {
//                    presenter.executeData(messageOutgoing.messages, false);
//                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ConversationsPresenter presenter = mWeakReference.get();
            if(presenter != null){
                if (presenter.mResourceMessage != null && presenter.mResourceMessage.records != null) {
                    presenter.mView.showListMessage(presenter.mResourceMessage.records);
                }
                presenter.mView.showLoading(false);
            }
            DebugTool.logD("POST EXECUTE");
        }
    }
}
