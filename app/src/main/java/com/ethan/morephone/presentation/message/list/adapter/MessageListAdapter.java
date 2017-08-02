package com.ethan.morephone.presentation.message.list.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.MessageItem;
import com.ethan.morephone.Constant;
import com.ethan.morephone.R;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.TextDrawable;

import java.util.List;

/**
 * Created by Ethan on 2/16/17.
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int INCOMING_ITEM = 0;
    public static final int OUTGOING_ITEM = 1;

    private Context mContext;
    private String mTitle;
    private List<MessageItem> mMessageItems;
    private TextDrawable.IBuilder mDrawableBuilder;
    private OnItemMessageClickListener mOnItemMessageClickListener;


    public MessageListAdapter(Context context, List<MessageItem> messageItems, String title, OnItemMessageClickListener onItemConversationClickListener) {
        mContext = context;
        mMessageItems = messageItems;
        mTitle = title;
        mOnItemMessageClickListener = onItemConversationClickListener;
        mDrawableBuilder = TextDrawable.builder().round();
    }

    public void replaceData(List<MessageItem> messageItems) {
        mMessageItems = messageItems;
        notifyDataSetChanged();
    }

    public List<MessageItem> getData(){
        return mMessageItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int resource;
        boolean isSent;

        if (viewType == INCOMING_ITEM) {
            resource = R.layout.item_message_in;
            isSent = false;
        } else {
            resource = R.layout.item_message_out;
            isSent = true;
        }

        View view = inflater.inflate(resource, parent, false);
        return setupViewHolder(view, isSent);
    }

    private RecyclerView.ViewHolder setupViewHolder(View view, boolean isSent) {
        if (!isSent) {
            MessageInViewHolder holder = new MessageInViewHolder(view);
            return holder;
        } else {
            MessageOutViewHolder holder = new MessageOutViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MessageItem messageItem = mMessageItems.get(position);
        if (holder instanceof MessageInViewHolder) {
            MessageInViewHolder viewHolder = (MessageInViewHolder) holder;
            viewHolder.imageAvatar.setImageDrawable(mDrawableBuilder.build(String.valueOf(mTitle.charAt(0)), ContextCompat.getColor(mContext, R.color.colorBackgroundAvatar)));
            viewHolder.textMessageBody.setText(messageItem.body);
            viewHolder.textMessageTime.setText(Utils.formatDate(messageItem.dateSent));

            viewHolder.relativeMessageBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemMessageClickListener.onItemClick(position);
                }
            });

        } else if (holder instanceof MessageOutViewHolder) {
            MessageOutViewHolder viewHolder = (MessageOutViewHolder) holder;
            viewHolder.textMessageBody.setText(messageItem.body);
            viewHolder.textMessageTime.setText(Utils.formatDate(messageItem.dateSent));

            viewHolder.imageAvatar.setImageDrawable(mDrawableBuilder.build("Me", ContextCompat.getColor(mContext, R.color.colorBackgroundAvatar)));
            viewHolder.textMessageBody.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorBackgroundMessageBody), PorterDuff.Mode.SRC_ATOP);
            viewHolder.relativeMessageBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemMessageClickListener.onItemClick(position);
                }
            });
//            viewHolder.imageAvatar.setImageDrawable(Contact.getMe(true).getAvatar(mContext, null));
        }
    }

    @Override
    public int getItemViewType(int position) {
        // This method shouldn't be called if our cursor is null, since the framework should know
        // that there aren't any items to look at in that case
        MessageItem item = mMessageItems.get(position);
        if(item == null) return INCOMING_ITEM;
        String isSent = item.status;

        if (isSent.equals(Constant.MESSAGE_STATUS_RECEIVED)) {
            return INCOMING_ITEM;
        } else {
            return OUTGOING_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mMessageItems.size();
    }

    public interface OnItemMessageClickListener {
        void onItemClick(int pos);
    }
}
