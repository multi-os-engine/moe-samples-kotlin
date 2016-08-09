package org.moe.kotlinmuseummap.ios

import org.moe.natj.general.Pointer
import org.moe.natj.general.ann.RegisterOnStartup
import org.moe.natj.objc.ann.Selector
import ios.NSObject
import ios.foundation.NSDictionary
import ios.uikit.UIApplication
import ios.uikit.UINavigationController
import ios.uikit.UIScreen
import ios.uikit.UIWindow
import ios.uikit.c.UIKit
import ios.uikit.protocol.UIApplicationDelegate

@RegisterOnStartup
class Main protected constructor(peer: Pointer) : NSObject(peer), UIApplicationDelegate {

    private var window: UIWindow? = null

    override fun applicationDidFinishLaunchingWithOptions(application: UIApplication?, launchOptions: NSDictionary<*, *>?): Boolean {
        // For setup proxy
        //System.setProperty("http.proxyHost", "proxy_host");
        //System.setProperty("http.proxyPort", "proxy_port");
        //System.setProperty("https.proxyHost", "proxy_host");
        //System.setProperty("https.proxyPort", "proxy_port");
        val vc = MuseumMapController.alloc().init()
        val navigationController = UINavigationController.alloc().init()

        navigationController.initWithRootViewController(vc)

        val screen = UIScreen.mainScreen()

        val bounds = screen.bounds()
        window = UIWindow.alloc().initWithFrame(bounds)

        window!!.setRootViewController(navigationController)

        window!!.makeKeyAndVisible()

        return true
    }

    override fun setWindow(value: UIWindow?) {
        window = value
    }

    override fun window(): UIWindow {
        return window as UIWindow
    }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            UIKit.UIApplicationMain(0, null, null, Main::class.java.name)
        }

        @Selector("alloc")
        @JvmStatic external fun alloc(): Main
    }
}