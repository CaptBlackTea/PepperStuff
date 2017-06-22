package de.lilithwittmann.pepperandroid;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.Future;
import com.aldebaran.qi.Session;

import de.lilithwittmann.pepperandroid.api.ALMemory;
import de.lilithwittmann.pepperandroid.api.ALTextToSpeech;
import de.lilithwittmann.pepperandroid.api.Interfaces.EventCallback;
import de.lilithwittmann.pepperandroid.api.ReactToEvents;
import de.lilithwittmann.pepperandroid.api.exceptions.CallError;

/**
 * Created by dea on 22/06/17.
 */

public class RunPepperApplication {

	public static void main(String[] args) throws Exception {

		String robotUrl = "tcp://nao.local:9559";
		// Create a new application
		System.out.println("##### Main started");
		Application application = new Application(args, robotUrl);
		// Start your application
		System.out.println("##### Application.Session(): " + application.session().toString());
		application.start();
		System.out.println("Successfully connected to the robot");
		// Subscribe to selected ALMemory events
		RunPepperApplication reactor = new RunPepperApplication();
		reactor.run(application.session());
		System.out
				.println("Subscribed to FrontTactilTouched and RearTactilTouched.");
		// Run your application
		application.run();

	}

		private ALMemory memory;
		private ALTextToSpeech tts;
		private long frontTactilSubscriptionId;

		public void run(Session session) throws Exception {
			memory = new ALMemory(session);
			tts = new ALTextToSpeech(new PepperSession());
			frontTactilSubscriptionId = 0;


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
