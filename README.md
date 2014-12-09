soas (Sample Of All Samples)
----

With your help we can build a sample app that touches most components of the Android framework, helpful for beginners and experienced. Your pull-requested are appreciated.

**Here's what the project touches so far:**
- Toolbar
- Drawer (following the updated Material Design Guidelines)
- Volley (with forced caching option, it caches regardless of server response headers, Memory and Disk)
- Download images using volley (with Memory and Disk caching)
- Json (using Gson)
- ListView with a custom Adapter (based on BaseAdapter)
- SwipeToReferesh (using SwipeRefreshLayout)
- Endless Adapter (using a custom OnScrollListener)
- Fragment (ListFragment and Fragment)
- Headless Fragment (maintain data on config changed)
- Landscape, 7'' and 10'' tablets specific layouts
- Custom Views
- Palette
- Themes
- Styles
- Gradle
- Gradle product flavors
- Espresso tests

**Your can help with:**
- List filter
- RTL
- Database
- More tests
- Receiver (Check [Emailer - ConnectivityReceiver.java][1])
- Service
- Loader
- CursorAdapter
- ContentProvider
- JNI
- HMAC
- SSL
- Bluetooth (check [iRacerController - Deployer.java][2])
- Scheduling tasks (Check [Emailer - Scheduler.java][3])
- Material Dialog (like a rate me dialog, https://github.com/afollestad/material-dialogs)
- Differenet buttons (http://www.google.com/design/spec/components/buttons.html)
- _Anything else you think worth adding_
- _Or email me with what you think should be added_

**Credits:**
- Your name!

**Useful materials (To start developing for native Android or update your knowledge):**
- https://developer.android.com/guide/index.html
- http://www.google.com/design/spec/material-design/introduction.html
- http://www.vogella.com/tutorials/android.html
- https://github.com/codepath/android_guides/wiki

**Developed by:**
Mostafa Gazar - eng.mostafa.gazar@gmail.com

[1]: https://github.com/MostafaGazar/Emailer/blob/master/src/com/meg7/emailer/receiver/ConnectivityReceiver.java
[2]: https://github.com/MostafaGazar/iRacerController/blob/master/source/src/main/java/com/meg7/controller/Deployer.java
[3]: https://github.com/MostafaGazar/Emailer/blob/master/src/com/meg7/emailer/util/Scheduler.java
