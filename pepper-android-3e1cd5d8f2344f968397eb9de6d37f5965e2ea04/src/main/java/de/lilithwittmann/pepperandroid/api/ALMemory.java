package de.lilithwittmann.pepperandroid.api;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.Session;

import de.lilithwittmann.pepperandroid.PepperAPI;
import de.lilithwittmann.pepperandroid.PepperSession;
import de.lilithwittmann.pepperandroid.api.Interfaces.EventCallback;

/**
 * Created by dea on 21/06/17.
 */
public class ALMemory extends ALMemoryHelper {
	public ALMemory(PepperSession session) throws Exception {
		super(session, "ALMemory");
	}

	public ALMemory(Session session) throws Exception {
		super(session, "ALMemory");
	}

	@Override
	public Future<Object> subscribeToEvent(String event, EventCallback eventCallback) {
		return this.API.call("subscribeToEvent", eventCallback);
	}

	@Override
	public void unsubscribeToEvent(long eventId) {
		this.API.call("unsubscribeToEvent", eventId);
	}

}
