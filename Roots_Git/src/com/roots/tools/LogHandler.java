/*
 * Created on 28-Mar-2013
 *
 * com.roots.tools.CreateHTMLFile.java
 * 
 * Copyright (c) 2005 NCR Corporation
 * All rights reserved. 
 *
 * This software is the confidential and proprietary information of 
 * NCR Corporation. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with NCR Corporation. 
 */
package com.roots.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LogHandler {

    private static LogHandler handle;

    private FileOutputStream outFile;

    private static boolean PRINT_TO_SCREEN = true;

    public static LogHandler getInstance(String filename) {
        if (filename == null || filename.trim().length() == 0) {
            System.out.println("default printing to screen...");
            PRINT_TO_SCREEN = true;
            if (handle == null) {
                handle = new LogHandler();
            }
        } else {
            System.out.println("printing to file..." + filename);
            PRINT_TO_SCREEN = false;
            if (handle == null) {
                handle = new LogHandler();
            }
            try {
                handle.outFile = new FileOutputStream(new File(filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return handle;
    }

    public void print(String str) {
        if (PRINT_TO_SCREEN) {
            System.out.print(str);
        } else {
            try {
                outFile.write(str.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void println(String str) {
        if (PRINT_TO_SCREEN) {
            System.out.println(str);
        } else {
            try {
                outFile.write(str.getBytes());
                outFile.write("\n".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        if (outFile != null) {
            try {
                outFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
