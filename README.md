scrollscreenshot
================

Make Android screenshots of scrollable screen content - brought to you by [PGS Software SA](http://www.pgs-soft.com)


This tool makes a number of screenshots, scrolling screen content automatically between each shot. By default status bar and navigation bar are included only once.

![Illustration how images are merged](https://github.com/PGSSoft/scrollscreenshot/blob/master/illustration.png "Illustration how images are merged")


Documentation:
--------------

```
Usage: com.pgssoft.scrollscreenshot.ScrollScreenShot [options]
  Options:
    -c, --count
       Number of screenshot to take
       Default: 5
    -v, --device
       Device ID, first device is used if not specified (i.e. "4df1902336814fa6"
       or "192.168.56.102:5555")
    -d, --direction
       Swipe direction: topdown (default), leftright (implies "--stitch none")
       Default: topdown
    -h, --help
       Display this help
       Default: false
    -e, --inertia
       Inertia of content, how many pixels are required to start dragging. Use
       non-zero value if there are duplicated stripes.
       Default: 0
  * -i, --inputdevice
       Digitizer input device number, N in /dev/input/eventN
       Default: 1
    -n, --nameprefix
       Output filename prefix
       Default: out
    -p, --pathsdk
       Path to Android SDK
    -s, --stitch
       Stitch mode: full (smooth stitch), none (merged full screenshots),
       separate (separate files)
       Default: full

```



How to use
----------

You need to know digitizer device input number, which is specific to every device and ROM.

Use command 
```sh
adb shell getevent -l
```
and move finger on screen. You will see something like
```
/dev/input/event2: EV_SYN       SYN_REPORT           00000000
/dev/input/event2: EV_ABS       ABS_MT_WIDTH_MAJOR   00000014
/dev/input/event2: EV_ABS       ABS_MT_POSITION_X    00000247
/dev/input/event2: EV_ABS       ABS_MT_POSITION_Y    0000030c
/dev/input/event2: EV_ABS       ABS_MT_TOUCH_MAJOR   0000001a
/dev/input/event2: EV_ABS       ABS_MT_TOUCH_MINOR   0000000e
/dev/input/event2: EV_ABS       003c                 ffffffb3
```
In your case device you are looking for has number **2**.


You can now start screen capturing. Download [latest scrollscreenshot binary](https://github.com/PGSSoft/scrollscreenshot/blob/master/binaries/scrollscreenshot-latest.jar?raw=true), unlock screen, start app you want to scroll-capture and type (replace *2* by your device input number): 

```
java -cp scrollscreenshot-latest.jar com.pgssoft.scrollscreenshot.ScrollScreenShot -i 2
```

If eveything goes well, you will get file `out.png` with something like:

<img src="https://github.com/PGSSoft/scrollscreenshot/blob/master/sample.png" alt="SAMPLE" width="200">

Left-to-right mode will give you something like this:

<img src="https://github.com/PGSSoft/scrollscreenshot/blob/master/samplehorizontal.png" alt="SAMPLE" width="800">


Todo:
-----

* scrolling in all 4 directions
* automatic detection of scroll area edge


Changelog
---------

* 0.1 - initial release, only top-down scrolling for first device found by ADB
* 0.2 - scrolling top-down and left-right, stitch now works in smooth/none/separate modes, ADB device can be choosen by id


Acknowledgments
---------------

* description of input events was taken from
[this blog post](http://ktnr74.blogspot.com/2013/06/emulating-touchscreen-interaction-with.html)

* command line parsing uses http://jcommander.org/ licensed under [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0)

* device communication handled by [AOSP](http://source.android.com/) tools licensed under [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0)


[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-scrollscreenshot-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1047)

License
----

scrollscreenshot for Android

Copyright (c) 2014 PGS Software SA

https://github.com/PGSSoft/scrollscreenshot

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions
of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE SOFTWARE.
