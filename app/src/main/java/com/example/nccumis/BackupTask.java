package com.example.nccumis;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class BackupTask extends AsyncTask<String, Void, Integer> {
    private static final String COMMAND_BACKUP = "backupDatabase";
    public static final String COMMAND_RESTORE = "restroeDatabase";
    private Context mCoNtext;
    public BackupTask(Context coNtext) {
        this.mCoNtext = coNtext;
    }
    @Override
    protected Integer doInBackground(String... params) {
        File dbFile = mCoNtext.getDatabasePath(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/dlion/db_dlion.db");
        File exportDir = new File(Environment.getExternalStorageDirectory(),
                "dlionBackup");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File backup = new File(exportDir, dbFile.getName());
        String command = params[0];
        if (command.equals(COMMAND_BACKUP)) {
            try {
                backup.createNewFile();
                fileCopy(dbFile, backup);
                return Log.d("backup", "ok");
            } catch (Exception e) {
                e.printStackTrace();
                return Log.d("backup", "fail");
            }
        } else if (command.equals(COMMAND_RESTORE)) {
            try {
                fileCopy(backup, dbFile);
                return Log.d("restore", "success");
            } catch (Exception e) {
                e.printStackTrace();
                return Log.d("restore", "fail");
            }
        } else {
            return null;
        }
    }
    private void fileCopy(File dbFile, File backup) throws IOException {
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
}


