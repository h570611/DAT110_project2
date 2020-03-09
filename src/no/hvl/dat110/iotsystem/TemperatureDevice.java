package no.hvl.dat110.iotsystem;

import no.hvl.dat110.broker.ClientSession;
import no.hvl.dat110.client.Client;
import no.hvl.dat110.common.TODO;

public class TemperatureDevice {

	private static final int COUNT = 10;

	public static void main(String[] args) {

		// simulated / virtual temperature sensor
		TemperatureSensor sn = new TemperatureSensor();

		// TODO - start

		// create a client object and use it to
		Client klient = new Client("tempDevice", Common.BROKERHOST, Common.BROKERPORT);
		// - connect to the broker
		klient.connect();
		// - publish the temperature(s)
		for(int i = 0; i < COUNT; i++) {
//			int temp = Integer.toString(sn.read());
			klient.publish(Common.TEMPTOPIC, Integer.toString(sn.read()));
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// - disconnect from the broker
		klient.disconnect();

		// TODO - end

		System.out.println("Temperature device stopping ... ");


	}
}
