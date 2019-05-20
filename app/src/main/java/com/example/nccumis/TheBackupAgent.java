//package com.example.nccumis;
//
//import android.app.backup.BackupAgentHelper;
//import android.app.backup.FileBackupHelper;
//
//import java.io.File;
//
//public class TheBackupAgent extends BackupAgentHelper {
//
//    private static final String DB_NAME = "App.db"; // db name
//
//    @Override
//    public void onCreate() {
//        FileBackupHelper dbs = new FileBackupHelper(this, DB_NAME);
//        addHelper("dbs", dbs);
//    }
//
//    @Override
//    public File getFilesDir() {
//        final File f = getDatabasePath(DB_NAME);
//        return f.getParentFile();
//    }
//}