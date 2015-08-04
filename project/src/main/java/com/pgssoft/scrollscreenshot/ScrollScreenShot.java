/**
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

 
    author: Tomasz Zielinski, PGS Software SA
    https://github.com/PGSSoft/scrollscreenshot

 */

package com.pgssoft.scrollscreenshot;

import com.android.ddmlib.*;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.prefs.PreferenceChangeEvent;

public class ScrollScreenShot {

    public static void main(String[] args) {

        Params params = null;
        JCommander jcom = null;

        try {
            params = new Params();
            jcom = new JCommander(params, args);
            jcom.setProgramName("com.pgssoft.scrollscreenshot.ScrollScreenShot");
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        if (null == params.pathsdk){
            params.pathsdk = System.getenv("ANDROID_SDK_HOME");
            if (null == params.pathsdk){
                System.out.println("Set ANDROID_SDK_HOME env variable or use --pathsdk parameter");
                System.exit(-1);
            }
        }

        if (params.help){
            jcom.usage();
            return;
        }

        if (params.direction.equals(Params.DIR_LEFTRIGHT) && params.stitch.equals(Params.STITCH_FULL)){
            System.out.println("* topdown direction detected, reverting --stitch to \"none\"");
            params.stitch = Params.STITCH_NONE;
        }

        int ret = new ScrollScreenShot().process(params);
        System.exit(ret);
    }

    public int process(Params params)
    {

        AndroidDebugBridge.init(true);

        String adb = params.pathsdk+File.separator+"platform-tools"+File.separator+"adb";
        if (System.getProperty("os.name").toLowerCase().indexOf("win")>=0){
            adb += ".exe";
        }


        if (false == new File(adb).exists())
        {
            System.out.println("Cannot find ADB executable: "+adb);
            return -1;
        }

        Iterator imageWriters = ImageIO.getImageWritersByFormatName("png");
        if (false == imageWriters.hasNext()){
            System.out.println("PNG image encoder is not available");
            return -1;
        }
        ImageWriter imageWriter = (ImageWriter) imageWriters.next();

        AndroidDebugBridge bridge = AndroidDebugBridge.createBridge(adb, false );

        if (null != bridge){

            if (false == waitUntilConnected(bridge)) {
                System.out.println("Error connecting to device");
                return -1;
            }

            IDevice[] devices = bridge.getDevices();
            if (devices.length>0)
            {
                IDevice device = devices[0]; // first by default

                if (null != params.deviceId)
                {
                    device = null;
                    for (IDevice iDevice : devices) {
                        if (params.deviceId.trim().toLowerCase().equals(iDevice.getSerialNumber().trim().toLowerCase()))
                        {
                            device = iDevice;
                            break;
                        }
                    }
                    if (null == device)
                    {
                       System.out.println("Couldn't find device with id: "+params.deviceId);
                       return -1;
                    }
                }


                try {

                    BufferedImage summaryImage = null;
                    Graphics summaryImageGfx = null;

                    for (int count = 0; count < params.count; count++) {

                        System.out.println(String.format("Screenshot %d of %d", count+1, params.count));

                        RawImage img = device.getScreenshot();

                        if (summaryImage==null){
                            if( params.stitch.equals( Params.STITCH_FULL )) {
                                summaryImage = new BufferedImage(img.width, (int) (img.height * (0.5 + params.count / 2.0)), BufferedImage.TYPE_INT_ARGB);
                                summaryImageGfx = summaryImage.getGraphics();
                            }
                            if( params.stitch.equals( Params.STITCH_NONE ) && false == params.direction.equals(Params.DIR_LEFTRIGHT)) {
                                summaryImage = new BufferedImage(img.width, (int) (img.height * params.count), BufferedImage.TYPE_INT_ARGB);
                                summaryImageGfx = summaryImage.getGraphics();
                            }

                            if( params.stitch.equals( Params.STITCH_NONE ) && true == params.direction.equals(Params.DIR_LEFTRIGHT)) {
                                summaryImage = new BufferedImage(img.width * params.count, img.height, BufferedImage.TYPE_INT_ARGB);
                                summaryImageGfx = summaryImage.getGraphics();
                            }
                        }

                        BufferedImage oneScreenImage = new BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_ARGB);

                        for (int row = 0; row < img.height; row++) {
                            for (int col = 0; col < img.width; col++) {
                                int argb = img.getARGB(4 * (row * img.width + col));
                                oneScreenImage.setRGB(col, row, argb);
                            }
                        }

                        int h = oneScreenImage.getHeight();
                        int w = oneScreenImage.getWidth();

						if(summaryImageGfx != null) {
							if (count == 0)
							{
								summaryImageGfx.drawImage(oneScreenImage, 0, h * count, null);
							} else {

								if (params.stitch.equals(Params.STITCH_FULL)) {
									// skip first 0.25 of height of current image, draw
									summaryImageGfx.drawImage(
											oneScreenImage,
											0, (int) (h * (0.25 + (count / 2.0))), w, (int) (h * (1.0 + (count / 2.0))),
											0, (int) (0.25 * h), w, h, null);
								}
								if (params.stitch.equals(Params.STITCH_NONE) && params.direction.equals(Params.DIR_TOPDOWN)) {
									summaryImageGfx.drawImage(
											oneScreenImage,
											0, (int) (h * count), w, (int) (h * (1.0 + (count))),
											0, (int) 0, w, h, null);
								}
								if (params.stitch.equals(Params.STITCH_NONE) && params.direction.equals(Params.DIR_LEFTRIGHT)) {
									summaryImageGfx.drawImage(
											oneScreenImage,
											w * count, 0, (int)(w*(1.0+count)), h,
											0, (int) 0, w, h, null);
								}
							}
						}

                        if ( params.stitch.equals(Params.STITCH_SEPARATE )) {
                            ImageOutputStream ios = ImageIO.createImageOutputStream(new File(params.nameprefix + count + ".png"));
                            imageWriter.setOutput(ios);
                            imageWriter.write(oneScreenImage);
                            imageWriter.dispose();
                        }

                        // scroll window
                        if (count < params.count-1) {

                            if ( params.direction.equals(Params.DIR_TOPDOWN) ) {

                                int topy = img.height / 4; // 0.25 of screen height
                                int bottomy = topy + img.height / 2; // 0.75 of screen height
                                int x = img.width / 2; // middle of screen width

                                scrollScreen(device, params.inputDeviceNo, x, bottomy, x, topy, params.inertia);
                            }

                            if ( params.direction.equals(Params.DIR_LEFTRIGHT) ) {

                                int y = img.height / 2;
                                int leftx = img.width / 10; // 0.2 of screen width
                                int rightx = img.width - leftx; // 0.8 of screen width

                                scrollScreen(device, params.inputDeviceNo, rightx, y, leftx, y, params.inertia);
                            }
                        }

                    }

                    if (false == params.stitch.equals(Params.STITCH_SEPARATE )) {

                        ImageOutputStream ios = ImageIO.createImageOutputStream(new File(params.nameprefix + ".png"));
                        imageWriter.setOutput(ios);
                        imageWriter.write(summaryImage);
                        imageWriter.dispose();
                    }

                } catch (Exception e) {
                    System.err.print(e);
                }


            } else {
                System.out.println("ADB device list is empty, verify with 'adb devices'");
                return -1;
            }

        } else {
            System.out.println("Cannot create ADB bridge, please check your Android SDK installation");
            return -1;
        }

        return 0;
    }

    void scrollScreen(IDevice device, int input, int x1, int y1, int x2, int y2, int inertia) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {

        // simulate finger press

        device.executeShellCommand("sendevent /dev/input/event"+input+" 3 57 111", rec); // ABS_MT_TRACKING_ID (57) - ID of the touch (important for multi-touch reports)
        //device.executeShellCommand("sendevent /dev/input/event2 3 47 0", rec); // ABS_MT_SLOT
        device.executeShellCommand("sendevent /dev/input/event"+input+" 3 63 1", rec); // 3f ????
        device.executeShellCommand("sendevent /dev/input/event"+input+" 1 330 1", rec); // 14A = 330 = BTN_TOUCH / DOWN
        device.executeShellCommand("sendevent /dev/input/event"+input+" 3 53 "+x1, rec); // ABS_MT_POSITION_X (53) - x coordinate of the touch
        device.executeShellCommand("sendevent /dev/input/event"+input+" 3 54 "+y1, rec); // ABS_MT_POSITION_Y (54) - y coordinate of the touch
        device.executeShellCommand("sendevent /dev/input/event"+input+" 3 48 5", rec); // ABS_MT_TOUCH_MAJOR (48) - basically width of your finger tip in pixels
        device.executeShellCommand("sendevent /dev/input/event"+input+" 3 49 6", rec); // ABS_MT_TOUCH_MINOR (49)
        //device.executeShellCommand("sendevent /dev/input/event2 3 58 50", rec); // ABS_MT_PRESSURE (58) - pressure of the touch
        //device.executeShellCommand("sendevent /dev/input/event2 0 2 0", rec); // end of separate touch data
        device.executeShellCommand("sendevent /dev/input/event"+input+" 0 0 0", rec); // end of report

        int steps = 10;

        // simulate finger drag
        for (int i=1; i<=steps; i++){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int x = x1 + i*(x2-x1)/steps;
            int y = y1 + i*(y2-y1)/steps;

            if (i == steps)
            {
                x -= inertia;
                y -= inertia;
            }

            device.executeShellCommand("sendevent /dev/input/event"+input+" 3 53 "+x, rec); // ABS_MT_POSITION_X (53) - x coordinate of the touch
            device.executeShellCommand("sendevent /dev/input/event"+input+" 3 54 "+y, rec); // ABS_MT_POSITION_Y (54) - y coordinate of the touch
            device.executeShellCommand("sendevent /dev/input/event"+input+" 3 49 6", rec); // ABS_MT_TOUCH_MINOR (49)
            device.executeShellCommand("sendevent /dev/input/event"+input+" 0 0 0", rec); // end of report

        }

        // stop dragging finger to avoid kinetic scroll
        for (int i=1; i<3; i++){

            device.executeShellCommand("sendevent /dev/input/event"+input+" 3 53 "+(x2-inertia), rec); // ABS_MT_POSITION_X (53) - x coordinate of the touch
            device.executeShellCommand("sendevent /dev/input/event"+input+" 3 54 "+(y2-inertia), rec); // ABS_MT_POSITION_Y (54) - y coordinate of the touch
            device.executeShellCommand("sendevent /dev/input/event"+input+" 3 49 6", rec); // ABS_MT_TOUCH_MINOR (49)
            device.executeShellCommand("sendevent /dev/input/event"+input+" 0 0 0", rec); // end of report
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


        // simulate finger release
        release(device, input);

    }


    void release(IDevice device, int input) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {

        device.executeShellCommand("sendevent /dev/input/event"+input+" 3 48 0", rec); // ABS_MT_TOUCH_MAJOR (48) - basically width of your finger tip in pixels
        device.executeShellCommand("sendevent /dev/input/event"+input+" 3 57 -1", rec); // you just send the empty report with ABS_MT_TRACKING_ID = -1:
        device.executeShellCommand("sendevent /dev/input/event"+input+" 1 330 0", rec); // 14A = 330 = BTN_TOUCH / UP
        //device.executeShellCommand("sendevent /dev/input/event2 0 2 0", rec); // end of separate touch data
        device.executeShellCommand("sendevent /dev/input/event"+input+" 0 0 0", rec); // end of report

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    IShellOutputReceiver rec = new IShellOutputReceiver() {
        @Override
        public void addOutput(byte[] data, int offset, int length) {
            // noop
        }

        @Override
        public void flush() {
            // noop
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    };

    boolean waitUntilConnected( AndroidDebugBridge adb )
    {
        int trials = 40;
        final int connectionWaitTime = 50;
        while ( trials > 0 )
        {
            try
            {
                Thread.sleep( connectionWaitTime );
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
            if ( adb.isConnected() && adb.hasInitialDeviceList())
            {
                return true;
            }
            trials--;
        }
        return false;
    }

}
