scrollscreenshot
================

Make Android screenshots of scrollable screen content - brought to you by [PGS Software](www.pgs-soft.com)


This tool makes a number of screenshots, scrolling screen content by half height each time. Status bar and navigation bar are included only once.

![Illustration how images are merged](https://github.com/PGSSoft/scrollscreenshot/blob/master/illustration.png "Illustration how images are merged")


Documentation:
--------------

```
Usage: com.pgssoft.scrollscreenshot.ScrollScreenShot [options]
  Options:
    -c, --count
       Number of screenshot to take (2nd+ adds half screen height each)
       Default: 5
    -h, --help
       Display this help
       Default: false
  * -i, --inputdevice
       Digitizer input device number, N in /dev/input/eventN
       Default: 1
    -n, --nameprefix
       Output filename prefix
       Default: out
    -p, --pathsdk
       Path to Android SDK
    -s, --separate
       Save separate pictures instead of merging
       Default: false

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

You can now start capturing. Unlock screen, start app you want to scroll-capture and type (replace *2* by your device input number): 

```
java -cp scrollscreenshot-0.1.jar com.pgssoft.scrollscreenshot.ScrollScreenShot -i 2
```

If eveything goes well, you will get file `out.png` with something like:

<img src="https://github.com/PGSSoft/scrollscreenshot/blob/master/sample.png" alt="SAMPLE" width="200">


Todo:
-----

* scrolling in all 4 directions
* option for disable image merging
* option for choosing ADB device


Changelog
---------

* 0.1 - initial release, only top-down scrolling for first device found by ADB


Acknowledgments
---------------

* description of input events was taken from
[this blog post](http://ktnr74.blogspot.com/2013/06/emulating-touchscreen-interaction-with.html)

* command line parsing uses http://jcommander.org/



License
----

scrollscreenshot for Android
Copyright (c) 2014 PGS Software
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
