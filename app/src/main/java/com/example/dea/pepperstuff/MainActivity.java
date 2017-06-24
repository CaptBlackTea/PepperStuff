package com.example.dea.pepperstuff;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.aldebaran.qi.AnyObject;
import com.aldebaran.qi.Future;
import com.aldebaran.qi.FutureFunction;
import com.aldebaran.qi.QiSignalListener;

import java.util.ArrayList;
import java.util.List;

import de.lilithwittmann.pepperandroid.PepperSession;
import de.lilithwittmann.pepperandroid.api.ALDialogProxy;
import de.lilithwittmann.pepperandroid.api.ALMemory;
import de.lilithwittmann.pepperandroid.api.ALSpeechRecognition;
import de.lilithwittmann.pepperandroid.interaction.Say;
import de.lilithwittmann.pepperandroid.interaction.SpeechRecognition;


public class MainActivity extends AppCompatActivity {
	private static final String TAG = "Pepper Main";
	private AlertDialog.Builder alertDialogBuilder;
	private PepperSession pepperSession;
//	private ReactToEvents reactor;
	private String robotURL = "tcp://198.18.0.1:9559";
	private PepperSession pepper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		alertDialogBuilder = new AlertDialog.Builder(this);

//		// new Session for interacting
//		startPepperSession();
//		Log.d(TAG, "Pepper session started");
//
//		Log.d(TAG, "==> new Reactor...?");
//		// Subscribe to selected ALMemory events
//		try {
//			reactor = new ReactToEvents(pepperSession);
////			String[] args = new String[2];
////			args[0] = "--qi-url";
////			args[1] = robotURL;
////
////			RunPepperApplication.main(args);
//			Log.d(TAG, "==> new Reactor");
//
//			Log.d(TAG, "==> Trying to subscribe to touch event...");
//
////			reactor.headFrontBackTouchPingPong();
//
//
//			Log.d(TAG, "==> SUBSCRIBED to touch event");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}


	private PepperSession startPepperSession() {
		pepperSession = new PepperSession(this);
		pepperSession.connect();
		Log.d(TAG, "==== Pepper connected: " + pepperSession.isConnected());
		return pepperSession;
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart: Yay");
		// create a new pepper session
		pepper = new PepperSession(this);
		pepper.connect();

//		pepperSession = new PepperSession();
//		pepperSession.connect();
//		Log.d(TAG, "==== Pepper connected: " + pepperSession.isConnected());
//
//		try {
//			ALTextToSpeech textToSpeech = new ALTextToSpeech(pepperSession);
//			textToSpeech.say("I am singing in the rain");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void pushMyBtn(View view) {
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setMessage("Button pushing is Magic thehehehe...");
		alertDialog.show();
	}

	public void alMemoryTestBtn(View view) throws Exception {
//		// create a new pepper session
//		pepper = new PepperSession();
//		pepper.connect();

		final Say say = new Say(pepper);

		// say something
		say.say("Welcome to Pepper Android");


		say.setLanguage(say.LANGUAGE_GERMAN).andThen(new FutureFunction<Object, Object>() {
			@Override
			public Future<Object> execute(Future<Object> future) throws Exception {
				say.say("Wilkommen bei Pepper für Android");
				return null;
			}
		});

		//subscribe to an event

		AnyObject alm = pepper.getService("ALMemory");
		// subscribe to the event
		AnyObject touchEventListener = (AnyObject) alm.call("subscriber", "TouchChanged").get();

		// connect to the signals
		touchEventListener.connect("signal", new QiSignalListener() {
			@Override
			public void onSignalReceived(Object... objects) {

				for (List<Object> o : (List<List<Object>>) objects[0]) {
					Log.d("Sensor", o.get(0).toString() + "  " + o.get(1).toString());
				}
				try {
					say.say("Oouuh");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	public void asrTestBtn(View view) throws Exception {
		Log.d(TAG, "Pepper connected: " + pepper.isConnected());
		SpeechRecognition speechRecognition = new SpeechRecognition(pepper);
//		ALMemory alm = new ALMemory(pepper);
//		ALSpeechRecognition alsr = new ALSpeechRecognition(pepper);
//		ALDialogProxy alDialogProxy = new ALDialogProxy(pepper);
//
//		speechRecognition.clearActivatedTopics();
//		List<String> topicsList = new ArrayList<String>();
//		topicsList = (List<String>) alDialogProxy.getActivatedTopics().get();
//
//		Log.d(TAG, "Activated Topics: " + alDialogProxy.getActivatedTopics());
//
//		for(String topic : topicsList){
//			Log.d(TAG, "Topic: " + topic);
//			alDialogProxy.deactivateTopic(topic);
//			Log.d(TAG, "Topic deleted");
//
//		}
//
//		Log.d(TAG, "Activated Topics: " + alDialogProxy.getActivatedTopics());

		final Say say = new Say(pepper);
		say.setLanguage(say.LANGUAGE_ENGLISH);


		final ArrayList<String> vocabulary = new ArrayList<String>();
		vocabulary.add("yes");
		vocabulary.add("no");
		vocabulary.add("fuck you");

//		alsr.setVocabulary(vocabulary, true);

		speechRecognition.setVocabulary(vocabulary);

//		AnyObject asrService = (AnyObject) alm.call("subscriber", "WordRecognized").get();
		speechRecognition.connectToSignalReceiver("signal", new QiSignalListener() {
			@Override
			public void onSignalReceived(Object... objects) {
				say.say("I received: ");
//				for (String v : vocabulary) {
//					say.say(v);
//				}
//				say.say("That is it");
				Log.d("ASR", objects[0].toString());
			}
		});


	}


}
