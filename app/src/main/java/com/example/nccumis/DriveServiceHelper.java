package com.example.nccumis;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
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

/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
public class DriveServiceHelper {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;
    public static String TYPE_GOOGLE_DRIVE_FILE = "application/vnd.google-apps.file";
    private SQLiteDatabase database;

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }
//    public Task<Void> saveFile() {
//        return Tasks.call(mExecutor, () -> {
//            java.io.File data = Environment.getDataDirectory();
//            String currentDBPath = "/data/data/com.example.nccumis/databases/App.db";
//            File fileMetadata = new File();
//            fileMetadata.setName("App.db");
//            java.io.File filePath = new java.io.File(currentDBPath);
//            FileContent mediaContent = new FileContent("application/x-sqlite3", filePath);
//            File file = mDriveService.files().create(fileMetadata, mediaContent)
//                    .setFields("id")
//                    .execute();
//            System.out.println("File ID: " + file.getId());
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
            String fileId = "1Ah_ObNJiW83ClIxgBGiRiH4za673rlzp";
            OutputStream outputStream = new FileOutputStream(currentDBPath);
            mDriveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            return  null;
    });
}


}