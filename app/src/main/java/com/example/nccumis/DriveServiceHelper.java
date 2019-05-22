package com.example.nccumis;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.content.SharedPreferences;
import android.provider.OpenableColumns;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.content.Context.MODE_PRIVATE;

/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
public class DriveServiceHelper {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;
    public  String fileId;
    public  boolean restoresucess;

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }
    public Task<Void> createorupdateFile() {
        return Tasks.call(mExecutor, () -> {
            FileList result = mDriveService.files().list()
                    .execute();
            for (File file : result.getFiles()) {
                if(file.getName().equals("App.db")){
                    java.io.File data = Environment.getDataDirectory();
                    String currentDBPath = "/data/data/com.example.nccumis/databases/App.db";
                    File fileMetadata = new File();
                    fileMetadata.setName("App.db");
                    java.io.File filePath = new java.io.File(currentDBPath);
                    FileContent mediaContent = new FileContent("application/x-sqlite3", filePath);
                    fileId=file.getId();
                    mDriveService.files().update(fileId,fileMetadata,mediaContent).execute();
                    System.out.println("更新成功");
                    return null;
                    }
            }
            java.io.File data = Environment.getDataDirectory();
            String currentDBPath = "/data/data/com.example.nccumis/databases/App.db";
            File fileMetadata = new File();
            fileMetadata.setName("App.db");
            java.io.File filePath = new java.io.File(currentDBPath);
            FileContent mediaContent = new FileContent("application/x-sqlite3", filePath);
            mDriveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("建立成功");
            return null;
        });
    }
//    public Task<Void> restore(String fileid) {
//        return Tasks.call(mExecutor, () -> {
//            java.io.File data = Environment.getDataDirectory();
//            String currentDBPath = "/data/data/com.example.nccumis/databases/App.db";
//            File fileMetadata = new File();
//            fileMetadata.setName("App.db");
//            java.io.File filePath = new java.io.File(currentDBPath);
//            //String fileId = "1Ah_ObNJiW83ClIxgBGiRiH4za673rlzp";
//            OutputStream outputStream = new FileOutputStream(currentDBPath);
//            mDriveService.files().get(fileid).executeMediaAndDownloadTo(outputStream);
//            return  null;
//        });
//    }
    public Task<Void> restore() {
        return Tasks.call(mExecutor, () -> {
            java.io.File data = Environment.getDataDirectory();
            String currentDBPath = "/data/data/com.example.nccumis/databases/App.db";
            File fileMetadata = new File();
            fileMetadata.setName("App.db");
            java.io.File filePath = new java.io.File(currentDBPath);
            //String fileId = "1Ah_ObNJiW83ClIxgBGiRiH4za673rlzp";
            OutputStream outputStream = new FileOutputStream(currentDBPath);
            FileList result = mDriveService.files().list()
                    .execute();
            for (File file : result.getFiles()) {
              if(file.getName().equals("App.db")){
                  fileId=file.getId();
                  mDriveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
              }
            }
            return  null;
        });
    }
//    public Task<Void> update() {
//        return Tasks.call(mExecutor, () -> {
//            //String fileId = "1Ah_ObNJiW83ClIxgBGiRiH4za673rlzp";
//            java.io.File data = Environment.getDataDirectory();
//            String currentDBPath = "/data/data/com.example.nccumis/databases/App.db";
//            File fileMetadata = new File();
//            fileMetadata.setName("App.db");
//            java.io.File filePath = new java.io.File(currentDBPath);
//            FileContent mediaContent = new FileContent("application/x-sqlite3", filePath);
//            FileList result = mDriveService.files().list()
//                    .execute();
//            for (File file : result.getFiles()) {
//                if(file.getName().equals("App.db")){
//                    fileId=file.getId();
//                    mDriveService.files().update(fileId,fileMetadata,mediaContent).execute();
//                }
//            }
//            //(fileMetadata, mediaContent)
//            //.setFields("id")
//            //.execute();
//            //System.out.println("File ID: " + file.getId());
//            return  null;
//        });
//    }
    public boolean get_restoresuccess(){
        return restoresucess;
    }

}