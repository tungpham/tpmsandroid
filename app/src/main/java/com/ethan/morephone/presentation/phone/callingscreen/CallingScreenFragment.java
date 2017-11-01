package com.ethan.morephone.presentation.phone.callingscreen;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.twilio.client.Connection;
import com.twilio.client.ConnectionListener;
import com.twilio.client.Device;
import com.twilio.client.DeviceListener;
import com.twilio.client.PresenceEvent;
import com.twilio.client.Twilio;

/**
 * Created by Ethan on 4/8/17.
 */

public class CallingScreenFragment extends BaseFragment implements View.OnClickListener,
        DeviceListener, ConnectionListener {

    public static CallingScreenFragment getInstance(Bundle bundle) {
        CallingScreenFragment fragment = new CallingScreenFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";
    private static final String BUNDLE_TO_PHONE_NUMBER = "BUNDLE_TO_PHONE_NUMBER";

    private FloatingActionButton mFloatingButtonMute;
    private Device clientDevice;
    private boolean mMuteMicrophone;

    private Connection activeConnection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //        mFloatingButtonMute = (FloatingActionButton) view.findViewById(R.id.mute_action_fab);
//        mFloatingButtonMute.setOnClickListener(this);


        return inflater.inflate(R.layout.fragment_in_call, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mute_action_fab:
                mMuteMicrophone = !mMuteMicrophone;
//                if (activeConnection != null) {
//                    activeConnection.setMuted(muteMicrophone);
//                }
                if (mMuteMicrophone) {
                    mFloatingButtonMute.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mic_off_red_24px));
                } else {
                    mFloatingButtonMute.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mic_green_24px));
                }
                break;
            default:
                break;
        }
    }

    /*
     * Create a Device or update the capabilities of the current Device
     */
    private void createDevice(String capabilityToken) {
        try {
            if (clientDevice == null) {
                clientDevice = Twilio.createDevice(capabilityToken, this);
                clientDevice.setIncomingSoundEnabled(true);

                Intent intent = new Intent(getActivity(), CallingScreenActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                clientDevice.setIncomingIntent(pendingIntent);
            } else {
                clientDevice.updateCapabilityToken(capabilityToken);
            }


        } catch (Exception e) {
            Toast.makeText(getContext(), "Device error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnecting(Connection connection) {

    }

    @Override
    public void onConnected(Connection connection) {

    }

    @Override
    public void onDisconnected(Connection connection) {

    }

    @Override
    public void onDisconnected(Connection connection, int i, String s) {

    }

    @Override
    public void onStartListening(Device device) {

    }

    @Override
    public void onStopListening(Device device) {

    }

    @Override
    public void onStopListening(Device device, int i, String s) {

    }

    @Override
    public boolean receivePresenceEvents(Device device) {
        return false;
    }

    @Override
    public void onPresenceChanged(Device device, PresenceEvent presenceEvent) {

    }
}
