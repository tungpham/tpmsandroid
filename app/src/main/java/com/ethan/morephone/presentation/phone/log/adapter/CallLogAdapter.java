package com.ethan.morephone.presentation.phone.log.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.morephone.data.entity.call.Call;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.repository.contact.source.GetContactCallback;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.contact.GetContactByPhoneNumber;
import com.ethan.morephone.R;
import com.ethan.morephone.utils.ContactUtil;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.TextDrawable;

import java.util.Collections;
import java.util.List;

/**
 * Created by Ethan on 3/16/17.
 */

public class CallLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_LOADING = 1;

    private List<Call> mCalls;
    private TextDrawable.IBuilder mDrawableBuilder;
    private Context mContext;
    private OnItemCallLogClickListener mOnItemCallLogClickListener;
    private UseCaseHandler mUseCaseHandler;
    private GetContactByPhoneNumber mGetContactByPhoneNumber;

    public CallLogAdapter(Context context, String phoneNumber, List<Call> conversationEntities, OnItemCallLogClickListener onItemCallLogClickListener) {
        mContext = context;
//        String mPhoneNumber = phoneNumber;
        mCalls = conversationEntities;
        mOnItemCallLogClickListener = onItemCallLogClickListener;
        mDrawableBuilder = TextDrawable.builder().round();

        mUseCaseHandler = Injection.providerUseCaseHandler();
        mGetContactByPhoneNumber = Injection.providerGetContactByPhoneNumber(context);
    }

    public void replaceData(List<Call> calls) {
        Collections.sort(calls);
        mCalls = calls;
        notifyDataSetChanged();
    }

    public List<Call> getData() {
        return mCalls;
    }


    @Override
    public int getItemViewType(int position) {
        int VIEW_TYPE_ITEM = 0;
        return mCalls.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more, parent, false);
            DebugTool.logD("LOADING....");
            return new LoadingViewHolder(view);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_log, parent, false);
            return new CallLogViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof CallLogViewHolder) {
            String phoneNumber = "";
            final CallLogViewHolder holder = (CallLogViewHolder) viewHolder;
            final Call callEntity = mCalls.get(position);
            if (callEntity.direction.endsWith("outbound-dial")) {
                holder.textPhoneNumber.setText(callEntity.toFormatted);
                phoneNumber = callEntity.toFormatted;
                holder.imageStatus.setImageResource(R.drawable.ic_call_outgoing_holo_dark);
            } else if (callEntity.direction.endsWith("inbound")) {
                holder.textPhoneNumber.setText(callEntity.fromFormatted);
                phoneNumber = callEntity.fromFormatted;
                holder.imageStatus.setImageResource(R.drawable.ic_call_incoming_holo_dark);
            } else {
                DebugTool.logD("COME NOW");
            }

            ContactUtil.getContactDisplay(holder.textPhoneNumber, mUseCaseHandler, mGetContactByPhoneNumber, phoneNumber, new GetContactCallback() {
                @Override
                public void onContactLoaded(View view, Contact contact) {
                    holder.contact = contact;
                    if(view instanceof TextView){
                        ((TextView) view).setText(contact.getDisplayName());
                    }
                }

                @Override
                public void onContactNotAvailable() {

                }
            });

            holder.textTime.setText(Utils.formatDate(callEntity.dateCreated));

            holder.imageIcon.setImageDrawable(mDrawableBuilder.build(String.valueOf(callEntity.from.charAt(0)), ContextCompat.getColor(mContext, R.color.colorBackgroundAvatar)));


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemCallLogClickListener.onCallClick(callEntity);
                }
            });

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(holder.contact != null) {
                        mOnItemCallLogClickListener.onCallLongClick(position, holder.contact.getId());
                    }else{
                        mOnItemCallLogClickListener.onCallLongClick(position, "");
                    }
                    return false;
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return mCalls.size();
    }

    public interface OnItemCallLogClickListener {
        void onCallClick(Call call);
        void onCallLongClick(int pos, String contactId);
    }
}
