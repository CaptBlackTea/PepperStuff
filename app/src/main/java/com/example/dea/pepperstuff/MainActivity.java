package com.example.dea.pepperstuff;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.aldebaran.qi.AnyObject;
import com.aldebaran.qi.Future;
import com.aldebaran.qi.FutureFunction;
import com.aldebaran.qi.QiSignalConnection;
import com.aldebaran.qi.QiSignalListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.lilithwittmann.pepperandroid.PepperSession;
import de.lilithwittmann.pepperandroid.api.ALDialogProxy;
import de.lilithwittmann.pepperandroid.api.ALTextToSpeech;
import de.lilithwittmann.pepperandroid.interaction.Dialog;
import de.lilithwittmann.pepperandroid.interaction.Say;
import de.lilithwittmann.pepperandroid.interaction.SpeechRecognition;


public class MainActivity extends AppCompatActivity {
	private static final String TAG = "Pepper Main";
	private AlertDialog.Builder alertDialogBuilder;
	private PepperSession pepperSession;
//	private ReactToEvents reactor;
	private String robotURL = "tcp://198.18.0.1:9559";
	private PepperSession pepper;
	private QiSignalConnection qiSignalConnection;
	private SpeechRecognition speechRecognition;

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
		try {
			ALTextToSpeech textToSpeech = new ALTextToSpeech(pepper);
			textToSpeech.say("I am at your service.");
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


		say.setLanguage(Say.LANGUAGE.GERMAN).andThen(new FutureFunction<Object, Object>() {
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
		speechRecognition = new SpeechRecognition(pepper);

		speechRecognition.clearActivatedTopics();

		final Say say = new Say(pepper);
		say.setLanguage(Say.LANGUAGE.ENGLISH);


		final ArrayList<String> vocabulary = new ArrayList<String>();
		vocabulary.add("yes");
		vocabulary.add("no");
		vocabulary.add("fuck you");

		speechRecognition.setVocabulary(vocabulary, true);

		Log.d(TAG, "###### speech recognition start ######");
		qiSignalConnection = speechRecognition.connectToSignalReceiver(new QiSignalListener() {
			@Override
			public void onSignalReceived(Object... objects) {
//				say.say("I received stuff");

				Log.d(TAG, "###### Objects: " + objects.getClass().getSimpleName());
				for(Object o : objects){
					Log.d(TAG, "---- Object.toString: " + o.toString());
				}

				Log.d(TAG, "Equals No: " + objects[0].equals("No"));
				Log.d(TAG, "Equals objects[0]: " + objects[0]);
				List<Object> objArray = (List<Object>) objects[0];
				if(objArray.contains("No")){
					say.say("I heard no");
				}

				Log.d("ASR", objects[0].toString());
			}
		});




	}

	public void closeSpeechRecognition(View view){
		speechRecognition.disconnectFromSignalReceiver(qiSignalConnection);
	}

	public void testDialog(View view) throws Exception {
		Dialog dialog = new Dialog(pepper);
		dialog.setLanguage(Say.LANGUAGE.ENGLISH);

//		this.clearActivatedTopics(dialog);


		String topicString = "topic: ~mytopic()\n" +
				"language: enu\n" +
				"proposal: hello Dea\n" +
				"ul:(hi) nice to meet you on this nice sunday\n";

		String topicString2 = "topic: ~greetings()\n" +
				"language: enu\n" +
				"u: (Hello Nao how are you today) Hello human, I am fine thank you and " +
						"you?\n" +
				"u: ({\"Good morning\"} {Nao} did you sleep * well) No damn! You forgot to switch" +
						" me off!\n" +
				"proposal: human, are you going well ?\n" +
				"u1: (yes) I'm so happy!\n" +
				"u1: (no) I'm so sad\n";


		String topicName = dialog.loadTopicContent(topicString2);
		dialog.subscribe("testDialog");
		dialog.activateTopic(topicName);

		Log.d(TAG, "TopicName: " + topicName.toString());

		List<String> topicsList = dialog.getActivatedTopics();
		Log.d(TAG, "TopicName: " + topicsList);


		dialog.runDialog();

//		dialog.stopDialog();
//
//		dialog.deactivateTopic(topicName.toString());
//
//		dialog.unloadTopic(topicName.toString());

	}

}
