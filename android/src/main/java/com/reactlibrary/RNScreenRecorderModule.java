
package com.reactlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;

import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import java.io.IOException;

public class RNScreenRecorderModule extends SimpleViewManager<View> {
  private static final String TAG = "RecordingManager";
  private static final int REQUEST_CODE = 1000;
  private static final SparseIntArray ORIENTATIONS = new SparseIntArray();


  private MediaProjectionManager mediaProjectionManager;
  private MediaProjection mediaProjection;
  private VirtualDisplay virtualDisplay;
  private MediaProjectionCallback mediaProjectionCallback;
  private MediaRecorder mediaRecorder;

  //Screen info
  private int mScreenDensity;
  private int DISPLAY_WIDTH;
  private int DISPLAY_HEIGHT;


  static {
    ORIENTATIONS.append(Surface.ROTATION_0,90);
    ORIENTATIONS.append(Surface.ROTATION_90,0);
    ORIENTATIONS.append(Surface.ROTATION_180,270);
    ORIENTATIONS.append(Surface.ROTATION_270,180);
  }

  private boolean iIsRecording = false ;
  private boolean iHasRecording = false;
  private Activity mActivity;
  public boolean isRecording(){
    return iIsRecording;
  }
  public boolean hasRecording(){
    return iHasRecording;
  }



  @Override
  public String getName() {
    return "RNScreenRecorder";
  }

  @Override
  protected View createViewInstance(ThemedReactContext reactContext) {
    getScreenInfo();
    View mView = new View(reactContext);
    mView.setBackgroundColor(Color.BLUE);
    return mView;
  }
  private void getMainActivity(Activity activity){
    mActivity = activity;
  }
  private void getScreenInfo(){
    DisplayMetrics metrics = new DisplayMetrics();
    mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    mScreenDensity = metrics.densityDpi;
    DISPLAY_WIDTH = metrics.widthPixels;
    DISPLAY_HEIGHT = metrics.heightPixels;
    Log.i(TAG,DISPLAY_WIDTH+","+DISPLAY_HEIGHT+","+mScreenDensity);

    mediaRecorder = new MediaRecorder();
    mediaProjectionManager = (MediaProjectionManager)mActivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
  }

  @ReactMethod
  public void startRecording(){
    if(iIsRecording){ return;}

    initRecorder();
    recordScreen();
  }
  private void initRecorder() {
    try{
      mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
      mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
      mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
      mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
      mediaRecorder.setVideoSize(DISPLAY_WIDTH,DISPLAY_HEIGHT);
      mediaRecorder.setVideoEncodingBitRate(512*1000);
      mediaRecorder.setVideoFrameRate(30);
      mediaRecorder.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/Video.mp4");
      int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
      int orientation = ORIENTATIONS.get(rotation + 90);
      mediaRecorder.setOrientationHint(orientation);
      mediaRecorder.prepare();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  private void recordScreen(){

    if(mediaProjection == null){
      mActivity.startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),REQUEST_CODE);
      return;
    }
    virtualDisplay = createVirtualDisplay();
    mediaRecorder.start();

  }

  private VirtualDisplay createVirtualDisplay() {

    return mediaProjection.createVirtualDisplay("ScreenCapture",DISPLAY_WIDTH,DISPLAY_HEIGHT,mScreenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder.getSurface(),null,null);
  }
  /**
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode != REQUEST_CODE)
    {
      Toast.makeText(this, "unkonw error",Toast.LENGTH_SHORT ).show();
      return;
    }

    if(resultCode != RESULT_OK)
    {
      Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
      iIsRecording = false;
      iHasRecording = false;
      return;
    }
    try
    {
      mediaProjectionCallback = new MediaProjectionCallback();
      mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
      if(mediaProjection == null){
        Log.e(TAG,"media projection is null");
        return;
      }
      mediaProjection.registerCallback(mediaProjectionCallback, null);
      virtualDisplay = createVirtualDisplay();
      Log.i(TAG,"virtual display is created");
      iIsRecording = true;
      iHasRecording = true;
      mediaRecorder.start();
    } catch (Exception e){
      Log.e(TAG,e.toString());
    }
  }
   **/

  private class MediaProjectionCallback extends MediaProjection.Callback {
    @Override
    public void onStop() {
      if(iIsRecording)
      {
        mediaRecorder.stop();
        mediaRecorder.reset();
      }
      mediaProjection = null;
      stopScreenCapture();
      super.onStop();
    }
  }
  private void stopScreenCapture() {
    if(virtualDisplay != null)
    {
      virtualDisplay.release();
      virtualDisplay = null;
      destroyMediaProjection();
      iIsRecording = false;
    }
  }


  @ReactMethod
  public void stopRecording(){
    if(!iIsRecording){ return; }
    if(mediaRecorder == null) { return; }
    try
    {
      mediaRecorder.stop();
      mediaRecorder.reset();
      stopScreenCapture();
      Log.e(TAG,"screen capture stopped");
    } catch (Exception e){
      Log.e(TAG,e.toString());
    } finally{
      iIsRecording = false;
    }

  }
  private void destroyMediaProjection() {
    if(mediaProjection != null)
    {
      mediaProjection.unregisterCallback(mediaProjectionCallback);
      mediaProjection.stop();
      mediaProjection = null;
    }
  }

  @ReactMethod
  public void playRecording(){
    if(iIsRecording || !iHasRecording){
      Log.e(TAG,"isRecording: "+isRecording()+" hasRecording: "+hasRecording());
      return;
    }

    Intent intent = new Intent(mActivity,VideoActivity.class);
    mActivity.startActivity(intent);

  }
}