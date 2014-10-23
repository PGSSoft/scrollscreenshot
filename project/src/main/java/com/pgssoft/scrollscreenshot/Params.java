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

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

/**
 * Created by tzielinski on 2014-10-13.
 */
public class Params  {

    public final static String STITCH_FULL = "full";
    public final static String STITCH_NONE = "none";
    public final static String STITCH_SEPARATE = "separate";

    public final static String DIR_TOPDOWN = "topdown";
    public final static String DIR_LEFTRIGHT = "leftright";

    @Parameter(names = { "-i", "--inputdevice" }, description = "Digitizer input device number, N in /dev/input/eventN", required = true)
    Integer inputDeviceNo = 1;

    @Parameter(names = { "-c", "--count" }, description = "Number of screenshot to take")
    int count = 5;

    @Parameter(names = { "-p", "--pathsdk" }, description = "Path to Android SDK")
    String pathsdk = null;

    @Parameter(
            names = { "-s", "--stitch" },
            description = "Stitch mode: full (smooth stitch), none (merged full screenshots), separate (separate files)",
            validateWith = StichtValidator.class)
    String stitch = STITCH_FULL;

    @Parameter(
            names = { "-d", "--direction" },
            description = "Swipe direction: topdown (default), leftright (implies \"--stitch none\")",
            validateWith = DirectionValidator.class)
    String direction = DIR_TOPDOWN;

    @Parameter(names = { "-n", "--nameprefix" }, description = "Output filename prefix")
    String nameprefix = "out";

    @Parameter(names = { "-e", "--inertia" }, description = "Inertia of content, how many pixels are required to start dragging. Use non-zero value if there are duplicated stripes.")
    Integer inertia = 0;

    @Parameter(names = { "-v", "--device" }, description = "Device ID, first device is used if not specified (i.e. \"4df1902336814fa6\" or \"192.168.56.102:5555\")")
    String deviceId = null;

    @Parameter(names = {"-h", "--help"}, description = "Display this help", help = true)
    boolean help;


    public static class StichtValidator implements IParameterValidator{
       @Override
        public void validate(String name, String value) throws ParameterException {
           if (false == value.equals(STITCH_NONE) && false == value.equals(STITCH_SEPARATE) && false == value.equals(STITCH_FULL) ){
                throw new ParameterException("Parameter " + name + " must be one of: "+ STITCH_NONE + ", " + STITCH_SEPARATE+", "+STITCH_FULL
                );
            }
        }
    }

    public static class DirectionValidator implements IParameterValidator{
       @Override
        public void validate(String name, String value) throws ParameterException {
           if (false == value.equals(DIR_LEFTRIGHT) && false == value.equals(DIR_TOPDOWN)){
                throw new ParameterException("Parameter " + name + " must be one of: "+ DIR_TOPDOWN + ", " + DIR_LEFTRIGHT
                );
            }
        }
    }


}
