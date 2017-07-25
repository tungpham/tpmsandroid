package com.ethan.morephone.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.morephone.data.entity.register.BindingRequest;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiMorePhone;
import com.ethan.morephone.Constant;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.presentation.numbers.IncomingPhoneNumbersFragment;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 7/25/17.
 */

public class BindingIntentService extends IntentService {

    private static final String TAG = "BindingIntentService";
    private static final String FCM_BINDING_TYPE = "fcm";

    private Intent bindingResultIntent;

    public BindingIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        bindingResultIntent = new Intent(IncomingPhoneNumbersFragment.BINDING_REGISTRATION);

        // Set the new binding identity
        String newIdentity = intent.getStringExtra(Constant.EXTRA_IDENTITY);

        // Load the old binding values from shared preferences if they exist
        String identity = MyPreference.getIdentity(getApplicationContext());
        String address = MyPreference.getAddress(getApplicationContext());

        if (newIdentity == null) {
            // If no identity was provided to us then we use the identity stored in shared preferences.
            if (identity != null) {
                newIdentity = identity;
            } else {
                /*
                 * When the application is first installed onTokenRefresh() may be called without the
                 * user providing an identity. In this case we ignore the request to bind.
                 */
                Log.w(TAG, "No identity was provided.");
                return;
            }
        }

        String endpoint = MyPreference.getEndpoint(getApplicationContext(), newIdentity);

        /*
         * Obtain the new address based off the Firebase instance token
         */
        String newAddress = FirebaseInstanceId.getInstance().getToken();

        /*
         * Check whether a new binding registration is required by comparing the prior values that
         * were stored in shared preferences after the last successful binding registration.
         */
        boolean sameBinding =
                newIdentity.equals(identity) && newAddress.equals(address);

        if (newIdentity == null) {
            DebugTool.logD( "A new binding registration was not performed because" +
                    " the identity cannot be null.");
//            bindingResultIntent.putExtra(MainActivity.BINDING_SUCCEEDED, false);
//            bindingResultIntent.putExtra(MainActivity.BINDING_RESPONSE, "Binding identity was null");
            // Notify the MainActivity that the registration ended
            LocalBroadcastManager.getInstance(BindingIntentService.this)
                    .sendBroadcast(bindingResultIntent);
        } else if (sameBinding) {
            DebugTool.logD( "A new binding registration was not performed because" +
                    "the binding values are the same as the last registered binding.");
//            bindingResultIntent.putExtra(MainActivity.BINDING_SUCCEEDED, true);
//            bindingResultIntent.putExtra(MainActivity.BINDING_RESPONSE, "Binding already registered");
            // Notify the MainActivity that the registration ended
            LocalBroadcastManager.getInstance(BindingIntentService.this)
                    .sendBroadcast(bindingResultIntent);
        } else {
            /*
             * Clear all the existing bindings from SharedPreferences and attempt to register
             * the new binding values.
             */
            MyPreference.removeIdentity(getApplicationContext());
            MyPreference.removeAddress(getApplicationContext());
            MyPreference.removeEndpoint(getApplicationContext(), newIdentity);
//            endpoint = UUID.randomUUID().toString();
            final BindingRequest binding = new BindingRequest(newIdentity, endpoint,newAddress, FCM_BINDING_TYPE);
            DebugTool.logD(" ADDRESS = " + newAddress);
            DebugTool.logD(" endpoint = " + endpoint);
            DebugTool.logD( " newIdentity = " + newIdentity);
            registerBinding(binding);
        }
    }

    /**
     * Register the binding with Twilio Notify via your application server.
     */
    private void registerBinding(final BindingRequest binding) {
        /*
         * Issue the request to your application server and wait for the result in the callback
         */
        DebugTool.logD("Binding with" +
                " identity: " + binding.identity +
                " endpoint: " + binding.endpoint +
                " address: " + binding.address);


        ApiMorePhone.binding(getApplicationContext(), binding, new Callback<com.android.morephone.data.entity.Response>() {
            @Override
            public void onResponse(Call<com.android.morephone.data.entity.Response> call, Response<com.android.morephone.data.entity.Response> response) {
                if(response.isSuccessful()) {
                    // Store binding in SharedPreferences upon success
                    MyPreference.setIdentity(getApplicationContext(), binding.identity);
                    MyPreference.setAddress(getApplicationContext(), binding.address);
                    MyPreference.setEndpoint(getApplicationContext(), response.body().error, binding.identity);

//                    bindingResultIntent.putExtra(MainActivity.BINDING_SUCCEEDED, true);
                } else {
                    String message = "Binding failed " + response.code() + " " + response.message();
                    Log.e(TAG, message);
//                    bindingResultIntent.putExtra(MainActivity.BINDING_SUCCEEDED, false);
//                    bindingResultIntent.putExtra(MainActivity.BINDING_RESPONSE, message);
                }

                // Notify the MainActivity that the registration ended
                LocalBroadcastManager.getInstance(BindingIntentService.this)
                        .sendBroadcast(bindingResultIntent);
            }

            @Override
            public void onFailure(Call<com.android.morephone.data.entity.Response> call, Throwable t) {
                String message = "Binding failed " + t.getMessage();
                Log.e(TAG, message);
//                bindingResultIntent.putExtra(MainActivity.BINDING_SUCCEEDED, false);
//                bindingResultIntent.putExtra(MainActivity.BINDING_RESPONSE, message);
                // Notify the MainActivity that the registration ended
                LocalBroadcastManager.getInstance(BindingIntentService.this)
                        .sendBroadcast(bindingResultIntent);
            }
        });
    }

}
