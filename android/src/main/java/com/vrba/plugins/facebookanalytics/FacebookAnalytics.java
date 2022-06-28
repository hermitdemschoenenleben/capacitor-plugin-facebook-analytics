package com.vrba.plugins.facebookanalytics;

import android.util.Log;

import com.facebook.appevents.AppEventsConstants;
import com.getcapacitor.Bridge;
import com.getcapacitor.JSObject;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;

import android.os.Bundle;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Iterator;

@CapacitorPlugin(
    name = "FacebookAnalytics"
)
public class FacebookAnalytics extends Plugin {
    private AppEventsLogger logger;

    private static final String PLUGIN_TAG = "FacebookAnalyticsCapacitorPlugin";

    @Override
    public void load() {
        if (bridge == null) {
            bridge = this.getBridge();
        }

        super.load();
    }

    @PluginMethod
    public void prepareLogging(PluginCall call) {
        // enable debug mode of facebook sdk
        if (logger == null) {
            Log.d(PLUGIN_TAG, "initializing SDK");

            /*
            Log.d(PLUGIN_TAG, "set debug enabled");

            // uncomment this code for debug logs of facebook SDK
            FacebookSdk.setIsDebugEnabled(true);
            // cf. https://developers.facebook.com/docs/reference/android/current/class/LoggingBehavior/
            FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_RAW_RESPONSES);
            */

            FacebookSdk.setAutoInitEnabled(true);
            FacebookSdk.setAdvertiserIDCollectionEnabled(true);
            FacebookSdk.fullyInitialize();

            logger = AppEventsLogger.newLogger(bridge.getActivity().getApplicationContext());

            // this line is required for reporting app installs to facebook
            logger.activateApp(bridge.getActivity().getApplication());
        } else {
            Log.d(PLUGIN_TAG, "SDK already initialized");
        }

        call.resolve();
    }

    @PluginMethod
    public void flush(PluginCall call) {
        logger.flush();
        call.resolve();
    }

    @PluginMethod
    public void logEvent(PluginCall call) {
        if (!call.getData().has("event")) {
            call.reject("Must provide an event");
            return;
        }

        String event = call.getString("event");
        JSObject params = call.getObject("params", new JSObject());
        Double valueToSum = call.getDouble("valueToSum", null);

        if (params.length() > 0) {
            Bundle parameters = new Bundle();
            Iterator<String> iter = params.keys();

            while (iter.hasNext()) {
                String key = iter.next();
                String value = params.getString(key);
                parameters.putString(key, value);
            }
            if (valueToSum != null) {
                logger.logEvent(event, valueToSum, parameters);
            } else {
                logger.logEvent(event, parameters);
            }

        } else {
            if (valueToSum != null) {
                logger.logEvent(event, valueToSum);
            } else {
                logger.logEvent(event);
            }
        }

        call.resolve();
    }

    @PluginMethod
    public void logPurchase(PluginCall call) {
        if (!call.getData().has("amount")) {
            call.reject("Must provide an amount");
            return;
        }

        Double amount = call.getDouble("amount", null);
        String curr = call.getString("currency");
        JSObject params = call.getObject("params", new JSObject());

        Currency currency = Currency.getInstance(curr);
        if (params.length() > 0) {
            Bundle parameters = new Bundle();
            Iterator<String> iter = params.keys();

            while (iter.hasNext()) {
                String key = iter.next();
                String value = params.getString(key);
                parameters.putString(key, value);
            }
            logger.logPurchase(BigDecimal.valueOf(amount), currency, parameters);
        } else {
            logger.logPurchase(BigDecimal.valueOf(amount), currency);
        }


        call.resolve();
    }

    @PluginMethod
    public void logAddPaymentInfo(PluginCall call) {
        Integer success = call.getInt("success");
        Bundle params = new Bundle();
        params.putInt(AppEventsConstants.EVENT_PARAM_SUCCESS, success);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO, params);

        call.resolve();
    }

    @PluginMethod
    public void logAddToCart(PluginCall call) {
        if (!call.getData().has("amount")) {
            call.reject("Must provide an amount");
            return;
        }
        Double amount = call.getDouble("amount", null);
        String currency = call.getString("currency");
        Bundle params = new Bundle();

        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, amount, params);

        call.resolve();
    }

    @PluginMethod
    public void logCompleteRegistration(PluginCall call) {
        JSObject params = call.getObject("params", new JSObject());
        if (params.length() > 0) {
            Bundle parameters = new Bundle();
            Iterator<String> iter = params.keys();

            while (iter.hasNext()) {
                String key = iter.next();
                String value = params.getString(key);
                parameters.putString(key, value);
            }
            logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, parameters);
        } else {
            logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);
        }

        call.resolve();
    }

    @PluginMethod
    public void logCompletedTutorial(PluginCall call) {
        JSObject params = call.getObject("params", new JSObject());
        if (params.length() > 0) {
            Bundle parameters = new Bundle();
            Iterator<String> iter = params.keys();

            while (iter.hasNext()) {
                String key = iter.next();
                String value = params.getString(key);
                parameters.putString(key, value);
            }
            logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_TUTORIAL, parameters);
        } else {
            logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_TUTORIAL);
        }

        call.resolve();
    }
}
