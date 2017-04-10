package com.ethan.morephone.presentation.phone.incall;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.twilio.client.Connection;
import com.twilio.client.ConnectionListener;
import com.twilio.client.Device;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan on 4/10/17.
 */

public class InCallFragment extends BaseFragment implements View.OnClickListener, ConnectionListener {

    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";
    public static final String BUNDLE_TO_PHONE_NUMBER = "BUNDLE_TO_PHONE_NUMBER";

    public static InCallFragment getInstance(Bundle bundle) {
        InCallFragment fragment = new InCallFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private AudioManager audioManager;
    private int savedAudioMode = AudioManager.MODE_INVALID;
    private boolean speakerPhone;

    private Connection activeConnection;
    private Connection pendingConnection;

    private ImageView mImageSpeaker;

    private Device clientDevice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_call, container, false);

        TextView textToPhoneNumber = (TextView) view.findViewById(R.id.text_in_call_phone_number);

        String toPhoneNumber = getArguments().getString(BUNDLE_TO_PHONE_NUMBER);
        textToPhoneNumber.setText(toPhoneNumber);


        mImageSpeaker = (ImageView) view.findViewById(R.id.image_in_call_speaker);
        mImageSpeaker.setOnClickListener(this);

        view.findViewById(R.id.floating_button_in_call_hang_up).setOnClickListener(this);

        audioManager.setSpeakerphoneOn(speakerPhone);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getActivity().getIntent();

        if (intent != null) {
            /*
             * Determine if the receiving Intent has an extra for the incoming connection. If so,
             * remove it from the Intent to prevent handling it again next time the Activity is resumed
             */
            Device device = intent.getParcelableExtra(Device.EXTRA_DEVICE);
            Connection incomingConnection = intent.getParcelableExtra(Device.EXTRA_CONNECTION);

            if (incomingConnection == null && device == null) {
                return;
            }
            intent.removeExtra(Device.EXTRA_DEVICE);
            intent.removeExtra(Device.EXTRA_CONNECTION);

            pendingConnection = incomingConnection;
            pendingConnection.setConnectionListener(this);

            createIncomingCallDialog(getContext());
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_in_call_speaker:
                speakerPhone = !speakerPhone;

                setAudioFocus(true);
                audioManager.setSpeakerphoneOn(speakerPhone);

//                if (speakerPhone) {
//                    mImageSpeaker.
//                } else {
//                    mImageSpeaker.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_speaker_off_black_24dp));
//                }
                break;
            case R.id.floating_button_in_call_hang_up:
                disconnect();
                getActivity().finish();
                break;
            default:
                break;
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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(Device clientDevice) {
        this.clientDevice = clientDevice;
        Bundle bundle = getArguments();
        String toPhoneNumber = bundle.getString(BUNDLE_TO_PHONE_NUMBER);
        makeCall(toPhoneNumber);
    }

    /*
* Create an outgoing connection
*/
    private void makeCall(String contact) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("To", contact);
        if (clientDevice != null) {
            // Create an outgoing connection
            activeConnection = clientDevice.connect(params, this);
//            setCallUI();
        } else {
            Toast.makeText(getContext(), "No existing device", Toast.LENGTH_SHORT).show();
        }
    }

    private void disconnect() {
        if (activeConnection != null) {
            activeConnection.disconnect();
            activeConnection = null;
        }
    }

    private void setAudioFocus(boolean setFocus) {
        if (audioManager != null) {
            if (setFocus) {
                savedAudioMode = audioManager.getMode();
                // Request audio focus before making any device switch.
                audioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                /*
                 * Start by setting MODE_IN_COMMUNICATION as default audio mode. It is
                 * required to be in this mode when playout and/or recording starts for
                 * best possible VoIP performance. Some devices have difficulties with speaker mode
                 * if this is not set.
                 */
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(savedAudioMode);
                audioManager.abandonAudioFocus(null);
            }
        }
    }


    public AlertDialog createIncomingCallDialog(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setIcon(R.drawable.ic_call_black_24dp);
        alertDialogBuilder.setTitle("Incoming Call");
        alertDialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if( activeConnection != null ){
                    activeConnection.disconnect();
                }
                pendingConnection.accept();
                activeConnection = pendingConnection;
                pendingConnection = null;
            }
        });
        alertDialogBuilder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (pendingConnection != null) {
                    pendingConnection.reject();
                }
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setMessage("Incoming call");

        return alertDialogBuilder.create();
    }

}
