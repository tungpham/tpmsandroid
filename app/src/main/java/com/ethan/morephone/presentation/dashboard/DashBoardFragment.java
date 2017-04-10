package com.ethan.morephone.presentation.dashboard;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.dashboard.adapter.DashboardViewPagerAdapter;
import com.ethan.morephone.presentation.dashboard.model.ClientProfile;
import com.ethan.morephone.presentation.phone.incall.InCallActivity;
import com.ethan.morephone.widget.NavigationTabStrip;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.twilio.client.Device;
import com.twilio.client.DeviceListener;
import com.twilio.client.PresenceEvent;
import com.twilio.client.Twilio;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Ethan on 3/23/17.
 */

public class DashboardFragment extends BaseFragment implements DeviceListener {

    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";

    public static DashboardFragment getInstance(String phoneNumber) {
        DashboardFragment dashboardFragment = new DashboardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER, phoneNumber);
        dashboardFragment.setArguments(bundle);
        return dashboardFragment;
    }

    private static final String TOKEN_SERVICE_URL = "https://numberphone1.herokuapp.com/token";

    private final int MIC_PERMISSION_REQUEST_CODE = 11;

    private String mPhoneNumber;

    private ClientProfile clientProfile;

    private Device clientDevice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mPhoneNumber = getArguments().getString(BUNDLE_PHONE_NUMBER);

        setUpViewPager(view);

        setHasOptionsMenu(true);

        clientProfile = new ClientProfile(mPhoneNumber, true, true);

        if (!checkPermissionForMicrophone()) {
            requestPermissionForMicrophone();
        } else {
            initializeTwilioClientSDK();
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*
         * Check if microphone permissions is granted
         */
        if (requestCode == MIC_PERMISSION_REQUEST_CODE && permissions.length > 0) {
            boolean granted = true;
            if (granted) {
                /*
                * Initialize the Twilio Client SDK
                */
                initializeTwilioClientSDK();
            } else {
                Toast.makeText(getContext(),
                        "Microphone permissions needed. Please allow in App Settings for additional functionality.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setUpViewPager(View view) {
        NavigationTabStrip navigationTabStrip = (NavigationTabStrip) view.findViewById(R.id.tab_strip);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        DebugTool.logD("PHONE: " + mPhoneNumber);
        DashboardViewPagerAdapter myViewPagerAdapter = new DashboardViewPagerAdapter(getChildFragmentManager(), mPhoneNumber);
        viewPager.setAdapter(myViewPagerAdapter);
        navigationTabStrip.setViewPager(viewPager, 0);
    }

    private boolean checkPermissionForMicrophone() {
        int resultMic = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
        if (resultMic == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissionForMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(getContext(),
                    "Microphone permissions needed. Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MIC_PERMISSION_REQUEST_CODE);
        }
    }

    private void initializeTwilioClientSDK() {

        if (!Twilio.isInitialized()) {
            Twilio.initialize(getContext(), new Twilio.InitListener() {

                /*
                 * Now that the SDK is initialized we can register using a Capability Token.
                 * A Capability Token is a JSON Web Token (JWT) that specifies how an associated Device
                 * can interact with Twilio services.
                 */
                @Override
                public void onInitialized() {
                    Twilio.setLogLevel(Log.DEBUG);
                    /*
                     * Retrieve the Capability Token from your own web server
                     */
                    retrieveCapabilityToken(clientProfile);
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getContext(), "Failed to initialize the Twilio Client SDK", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void retrieveCapabilityToken(final ClientProfile newClientProfile) {

        // Correlate desired properties of the Device (from ClientProfile) to properties of the Capability Token
        Uri.Builder b = Uri.parse(TOKEN_SERVICE_URL).buildUpon();
        if (newClientProfile.isAllowOutgoing()) {
            b.appendQueryParameter("allowOutgoing", newClientProfile.isAllowOutgoing() ? "true" : "false");
        }
        if (newClientProfile.isAllowIncoming() && newClientProfile.getName() != null) {
            b.appendQueryParameter("client", newClientProfile.getName());
        }

        Ion.with(getContext())
                .load(b.toString())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String capabilityToken) {
                        if (e == null) {

                            // Update the current Client Profile to represent current properties
                            clientProfile = newClientProfile;

                            // Create a Device with the Capability Token
                            createDevice(capabilityToken);
                        } else {
                            Toast.makeText(getContext(), "Error retrieving token", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /*
     * Create a Device or update the capabilities of the current Device
     */
    private void createDevice(String capabilityToken) {
        try {
            if (clientDevice == null) {
                clientDevice = Twilio.createDevice(capabilityToken, this);
                clientDevice.setIncomingSoundEnabled(true);

                /*
                 * Providing a PendingIntent to the newly created Device, allowing you to receive incoming calls
                 *
                 *  What you do when you receive the intent depends on the component you set in the Intent.
                 *
                 *  If you're using an Activity, you'll want to override Activity.onNewIntent()
                 *  If you're using a Service, you'll want to override Service.onStartCommand().
                 *  If you're using a BroadcastReceiver, override BroadcastReceiver.onReceive().
                 */

                Intent intent = new Intent(getActivity(), InCallActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                clientDevice.setIncomingIntent(pendingIntent);


            } else {
                clientDevice.updateCapabilityToken(capabilityToken);
            }

            EventBus.getDefault().postSticky(clientDevice);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Device error", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStartListening(Device device) {
        DebugTool.logD("Device has started listening for incoming connections");
    }

    @Override
    public void onStopListening(Device device) {
        DebugTool.logD("Device has stopped listening for incoming connections");
    }

    @Override
    public void onStopListening(Device device, int i, String s) {
        DebugTool.logD(String.format("Device has encountered an error and has stopped" +
                " listening for incoming connections: %s", s));
    }

    @Override
    public boolean receivePresenceEvents(Device device) {
        return false;
    }

    @Override
    public void onPresenceChanged(Device device, PresenceEvent presenceEvent) {

    }


}
