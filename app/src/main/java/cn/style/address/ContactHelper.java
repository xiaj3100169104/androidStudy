package cn.style.address;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;

import com.style.utils.FormatUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class ContactHelper {

	public static List<UploadPhone> getContacts(Context context) {
		List<UploadPhone> contacts = new ArrayList<>();
		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cur != null) {
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					UploadPhone contact = new UploadPhone();
					String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					contact.setName(name);
					if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
						while (pCur.moveToNext()) {
							String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							phone = phone.replace("-", "").replace("-", "").replace(" ", "").trim();
							if (phone.length() >= 11) {
								phone = phone.substring(phone.length() - 11, phone.length());
								if (FormatUtil.isMobileNum(phone)) {
									if (null == contact.getTelephone()) {
										boolean inResult = false;
										for (UploadPhone up : contacts) {
											if (phone.equals(up.getTelephone())) {
												inResult = true;
												break;
											}
										}
										if (!inResult) {
											contact.setTelephone(phone);
											contacts.add(contact);
										}
									}
								}
							}
						}
						pCur.close();
					}
				}
			}
			cur.close();
		}
		return contacts;
	}

	public List<MyCallLog> queryCalls(Context context) {
		List<MyCallLog> calls = new ArrayList<>();;
		Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
		while (cursor.moveToNext()) {
			MyCallLog call = new MyCallLog();
			call.name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME)) + " ";
			String date = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
			call.date = formatTime(Long.parseLong(date));
			call.phone = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
			call.type = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));
			calls.add(call);
			if (call.name.equals("null ")) {
				call.setName(call.getPhone());
			}
		}
		cursor.close();
		return calls;
	}

	public String formatTime(long date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		// Date currentTime = new Date(java.lang.System.currentTimeMillis());
		String dateString = sdf.format(date);
		return dateString;
	}
}
