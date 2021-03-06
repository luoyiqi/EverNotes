/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package com.evernote.edam.type;

import com.evernote.thrift.TEnum;

/**
 * An enumeration describing the configuration state related to receiving
 * reminder e-mails from the service.  Reminder e-mails summarize notes
 * based on their Note.attributes.reminderTime values.
 * 
 * DO_NOT_SEND: The user has selected to not receive reminder e-mail.
 * 
 * SEND_DAILY_EMAIL: The user has selected to receive reminder e-mail for those
 *   days when there is a reminder.
 */
public enum ReminderEmailConfig implements TEnum {
	DO_NOT_SEND(1), SEND_DAILY_EMAIL(2);

	private final int value;

	private ReminderEmailConfig(int value) {
		this.value = value;
	}

	/**
	 * Get the integer value of this enum value, as defined in the Thrift IDL.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Find a the enum type by its integer value, as defined in the Thrift IDL.
	 * @return null if the value is not found.
	 */
	public static ReminderEmailConfig findByValue(int value) {
		switch (value) {
		case 1:
			return DO_NOT_SEND;
		case 2:
			return SEND_DAILY_EMAIL;
		default:
			return null;
		}
	}
}
