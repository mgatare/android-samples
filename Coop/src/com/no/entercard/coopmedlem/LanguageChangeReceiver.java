package com.no.entercard.coopmedlem;
//package com.entercard.coopmedlem;
//
//import java.util.Locale;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//public class LanguageChangeReceiver extends BroadcastReceiver {
//
//	@Override
//	public void onReceive(Context context, Intent intent) {
//
//		if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
//
//			String language = Locale.getDefault().getLanguage();
//			String country = Locale.getDefault().getCountry();
//
//			Log.d("COOP", ":::language:::" + language);
//			Log.d("COOP", ":::country:::" + country);
//			Log.d("COOP", ":::APPLICATION:::" + ApplicationEx.getInstance());
//
//			if (language.equals("en")) {
//
//				Locale.setDefault(Locale.getDefault());
//				
//			} else if (language.equals("nb")) {
//
//				Locale locale = new Locale("ru");
//				Locale.setDefault(locale);
//				
//			} else if (language.equals("sv")) {
//
//				Locale locale = new Locale("ru");
//				Locale.setDefault(locale);
//				
//			}
//		}
//	}
//}