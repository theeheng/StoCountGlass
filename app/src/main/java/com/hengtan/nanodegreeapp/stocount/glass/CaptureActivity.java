/*
 * Copyright (C) 2014 ZXing authors
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

package com.hengtan.nanodegreeapp.stocount.glass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.mirasense.scanditsdk.ScanditSDKBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;
import com.mirasense.scanditsdk.interfaces.ScanditSDKOverlay;

import java.io.IOException;

/**
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements ScanditSDKListener {

  private static final String TAG = CaptureActivity.class.getSimpleName();

  // the barcode scanner picker
  private ScanditSDK mBarcodePicker;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);

    // Instantiate the default barcode picker
    ScanditSDKBarcodePicker picker = new ScanditSDKBarcodePicker(this, getString(R.string.scandit_app_key));

        /*
        Here, we disable all symbologies detection, except EAN13
        it frees up CPU resources
        */
    picker.set1DScanningEnabled(true);
    picker.set2DScanningEnabled(false);
    picker.setCodabarEnabled(false);
    picker.setCode128Enabled(false);
    picker.setCode39Enabled(false);
    picker.setCode93Enabled(false);
    picker.setDataMatrixEnabled(false);
    picker.setEan13AndUpc12Enabled(true);
    picker.setEan8Enabled(false);
    picker.setGS1DataBarEnabled(false);
    picker.setGS1DataBarExpandedEnabled(false);
    picker.setItfEnabled(false);
    picker.setMicroDataMatrixEnabled(false);
    picker.setMsiPlesseyEnabled(false);
    picker.setPdf417Enabled(false);
    picker.setQrEnabled(false);
    picker.setUpceEnabled(false);
    picker.setInverseRecognitionEnabled(false);

        /*
        We restrict the scanning area and zoom the viewfinder
         */
    picker.restrictActiveScanningArea(true);
    picker.setScanningHotSpotHeight(0.2f);
    picker.setZoom(0.5f);

    // All we want to show is the scanning view
    setContentView(picker);

    mBarcodePicker = picker;

    ScanditSDKOverlay mOv = mBarcodePicker.getOverlayView();
    mOv.addListener(this);

    // We set a small viewfinder according to the scanning area
    mOv.setViewfinderDimension(0.4f, 0.2f, 0.4f, 0.2f);

    //prevent screen dimming
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }

  @Override
  protected void onResume() {
    super.onResume();
    // Once the activity is in the foreground again, restart scanning.
    mBarcodePicker.startScanning();
  }

  @Override
  protected void onPause() {
    // When the activity is in the background immediately stop the
    // scanning to save resources and free the camera.
    mBarcodePicker.stopScanning();
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    if (mBarcodePicker != null && mBarcodePicker.isScanning()) {
      mBarcodePicker.stopScanning();
    }

    super.onDestroy();
  }

  @Override
  public void didScanBarcode(String barcode, String symbology) {
    Log.i(TAG, "(" + symbology + ") " + barcode);
    if (mBarcodePicker.isScanning()) {
      mBarcodePicker.stopScanning();
    }



 //   Toast.makeText(this,"barcode : "+barcode+ " , type : "+symbology, Toast.LENGTH_SHORT);
    /*Intent myIntent = new Intent(this, ProductLookupActivity.class);
    myIntent.putExtra("ScanResult", new ScanResult(barcode, symbology));
    startActivity(myIntent);
    */
  }

  @Override
  public void didCancel() {

  }

  @Override
  public void didManualSearch(String s) {

  }

}
