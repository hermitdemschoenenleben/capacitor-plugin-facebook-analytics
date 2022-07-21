import Foundation
import Capacitor
import FBSDKCoreKit
import FBSDKCoreKit.FBSDKSettings
import AppTrackingTransparency

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(FacebookAnalytics)
public class FacebookAnalytics: CAPPlugin {

    @objc func prepareLogging(_ call: CAPPluginCall) {
        // uncomment this for detailed logging of fbsdk
        // Settings.shared.enableLoggingBehavior(.appEvents)
        // Settings.shared.enableLoggingBehavior(.networkRequests)

        call.reject("facebook sdk on ios is disabled")

        /*

        let activateAppAndResolve: () -> Void = {
          // activateApp has to run on main thread
          DispatchQueue.main.async {
              print("call fbsdk activateApp")
              AppEvents.shared.activateApp()
              call.resolve()
          }
        }

        // the commented code below is used for requesting tracking permission
        // on ios. Only if this is granted, we may collect ad id and activate
        // facebooks trackingEnabled setting.
        // cf https://stackoverflow.com/a/67601694
        // for now, we don't want to ask for tracking permission and we don't
        // want to collect ad ID as we just want to test facebook integration
        // in general.
        activateAppAndResolve()
        // if #available(iOS 14, *) {
        //     ATTrackingManager.requestTrackingAuthorization { status in
        //         switch status {
        //             case .authorized:
        //                 print("enable tracking")
        //                 Settings.shared.isAdvertiserTrackingEnabled = true;
        //                 Settings.shared.isAdvertiserIDCollectionEnabled = true;

        //             case .denied:
        //                 print("disable tracking")
        //             default:
        //                 print("disable tracking")
        //         }

        //         activateAppAndResolve()
        //     }
        // } else {
        //   activateAppAndResolve()
        // }
        */
    }

    @objc func logEvent(_ call: CAPPluginCall) {
        guard let event = call.getString("event") else {
            call.reject("Missing event argument")
            return;
        }

        print(event)

        AppEvents.shared.logEvent(.init(event))

        call.resolve()
    }

    @objc func logCompleteRegistration(_ call: CAPPluginCall) {
        AppEvents.shared.logEvent(.completedRegistration)

        call.resolve()
    }

    @objc func logCompletedTutorial(_ call: CAPPluginCall) {
        AppEvents.shared.logEvent(.completedTutorial)

        call.resolve()
    }

    @objc func flush(_ call: CAPPluginCall) {
        AppEvents.shared.flush()

        call.resolve()
    }
}
