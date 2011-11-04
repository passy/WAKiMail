========
WAKiMail
========

This is a `RoboGuice`_ based Android application to read emails you receive on
the WAK_ platform. You can install this app from the `Android Market`_.

.. _WAK: http://www.wak-sh.de/
.. _RoboGuice: http://code.google.com/p/roboguice/
.. _Android Market: https://market.android.com/details?id=net.rdrei.android.wakimail

Licensing
=========

WAKiMail is licensed under the 3-clause BSD license.

Building
========

While it is planned to convert this to a Maven project, right now the easiest
way to set this project up is to import both sub-projects into Eclipse. After
that, you should be done as all dependencies are included for now, except for
the Android SDK.

Thanks
======

Libraries used:

    * Dependency Injection: `RoboGuice <http://code.google.com/p/roboguice/>`_ (Apache License 2.0)
    * Android Testing: `Robolectric <http://robolectric.org>`_ (MIT License)
    * Testing: `JUnit <http://www.junit.org/>`_ (Common Public License 1.0)
    * Crash Reporting: `ACRA <http://code.google.com/p/acra/>`_ (Apache License 2.0)
    * Mocking: `Mockito <http://mockito.org/>`_ (MIT License)
    * Android Fragments/LoaderManagers: `Android Support Library <http://developer.android.com/sdk/compatibility-library.html>`_

