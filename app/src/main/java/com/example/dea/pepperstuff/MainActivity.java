package com.example.dea.pepperstuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.aldebaran.qi.Application;

import de.lilithwittmann.pepperandroid.PepperSession;
import de.lilithwittmann.pepperandroid.RunPepperApplication;
import de.lilithwittmann.pepperandroid.api.ALTextToSpeech;
import de.lilithwittmann.pepperandroid.api.ReactToEvents;
import de.lilithwittmann.pepperandroid.api.exceptions.CallError;
import de.lilithwittmann.pepperandroid.api.model.Naoqi;


public class MainActivity extends AppCompatActivity {
	private static final String TAG = "Pepper Main";
	private AlertDialog.Builder alertDialogBuilder;
	private PepperSession pepperSession;
	private ReactToEvents reactor;
	private String robotURL = "tcp://198.18.0.1:9559";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		alertDialogBuilder = new AlertDialog.Builder(this);

		// new Session for interacting
		startPepperSession();
		Log.d(TAG, "Pepper session started");

		Log.d(TAG, "==> new Reactor...?");
		// Subscribe to selected ALMemory events
		try {
			reactor = new ReactToEvents(pepperSession);
//			String[] args = new String[2];
//			args[0] = "--qi-url";
//			args[1] = robotURL;
//
//			RunPepperApplication.main(args);
			Log.d(TAG, "==> new Reactor");

			Log.d(TAG, "==> Trying to subscribe to touch event...");

//			reactor.headFrontBackTouchPingPong();


			Log.d(TAG, "==> SUBSCRIBED to touch event");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private PepperSession startPepperSession() {
		pepperSession = new PepperSession();
		pepperSession.connect();
		Log.d(TAG, "==== Pepper connected: " + pepperSession.isConnected());
		return pepperSession;
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart: Yay");
		pepperSession = new PepperSession();
		pepperSession.connect();
		Log.d(TAG, "==== Pepper connected: " + pepperSession.isConnected());

		try {
			ALTextToSpeech textToSpeech = new ALTextToSpeech(pepperSession);
			textToSpeech.say("I am singing in the rain");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		pepperSession.close();
	}

	public void pushMyBtn(View view) {
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setMessage("Button pushing is Magic thehehehe...\n" +
		"Connected: " + pepperSession.isConnected());
		alertDialog.show();
	}

	public void startAssistenModeBtn(View view) throws Exception {
		Naoqi naoqi = Naoqi.getInstance();
		Log.d(TAG, "#### Started Assistent Mode; Naoqi: " + naoqi);
			Log.d(TAG, "#### Naoqi is running: " + naoqi.isRunning());
		if(naoqi.isRunning()){
			try {
				PepperSession currentSession = (PepperSession) naoqi.getSession();
				ALTextToSpeech alTextToSpeech = new ALTextToSpeech(currentSession);
				alTextToSpeech.say("Hello from Assisitent Mode");
				Log.d(TAG, "#### Say was executed");
			} catch (CallError callError) {
				callError.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
