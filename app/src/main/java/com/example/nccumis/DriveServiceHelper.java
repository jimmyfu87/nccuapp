package com.example.nccumis;

import android.os.Environment;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    public String fileId;
    public String folderId;
    public boolean restoresucess;

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }

    public Task<Void> createorupdateFile() {
        return Tasks.call(mExecutor, () -> {
            FileList result = mDriveService.files().list()
                    .setQ("trashed = false")
                    .execute();
            //更新
            for (File file2 : result.getFiles()) {
                if (file2.getName().equals("App.db")) {
                    // folderId = file2.getId();
                    java.io.File data = Environment.getDataDirectory();
                    String currentDBPath = "/data/data/com.example.nccumis/databases/App.db";
                    File fileMetadata = new File();
                    fileMetadata.setName("App.db");
                    java.io.File filePath = new java.io.File(currentDBPath);
                    FileContent mediaContent = new FileContent("application/x-sqlite3", filePath);
                    mDriveService.files().update(file2.getId(), fileMetadata, mediaContent).execute();
                    //System.out.println("更新成功");
                    return null;
                }
            }
            //建立
            File filefolderMetadata = new File();
            filefolderMetadata.setName("購智帳備份檔(不可更動)");
            filefolderMetadata.setMimeType("application/vnd.google-apps.folder");
            File file = mDriveService.files().create(filefolderMetadata)
                    .setFields("id")
                    .execute();
            fileId = file.getId();
            java.io.File data = Environment.getDataDirectory();
            String currentDBPath = "/data/data/com.example.nccumis/databases/App.db";
            File fileMetadata = new File();
            fileMetadata.setName("App.db");
            fileMetadata.setParents(Collections.singletonList(fileId));
            java.io.File filePath = new java.io.File(currentDBPath);
            FileContent mediaContent = new FileContent("application/x-sqlite3", filePath);
            mDriveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            //System.out.println("建立成功");
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
//        public Task<Void> createFolder () {
//            return Tasks.call(mExecutor, () -> {
//                //建立
//                File filefolderMetadata = new File();
//                filefolderMetadata.setName("購智帳備份檔案");
//                filefolderMetadata.setMimeType("application/vnd.google-apps.folder");
//                File file = mDriveService.files().create(filefolderMetadata)
//                        .setFields("id")
//                        .execute();
//                return null;
//            });
//        }
    //還原
    public Task<Void> restore()  {
        return Tasks.call(mExecutor, () -> {
            java.io.File data = Environment.getDataDirectory();
            String currentDBPath = "/data/data/com.example.nccumis/databases/App.db";
            File fileMetadata = new File();
            fileMetadata.setName("App.db");
            java.io.File filePath = new java.io.File(currentDBPath);
            //String fileId = "1Ah_ObNJiW83ClIxgBGiRiH4za673rlzp";
            OutputStream outputStream = new FileOutputStream(currentDBPath);
            FileList result = mDriveService.files().list()
                    .setQ("trashed = false")
                    .execute();
            for (File file : result.getFiles()) {
                if (file.getName().equals("App.db")) {
                    fileId = file.getId();
                    mDriveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
                }
            }
            return null;
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
//    public Task<Void> deleteappdata () {
//        return Tasks.call(mExecutor, () -> {
//            FileList appDataFolderlist = mDriveService.files().list()
//                    .setSpaces("appDataFolder")
//                    .execute();
//            for(int i=0;i<appDataFolderlist.getFiles().size();i++) {
//                String tempid=appDataFolderlist.getFiles().get(i).getId();
//                mDriveService.files().delete(tempid).execute();
//            }
//            System.out.println("移除成功");
//            return null;
//        });
//    }
    public boolean checkcontains() throws IOException {
        FileList result = mDriveService.files().list()
                .setQ("trashed = false")
                .execute();
        for (File file : result.getFiles()) {
            if (file.getName().equals("App.db")) {
                return true;
            }
        }
        return false;
    }
}
