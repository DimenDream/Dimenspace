package com.hoot.encoding;
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.util.Locale;

/**
 *
 * Constants used by multiple classes in this package
 */
public final class Constants {

    // Set to true to turn on verbose logging
    public static final boolean LOGV = false;
    
    // Set to true to turn on debug logging
    public static final boolean LOGD = true;



    /*
     * A user-agent string that's sent to the HTTP site. It includes information about the device
     * and the build that the device is running.
     */
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; U; Android "
            + android.os.Build.VERSION.RELEASE + ";"
            + Locale.getDefault().toString() + "; " + android.os.Build.DEVICE
            + "/" + android.os.Build.ID + ")";

    // Status values to broadcast to the Activity

    // The download is starting
    public static final int STATE_ACTION_STARTED = 0;

    // The background thread is connecting to the RSS feed
    public static final int STATE_ACTION_CONNECTING = 1;

    // The background thread is parsing the RSS feed
    public static final int STATE_ACTION_PARSING = 2;

    // The background thread is writing data to the content provider
    public static final int STATE_ACTION_WRITING = 3;

    // The background thread is done
    public static final int STATE_ACTION_COMPLETE = 4;

    // The background thread is doing logging
    public static final int STATE_LOG = -1;

    public static final CharSequence BLANK = " ";
}
