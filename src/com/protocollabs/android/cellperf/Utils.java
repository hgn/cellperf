package com.protocollabs.android.cellperf;


import java.util.Locale;
import java.util.UUID;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.FileOutputStream;

import android.os.Bundle;
import android.content.Context;


public class Utils {


    private static String mUniqueId = null;
    private static final String UUID_NAME = "UUID";

    public synchronized static String getUniqueId(Context context) {
        if (mUniqueId == null) {
            File installation = new File(context.getFilesDir(), UUID_NAME);
            try {
                if (!installation.exists())
                    writeUniqueIdFile(installation);
                mUniqueId = readUniqueIdFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return mUniqueId;
    }

    private static String readUniqueIdFile(File installation) throws
        IOException {
            RandomAccessFile f = new RandomAccessFile(installation, "r");
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            f.close();
            return new String(bytes);
        }

    private static void writeUniqueIdFile(File installation) throws
        IOException {
            FileOutputStream out = new FileOutputStream(installation);
            String id = UUID.randomUUID().toString();
            out.write(id.getBytes());
            out.close();
        }
}
