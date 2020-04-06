package com.example.helloworld;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileHandler {
    Context context;
    BufferedReader br = null;
    OutputStreamWriter streamWriter = null;
    FileHandler(Context c) {
        context = c;
        br = null;
        streamWriter = null;
    }

    public void readFile() {

    }

    public void writeFile(String fileName, String content) {
        try {
            streamWriter = new OutputStreamWriter(context.openFileOutput(fileName, context.MODE_PRIVATE));
            streamWriter.write(content + "\n");
            streamWriter.close();
        } catch (IOException e) {
            Log.e("IOExc", e.getMessage());
        } finally {
            try {
                if (streamWriter != null) {
                    streamWriter.close();
                }
            } catch (Exception ex) {
                Log.e("Unknown error", ex.getMessage());
            }
        }
        streamWriter = null;
    }

    public ArrayList<String> readFile(String fileName) {
        String line = "";
        ArrayList<String> fileLines = new ArrayList<String>();
        try {
            System.out.println("LOGGER: fname:"+ context.getFilesDir() + fileName);
            br = new BufferedReader(new InputStreamReader(context.openFileInput(fileName)));

            while ((line = br.readLine()) != null) {
                System.out.println("LOGGER: read " + line);
                fileLines.add(line);
            }

        } catch (IOException e) {
            Log.e("IOEx", e.getMessage());
        } catch (Exception exx) {
            System.out.println(exx.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception ex) {
                Log.e(ex.getMessage(), "Failed to close BufferedReader");
                System.exit(-1);
            }
        }
        br = null;
        return fileLines;
    }

}
