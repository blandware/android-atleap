Android AtLeap accelerator
==========================

[![Build Status](https://api.travis-ci.org/blandware/android-atleap.png?branch=master)](https://travis-ci.org/blandware/android-atleap)

# Features

## Universal Content Provider
Android AtLeap allows you configure ER-model using ORMLite POJOs and access SQLite database via
 ContentProvider as flexible as direct access to database.

 * Universal implementation of [`android.content.ContentProvider`](http://developer.android.com/guide/topics/providers/content-providers.html) with [SQLite](http://developer.android.com/training/basics/data-storage/databases.html).
 * Universal implementation of `android.content.ContentProvider` with [ORMLite](http://ormlite.com).
 * Flexible mapping  between ContentProvider Uri path and SQL. You can specify SQL for tables part only or specify raw SQL or even specify callback method which build SQL.
 * Integration with [RoboSpice ORMLite cache persister](https://github.com/octo-online/robospice).

## Navigation drawer

See [`android.support.v4.widget.DrawerLayout`](http://developer.android.com/training/implementing-navigation/nav-drawer.html)

## Account Authenticator helper classes

They are based on [`android.accounts.AbstractAccountAuthenticator`](http://developer.android.com/reference/android/accounts/AbstractAccountAuthenticator.html)


# Sample

[atleap-sample](https://github.com/blandware/android-atleap/tree/master/atleap-sample)

It is good example of how to integrate RoboSpice, Retrofit, Picasso, ORMLite, AtLeap and some other libraries.
This sample show integration with GitHub Api. The authentication is made via oAuth 2.0.

Please do not forget to insert correct GitHub Api oAuth client id and secret into `/atleap-sample/src/main/res/values/settings.xml`
before running this sample.

# Tests

[atleap-core-test](https://github.com/blandware/android-atleap/tree/master/atleap-core-test)

# Gradle

The releases of Android AtLeap are published in the MavenCentral. You can add dependency in the following way:

```
    compile 'com.blandware.android.atleap:core:+@aar'
```

If you would like to use snapshots, please add the following code into repositories statement:

```
    maven {
        url 'https://oss.sonatype.org/content/repositories/snapshots/'
    }
```

# Changelog

 See [changelog](https://github.com/blandware/android-atleap/blob/master/CHANGELOG.md).

# License
 Android AtLeap licensed under [Apache 2.0 license](https://github.com/blandware/android-atleap/blob/master/LICENSE).

