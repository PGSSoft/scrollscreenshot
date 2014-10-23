/**
    scrollscreenshot for Android
    Copyright (c) 2014 PGS Software

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

 */

package com.pgssoft.scrollscreenshot;

import com.beust.jcommander.Parameter;

/**
 * Created by tzielinski on 2014-10-13.
 */
public class Params{

    @Parameter(names = { "-i", "--inputdevice" }, description = "Digitizer input device number, N in /dev/input/eventN", required = true)
    Integer inputDeviceNo = 1;

    @Parameter(names = { "-c", "--count" }, description = "Number of screenshot to take (each but first adds half screen height)")
    int count = 5;

    @Parameter(names = { "-p", "--pathsdk" }, description = "Path to Android SDK")
    String pathsdk = null;

    @Parameter(names = { "-s", "--separate" }, description = "Save separate pictures instead of merging")
    boolean separate = false;

    @Parameter(names = { "-n", "--nameprefix" }, description = "Output filename prefix")
    String nameprefix = "out";

    @Parameter(names = { "-e", "--inertia" }, description = "Inertia of content, how many pixels are required to start dragging. Use non-zero value if there are duplicated stripes.")
    Integer inertia = 0;

    /* to be done in future

    @Parameter(names = { "-e", "--device" }, description = "Device ID, first device is used if not specified")
    String direction = null;

    @Parameter(names = { "-d", "--direction" }, description = "Scrolling direction: down/up/right/left")
    String direction = "down";

    @Parameter(names = { "-w", "--wholepics" }, description = "Do not cut pics while merging, useful for whole-screen tabs")
    boolean whole = false;
    */

    @Parameter(names = {"-h", "--help"}, description = "Display this help", help = true)
    boolean help;

}
