# Hosts Editor for Android

This repository contains the source code for the Hosts-editor
Android app available from [Google Play][].

![Hosts Editor for Android screenshots][]

This application, **for root devices only**, lets you modify easily your Android `/etc/hosts` file.

You might need to reboot to clear your DNS cache after any change.


## Features

* Android 2.2 and above
* Insert, Remove, Toggle (comment/uncomment) and Modify host entries
* Holo theme
* Fast search engine
* 100% free, no ads


## Building the app

The Android SDK for API level 17 is required to build the app.

Run the following commands to clone the repo and create a release build:

    git clone git@github.com:Nilhcem/hosts-editor-android.git
    cd hosts-editor-android
    git submodule update --init
    android update project -p contrib/actionbarsherlock/actionbarsherlock
    ant clean release

You will then have to sign the apk and run `zipalign` (See Android Developers Documentation).

If you want to install a debug version on your device, launch the following command:

    ant clean debug install


## Acknowledgements

This project uses many great open-source libraries, such as:

* [ActionBarSherlock][]
* [Butterknife][]
* [Dagger][]
* [Commons IO][]
* [Otto][]
* [RootTools][]


## License

<pre>
Copyright (c) 2013, Gautier <Nilhcem> MECHLING
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of the original author nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
</pre>


[Google Play]: https://play.google.com/store/apps/details?id=com.nilhcem.hostseditor
[Hosts Editor for Android screenshots]: http://nilhcem.github.com/screenshots/hosts-editor.png
[ActionBarSherlock]: http://actionbarsherlock.com/
[Butterknife]: http://jakewharton.github.com/butterknife/
[Dagger]: http://square.github.com/dagger/
[Commons IO]: http://commons.apache.org/proper/commons-io/
[Otto]: http://square.github.com/otto/
[RootTools]: https://code.google.com/p/roottools/
