Android AtLeap accelerator
==========================

Android AtLeap accelerator allows you configure ER-model using ORMLite POJOs and access SQLite database via
 ContentProvider as flexible as direct access to database.

 * Universal implementation of [`android.content.ContentProvider`](http://developer.android.com/guide/topics/providers/content-providers.html) with [SQLite](http://developer.android.com/training/basics/data-storage/databases.html).
 * Universal implementation of `android.content.ContentProvider` with [ORMLite](http://ormlite.com).
 * Flexible mapping  between ContentProvider Uri path and SQL. You can specify SQL for tables part only or specify raw SQL or even specify callback method which build SQL.
 * Integration with [RoboSpice ORMLite cache persister](https://github.com/octo-online/robospice).

## Examples

 * [atleap-sample](https://github.com/blandware/android-atleap/tree/master/atleap-sample) Android application.
 * [atleap-core-test](https://github.com/blandware/android-atleap/tree/master/atleap-core-test) Test Cases contains good samples.

## Maven

The releases of Android AtLeap are published in the MavenCentral. You can add dependency in the following way:

```
    compile 'com.blandware.android.atleap:core:+@aar'
```

If you would like to use snapshots,

```
    maven {
        url 'https://oss.sonatype.org/content/repositories/snapshots/'
    }
```

## Changelog

 See [changelog](https://github.com/blandware/android-atleap/blob/master/CHANGELOG.md).

## License
 Android AtLeap licensed under [Apache 2.0 license](https://github.com/blandware/android-atleap/blob/master/LICENSE).

