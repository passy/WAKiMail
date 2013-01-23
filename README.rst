========
WAKiMail
========

This is a `RoboGuice`_-based Android application to read emails you receive on
the WAK_ platform. You can install this app from the `Google Play`_.

.. _WAK: http://www.wak-sh.de/
.. _RoboGuice: http://code.google.com/p/roboguice/
.. _Google Play: https://play.google.com/store/apps/details?id=net.rdrei.android.wakimail

Licensing
=========

WAKiMail is licensed under the 3-clause BSD license.

Building
========

WAKiMail is now a maven3 project, so building has been drastically simplified.
However, you need to setup Android for maven first. Have a look at the
`Maven Reference
<http://www.sonatype.com/books/mvnref-book/reference/android-dev-sect-config-build.html>`_
for a great tutorial on how to do this.

After this is done, all you need to do in order to build this application is::

    # Build the APKs.
    mvn clean install
    # Install on your emulator or connected device.
    mvn android:deploy android:run

Thanks
======

Libraries used:

    * Dependency Injection: `RoboGuice <http://code.google.com/p/roboguice/>`_ (Apache License 2.0)
    * Android Testing: `Robolectric <http://robolectric.org>`_ (MIT License)
    * Testing: `JUnit <http://www.junit.org/>`_ (Common Public License 1.0)
    * Crash Reporting: `ACRA <http://code.google.com/p/acra/>`_ (Apache License 2.0)
    * Mocking: `Mockito <http://mockito.org/>`_ (MIT License)
    * Android Fragments/LoaderManagers: `Android Support Library <http://developer.android.com/sdk/compatibility-library.html>`_

Big thanks to `BugSense <http://www.bugsense.com/>`_ for providing excellent crash analytics for free.
