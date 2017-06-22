package de.lilithwittmann.pepperandroid.api;

import android.content.SyncStatusObserver;
import android.util.Log;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.Session;

import java.util.logging.Logger;

import de.lilithwittmann.pepperandroid.PepperAPI;
import de.lilithwittmann.pepperandroid.PepperSession;
import de.lilithwittmann.pepperandroid.api.Interfaces.EventCallback;
import de.lilithwittmann.pepperandroid.api.exceptions.CallError;

/**
 * Created by dea on 21/06/17.
 */

public class ReactToEvents extends PepperAPI{

	ALMemory memory;
	ALTextToSpeech tts;
	long frontTactilSubscriptionId = 0;

	public ReactToEvents(PepperSession session) throws Exception {
		super(session, "ReactToEvents");
		memory = new ALMemory(session);

	}


	public void headFrontBackTouchPingPong() {
		// Subscribe to FrontTactilTouched event,
		// create an EventCallback expecting a Float.
		Future<Object> objectFromCallback = memory.subscribeToEvent(
				"FrontTactilTouched", new EventCallback<Float>() {
					@Override
					public void onEvent(Float arg0)
							throws InterruptedException, CallError {
						// 1 means the sensor has been pressed
						if (arg0 > 0) {
							tts.say("ouch!");
							System.out.println("=== FRONT PRESSED ===");
						}
					}
				});

		frontTactilSubscriptionId = Long.parseLong(String.valueOf(objectFromCallback));
		// Subscribe to RearTactilTouched event,
		// create an EventCallback expecting a Float.
		memory.subscribeToEvent("RearTactilTouched",
				new EventCallback<Float>() {
					@Override
					public void onEvent(Float arg0)
							throws InterruptedException, CallError {
						if (arg0 > 0) {
							if (frontTactilSubscriptionId > 0) {
								tts.say("I'll no longer say ouch");
								// Unsubscribing from FrontTactilTouched event
								memory.unsubscribeToEvent(frontTactilSubscriptionId);
							}
						}
					}
				});
	}

}
