package com.protocollabs.android.cellperf;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;

public class Speech
{
    private TextToSpeech myTTS;
    private boolean readyToSpeak = false;
    private Context context;

    public Speech(Context baseContext) {
        this.context = baseContext;
        initOrInstallTTS();
    }

    public void shutdown() {
        if (myTTS != null)
            myTTS.shutdown();
    }


    public void initOrInstallTTS() {
        myTTS = new TextToSpeech(context, new OnInitListener() {               
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    myTTS.setLanguage(Locale.US);
                    readyToSpeak = true;
                } else {
                    installTTS();
                }
            }
        });
    }

    private void installTTS() {
        Intent installIntent = new Intent();
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        context.startActivity(installIntent);
    }

    public void speak(String text) {       
        if (readyToSpeak)
            myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}
