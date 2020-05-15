package com.fileopener;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;
import java.util.HashMap;

public class FileOpener extends ReactContextBaseJavaModule {

  public FileOpener(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "FileOpener";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    return constants;
  }

  @ReactMethod
  public void open(String fileArg, String contentType, Promise promise) throws JSONException {
  		File file = new File(fileArg);

  		if (file.exists()) {
  			try {
                Uri path;// = Uri.fromFile(file);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    path = Uri.fromFile(file);
                } else {
                    final String packageName = getCurrentActivity().getPackageName();
                    final String authority = new StringBuilder(packageName).append(".provider").toString();
                    System.out.println("autority===>"+authority);
                    System.out.println("content type==>"+contentType);
                    path=  FileProvider.getUriForFile(getReactApplicationContext(),authority, file);
                }

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, contentType);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getReactApplicationContext().startActivity(intent);

                promise.resolve("Open success!!");
  			} catch (android.content.ActivityNotFoundException e) {
                promise.reject("Open error!!");
  			}
  		} else {
            promise.reject("File not found");
  		}
  	}

}
