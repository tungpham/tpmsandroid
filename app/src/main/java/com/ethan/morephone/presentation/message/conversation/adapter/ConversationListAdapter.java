package com.ethan.morephone.presentation.message.conversation.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.conversation.ConversationModel;
import com.android.morephone.data.repository.contact.source.GetContactCallback;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.contact.GetContact;
import com.android.morephone.domain.usecase.contact.GetContactByPhoneNumber;
import com.ethan.morephone.R;
import com.ethan.morephone.utils.ContactUtil;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.TextDrawable;

import java.util.Collections;
import java.util.List;

/**
 * Created by Ethan on 2/16/17.
 */

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListViewHolder> {

    private List<ConversationModel> mConversationModels;
    private OnItemConversationClickListener mOnItemConversationClickListener;
    private TextDrawable.IBuilder mDrawableBuilder;
    private Context mContext;
    private UseCaseHandler mUseCaseHandler;
    private GetContactByPhoneNumber mGetContact;

    public ConversationListAdapter(Context context, UseCaseHandler useCaseHandler, GetContactByPhoneNumber getContact, List<ConversationModel> conversationModels, OnItemConversationClickListener onItemConversationClickListener) {
        mContext = context;
        mConversationModels = conversationModels;
        mOnItemConversationClickListener = onItemConversationClickListener;
        mDrawableBuilder = TextDrawable.builder().round();
        mUseCaseHandler = useCaseHandler;
        mGetContact = getContact;
    }

    public void replaceData(List<ConversationModel> conversationModels) {
        Collections.sort(conversationModels);

        mConversationModels = conversationModels;
        notifyDataSetChanged();
    }

    public List<ConversationModel> getData() {
        return mConversationModels;
    }

    @Override
    public ConversationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conversation, parent, false);
        return new ConversationListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ConversationListViewHolder holder, int position) {
        final ConversationModel conversationModel = mConversationModels.get(position);
        List<MessageItem> messageItems = conversationModel.mMessageItems;
        MessageItem messageItem = messageItems.get(messageItems.size() - 1);

        holder.textSmsTitle.setText(conversationModel.mPhoneNumber);

        ContactUtil.getContactDisplay(holder.textSmsTitle, mUseCaseHandler, mGetContact, conversationModel.mPhoneNumber, new GetContactCallback() {
            @Override
            public void onContactLoaded(View view, Contact contact) {
                if(view instanceof TextView){
                    ((TextView) view).setText(contact.getDisplayName());
                }
            }

            @Override
            public void onContactNotAvailable() {

            }
        });

        holder.textSmsDescription.setText(messageItem.body);
        if (!TextUtils.isEmpty(messageItem.dateSent)) {
            holder.textSmsTime.setText(Utils.formatDate(messageItem.dateSent));
        }
        holder.relativeItemSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemConversationClickListener.onItemClick(conversationModel);
            }
        });
        holder.imageIcon.setImageDrawable(mDrawableBuilder.build(String.valueOf(conversationModel.mPhoneNumber.charAt(0)), ContextCompat.getColor(mContext, R.color.colorBackgroundAvatar)));
    }

    @Override
    public int getItemCount() {
        return mConversationModels.size();
    }

//    public void setFilter(List<MessageItem> smsEntities) {
//        mMessageItems = new ArrayList<>();
//        mMessageItems.addAll(smsEntities);
//        notifyDataSetChanged();
//    }

    public interface OnItemConversationClickListener {
        void onItemClick(ConversationModel conversationModel);
    }
}
