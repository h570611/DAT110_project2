package no.hvl.dat110.broker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import no.hvl.dat110.common.TODO;
import no.hvl.dat110.messages.CreateTopicMsg;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.common.Logger;
import no.hvl.dat110.messagetransport.Connection;

public class Storage {

	// data structure for managing subscriptions
	// maps from a topic to set of subscribed users
	protected ConcurrentHashMap<String, Set<String>> subscriptions;

	// data structure for managing currently connected clients
	// maps from user to corresponding client session object

	protected ConcurrentHashMap<String, ClientSession> clients;
	
	//Oppgåve E. Liste for å vite om bruker er connected
	protected ConcurrentHashMap<String, Boolean> isConnected;
	
	//Liste for messagebuffers
	protected ConcurrentHashMap<String, ArrayList<Message>> messageBuffer;

	public Storage() {
		subscriptions = new ConcurrentHashMap<String, Set<String>>();
		clients = new ConcurrentHashMap<String, ClientSession>();
		isConnected = new ConcurrentHashMap<String, Boolean>();
		messageBuffer = new ConcurrentHashMap<String, ArrayList<Message>>();
	}

	public Collection<ClientSession> getSessions() {
		return clients.values();
	}

	public Set<String> getTopics() {

		return subscriptions.keySet();

	}
	
	public boolean isConnected(String user) {
		return isConnected(user);
	}

	// get the session object for a given user
	// session object can be used to send a message to the user

	public ClientSession getSession(String user) {

		ClientSession session = clients.get(user);

		return session;
	}

	public Set<String> getSubscribers(String topic) {

		return (subscriptions.get(topic));

	}

	public void addClientSession(String user, Connection connection) {

		// TODO: add corresponding client session to the storage
		if (!clients.containsKey(user)) {
			isConnected.put(user, true);
			messageBuffer.put(user, new ArrayList<Message>());
			ClientSession cl = new ClientSession(user, connection);
			clients.put(user, cl);
		} 

	}

	public void removeClientSession(String user) {

		// TODO: remove client session for user from the storage
		ClientSession cS = clients.get(user);
		
		if(cS != null) {
			cS.disconnect();
			clients.remove(user);
		}
		
	}

	public void createTopic(String topic) {

		// TODO: create topic in the storage
		subscriptions.put(topic, new HashSet<String>());


	}

	public void deleteTopic(String topic) {

		subscriptions.remove(topic);

	}

	public void addSubscriber(String user, String topic) {

		// TODO: add the user as subscriber to the topic
//
//		Set<String> nySet = subscriptions.get(topic);
//		if(!nySet.contains(user)) {
//			nySet.add(user);
////			subscriptions.put(topic, nySet);
			subscriptions.get(topic).add(user);
	}

	public void removeSubscriber(String user, String topic) {

		// TODO: remove the user as subscriber to the topic
		Set<String> nySet = subscriptions.get(topic);
		if(nySet.contains(user)) {
			nySet.remove(user);
			subscriptions.put(topic, nySet);
		}
		
	}
	public void disconnect(String user) {
		isConnected.put(user, false);
		clients.get(user).disconnect();
	}
	
	public void reconnect(String user, Connection connection) {
		isConnected.put(user, true);
		ClientSession cS = new ClientSession(user, connection);
		clients.put(user, cS);
	}
	public void messageToBuffer(String user, Message message) {
		messageBuffer.get(user).add(message);
	}
	public ArrayList<Message> getMessageFromBuffer(String user) {
		return messageBuffer.get(user);
	}
	public void clearBuffer(String user) {
		messageBuffer.get(user).clear();
	}
}
