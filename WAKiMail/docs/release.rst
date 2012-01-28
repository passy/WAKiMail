====================
Notes about Releases
====================

Preparing a Release
-------------------

    * Version Number is automatically updated
    * Version *name* needs to be adjusted manually, by using
      ``mvn release:update-versions -DautoVersionSubmodules=true``

settings.xml
------------

~/.m2/settings.xml::

    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
            http://maven.apache.org/xsd/settings-1.0.0.xsd">
        <profiles>
            <profile>
                <id>android</id>
                <properties>
                    <android.sdk.path>/home/pascal/Applications/android-sdk-linux_x86</android.sdk.path>
                </properties>
            </profile>
            <profile>
                <id>sign</id>
                <properties>
                    <sign.keystore>~/certs/android_keystore.sec</sign.keystore>
                    <sign.storepass>xxx</sign.storepass>
                    <sign.keypass>xxx</sign.keypass>
                    <sign.alias>rdrei.net</sign.alias>
                </properties>
            </profile>
        </profiles>
        <activeProfiles>
            <!--make the profile active all the time -->
            <activeProfile>android</activeProfile>
        </activeProfiles>
    </settings>

Important parts are the keystore information.

Creating release APK
--------------------

::

    mvn install -Prelease -Psign
