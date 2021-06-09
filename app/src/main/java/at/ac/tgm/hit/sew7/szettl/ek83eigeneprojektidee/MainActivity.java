package at.ac.tgm.hit.sew7.szettl.ek83eigeneprojektidee;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Die Main Activity die Befehle durch SpeechToText übernimmt.
 * @author Sebastian Zettl
 * @version 2021-06-08
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private EditText commandLine;
    TextToSpeech tts;

    /**
     * Methode die aufgerufen wird wenn die Activity erstellt wird
     * @param savedInstanceState Ein saved Instance State
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commandLine = (EditText) findViewById(R.id.editTextTextPersonName);

        // Wir fügen einen initListener dazu, demit die Engine initialiert werden kann
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // Wir checken nach ob es einen Fehler gab
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK); // Wir setzen die Sprache
                }
            }
        });

    }

    /**
     * Diese Methode startet TextToSpeech.
     * @param view Die View die die Methode aufgerufen hat
     */
    public void startTextToSpeech(View view) {
        // Spreche den String, schmeiß alle anderen dinge aus der Queue heraus, kein Bundle und eine eindeutige Id
        tts.speak(commandLine.getText().toString(),TextToSpeech.QUEUE_FLUSH,null, "id1");
    }

    /**
     * Der Befehl wird per Button ausgeführt
     * @param view Die View die die Methode aufgerufen hat
     */
    public void startCommandsButton(View view) {
        startCommands(commandLine.getText().toString());
    }

    /**
     * Fürht verschiedene Commandos aus
     * @param command Der command welcher ausgeführt werden soll
     */
    private void startCommands(String command) {
        command = command.toLowerCase();
        ImageView bild = (ImageView) findViewById(R.id.imageView);

        switch(command) {
            case "scale":
                Animation scaleAnimation = new ScaleAnimation(1,2,1, 1.5f);
                scaleAnimation.setDuration(1000);
                bild.startAnimation(scaleAnimation);
                break;
            case "translate":
                Animation bewegenAnimation = new TranslateAnimation(0,100,0, -50);
                bewegenAnimation.setDuration(1500);
                bild.startAnimation(bewegenAnimation);
                break;
            case "rotate":
                Animation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 24.0f, 24.0f);
                rotateAnimation.setDuration(2000);
                bild.startAnimation(rotateAnimation);
                break;
            case "play sound":
                break;
            default:
        }
    }

    /**
     * Diese Methode startet einen Intent, welchen die Google Voice Recognision startet.
     * @param view Die View die die Methode aufgerufen hat
     */
    public void startSpeechRecognision(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // Erstelle einen neuen Intent der den Voice Recognion Service startet
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // Here we say how the Recognisior is supposed to interpret the Result
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()); // Here we say what Language it is supposued to hear, The Locale one
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, please say a command."); // Here we set the Prompt that should appear on the Recognisior
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT); // Here starten wir die Voice Recogision Activity mit einem eindeutigen Rquestcode
        } catch (ActivityNotFoundException a) {

        }
    }

    /**
     * Hier warten wir auf das Ergebnis einer Activity
     * @param requestCode Der Requestcode
     * @param resultCode Der Resultcode
     * @param data Die Intent Data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Wir checken den requestcode
        switch (requestCode) {
            // DER Code den wir beim starten übergeben haben
            case REQ_CODE_SPEECH_INPUT: {
                // Dann checken wir nach ob das Result ok ist und wir auch wirklich data haben
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS); // Wir können und die Daten in ein Array reinspeichern lassen
                    commandLine.setText(result.get(0)); // Dieses können wir dann in die View setzen.
                    startCommands(result.get(0)); // Führe den Command aus
                }
                break;
            }

        }
    }
}