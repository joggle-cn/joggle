package com.wuweibi.bullet.client.network.wol;

import com.wuweibi.bullet.client.network.wol.exceptions.UnableToWakeUpWOLNodeException;
import com.wuweibi.bullet.client.network.wol.validator.MacAddressValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class WOLNode.
 */
public class WOLNode {

	/** The log. */
	private static Log log = LogFactory.getLog(WOLNode.class);

	/** The Constant EMPTY_STIRNG. */
	private static final String EMPTY_STIRNG = "";

	/** The Constant BROADCAST_IP. */
	private static final String BROADCAST_IP = "255.255.255.255";

	/** The Constant WOL_PORT. */
	private static final int WOL_PORT = 9;

	/** The mac address. */
	private String macAddress = null;

	/** The broadcast ip. */
	private String broadcastIP = null;

	/** The broadcast port. */
	private int broadcastPort;

	/**
	 * Instantiates a new wOL node.
	 * 
	 * @param macAddress
	 *            the mac address
	 */
	public WOLNode(String macAddress) {
		super();
		this.macAddress = macAddress;
		this.broadcastIP = BROADCAST_IP;
		this.broadcastPort = WOL_PORT;
	}

	/**
	 * Instantiates a new wOL node.
	 *
	 * @param macAddress the mac address
	 * @param broadcastIP the broadcast ip
	 * @param broadcastPort the broadcast port
	 */
	public WOLNode(String macAddress, String broadcastIP, int broadcastPort) {
		super();
		this.macAddress = macAddress;
		this.broadcastIP = broadcastIP;
		this.broadcastPort = broadcastPort;
	}

	/**
	 * Wake up.
	 *
	 * @throws UnableToWakeUpWOLNodeException the unable to wake up wol node exception
	 */
	public void wakeUP() throws UnableToWakeUpWOLNodeException {
		log.debug("wakeUP():start");
		try {
			validate();
			send();
		} catch (Exception e) {
			log.error("An error has occurred while sending the magic package.",
					e);
			throw new UnableToWakeUpWOLNodeException(
					"An error has occurred while sending the magic package.", e);
		} finally {
			log.debug("wakeUP():end");
		}

	}

	/**
	 * Validate.
	 * 
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
	private void validate() throws IllegalArgumentException {
		log.debug("validate():start");
		if (this.macAddress == null || this.macAddress != null
				&& EMPTY_STIRNG.equals(this.macAddress.trim())) {
			log.debug("validate():end");
			throw new IllegalArgumentException(
					"The Mac address must be provided.");
		}

		if (!MacAddressValidator.validate(macAddress)) {
			log.debug("validate():start");
			throw new IllegalArgumentException("Invalid MAC address.");
		}

	}

	/**
	 * Send.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void send() throws Exception {
		DatagramSocket socket = null;
		try {
			byte[] macBytes = getMacBytes();
			byte[] bytes = new byte[6 + 16 * macBytes.length];
			for (int i = 0; i < 6; i++) {
				bytes[i] = (byte) 0xff;
			}
			for (int i = 6; i < bytes.length; i += macBytes.length) {
				System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
			}

			InetAddress address = InetAddress.getByName(this.broadcastIP);
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length,
					address, this.broadcastPort);
			socket = new DatagramSocket();
			socket.send(packet);
			socket.close();
		} catch (Exception e) {
			log.error("An error has occurred while sending the datagram.", e);
			throw e;
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
	}

	/**
	 * Clean.
	 * 
	 * @return the string
	 */
	private void clean() {
		macAddress = macAddress.replaceAll("\\:|\\.|\\-", "");
	}

	/**
	 * Split.
	 * 
	 * @return the string[]
	 */
	private String[] split() {
		List<String> splittedMac = new ArrayList<String>();
		String[] result = new String[6];
		for (int i = 0; i < macAddress.length(); i = i + 2) {
			splittedMac.add(macAddress.substring(i, i + 2));
		}
		splittedMac.toArray(result);
		return result;
	}

	/**
	 * Gets the mac bytes.
	 * 
	 * @return the mac bytes
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
	private byte[] getMacBytes() throws IllegalArgumentException {
		clean();
		byte[] bytes = new byte[6];
		String[] hex = split();
		for (int i = 0; i < 6; i++) {
			bytes[i] = (byte) Integer.parseInt(hex[i], 16);
		}
		return bytes;
	}

	/**
	 * Creates the.
	 * 
	 * @param macAddress
	 *            the mac address
	 * @return the wOL node
	 */
	public static WOLNode create(String macAddress) {
		return new WOLNode(macAddress);
	}

	/**
	 * Creates the.
	 * 
	 * @param macAddress
	 *            the mac address
	 * @param broadcast
	 *            the broadcast
	 * @param port
	 *            the port
	 * @return the wOL node
	 */
	public static WOLNode create(String macAddress, String broadcast, int port) {
		return new WOLNode(macAddress, broadcast, port);
	}

	/**
	 * Creates a WOLNode.
	 *
	 * @param macAddress the mac address
	 * @param broadcast the broadcast
	 * @return the wOL node
	 */
	public static WOLNode create(String macAddress, String broadcast) {
		return new WOLNode(macAddress, broadcast, WOL_PORT);
	}

}
