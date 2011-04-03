package com.thinkers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Calling extends Activity implements TextToSpeech.OnInitListener {

    private static final String TAG = "TextToSpeechDemo";
    private TextToSpeech mTts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTts = new TextToSpeech(this, this);

        BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                String phoneNr = bundle.getString("incoming_number");
                if(phoneNr!=null){
                    Log.v(TAG, "Before reading out loud");
                    Log.v(TAG, "phoneNr: " + phoneNr);
                    saySomething("Getting call from "+ phoneNr);
                }
            }
        };
        registerReceiver(connectionReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language is not available.");
            }
        } else {
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }

    public void saySomething(String str) {
        mTts.speak(str,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

}