package com.robotemi.sdk.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import com.robotemi.sdk.BatteryData;
import com.robotemi.sdk.MediaObject;
import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.UserInfo;
import com.robotemi.sdk.activitystream.ActivityStreamObject;
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage;
import com.robotemi.sdk.constants.ContentType;
import com.robotemi.sdk.constants.Page;
import com.robotemi.sdk.constants.Platform;
import com.robotemi.sdk.constants.SdkConstants;
import com.robotemi.sdk.exception.OnSdkExceptionListener;
import com.robotemi.sdk.exception.SdkException;
import com.robotemi.sdk.face.ContactModel;
import com.robotemi.sdk.face.OnFaceRecognizedListener;
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener;
import com.robotemi.sdk.listeners.OnConstraintBeWithStatusChangedListener;
import com.robotemi.sdk.listeners.OnConversationStatusChangedListener;
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotLiftedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import com.robotemi.sdk.listeners.OnTelepresenceEventChangedListener;
import com.robotemi.sdk.listeners.OnTtsVisualizerFftDataChangedListener;
import com.robotemi.sdk.listeners.OnTtsVisualizerWaveFormDataChangedListener;
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener;
import com.robotemi.sdk.model.CallEventModel;
import com.robotemi.sdk.model.DetectionData;
import com.robotemi.sdk.model.MemberStatusModel;
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener;
import com.robotemi.sdk.navigation.listener.OnDistanceToLocationChangedListener;
import com.robotemi.sdk.navigation.listener.OnReposeStatusChangedListener;
import com.robotemi.sdk.navigation.model.Position;
import com.robotemi.sdk.navigation.model.SafetyLevel;
import com.robotemi.sdk.navigation.model.SpeedLevel;
import com.robotemi.sdk.permission.OnRequestPermissionResultListener;
import com.robotemi.sdk.permission.Permission;
import com.robotemi.sdk.sequence.OnSequencePlayStatusChangedListener;
import com.robotemi.sdk.sequence.SequenceModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.kist.rest_api_test.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.lang.String;
import java.util.*;
/**
 * 2021-01-21 vol_1
 */


public class MainActivity extends AppCompatActivity implements
        Robot.NlpListener,
        OnRobotReadyListener,
        Robot.ConversationViewAttachesListener,
        Robot.WakeupWordListener,
        Robot.ActivityStreamPublishListener,
        Robot.TtsListener,
        OnBeWithMeStatusChangedListener,
        OnGoToLocationStatusChangedListener,
        OnLocationsUpdatedListener,
        OnConstraintBeWithStatusChangedListener,
        OnDetectionStateChangedListener,
        Robot.AsrListener,
        OnTelepresenceEventChangedListener,
        OnRequestPermissionResultListener,
        OnDistanceToLocationChangedListener,
        OnCurrentPositionChangedListener,
        OnSequencePlayStatusChangedListener,
        OnRobotLiftedListener,
        OnDetectionDataChangedListener,
        OnUserInteractionChangedListener,
        OnFaceRecognizedListener,
        OnConversationStatusChangedListener,
        OnTtsVisualizerWaveFormDataChangedListener,
        OnTtsVisualizerFftDataChangedListener,
        OnReposeStatusChangedListener,
        OnSdkExceptionListener {

    public static final String ACTION_HOME_WELCOME = "home.welcome", ACTION_HOME_DANCE = "home.dance", ACTION_HOME_SLEEP = "home.sleep";
    public static final String HOME_BASE_LOCATION = "home base";
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final int REQUEST_CODE_NORMAL = 0;
    private static final int REQUEST_CODE_FACE_START = 1;
    private static final int REQUEST_CODE_FACE_STOP = 2;
    private static final int REQUEST_CODE_MAP = 3;
    private static final int REQUEST_CODE_SEQUENCE_FETCH_ALL = 4;
    private static final int REQUEST_CODE_SEQUENCE_PLAY = 5;
    private static final int REQUEST_CODE_START_DETECTION_WITH_DISTANCE = 6;
    private static final int REQUEST_CODE_SEQUENCE_PLAY_WITHOUT_PLAYER = 7;

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private EditText etSpeak, etSpeak2,etSaveLocation, etGoTo,etGoTo2, etDistance, etX, etY, etYaw, etNlu,etNlu2;

    private List<String> locations;

    private Robot robot;

    private CustomAdapter mAdapter;

    private TextView tvLog;

    private AppCompatImageView ivFace;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private TtsVisualizerView ttsVisualizerView;

    public String nam="home base";

    public String sentance=null;
    //public String err;
    public String recog;
    public String call;
    public String message;
    public int receive=0;
    public String retu=null;
    public String home="home base";
    public int tier=0;
    public String auto="off";
    public String abc="홈베이스로 복귀 하겠습니다.";

    public String sig=null;
    public String sender=null;
    public String user1="연구실";
    public String user2="길";
    public String user3="책상";
    public String user4="8226";
    public String user5="복도";
    public String user6="교차로";
    public String user7="중앙";
    //public String user8="8226";

    int m_nTimeOutConnection = 5000;
    int m_nTimeOutRead = 5000;

    public String postan=null;


    public int nu=0;

    // Debug log tag.
    private static final String TAG_HTTP_URL_CONNECTION = "HTTP_URL_CONNECTION";

    // Child thread sent message type value to activity main thread Handler.
    private static final int REQUEST_CODE_SHOW_RESPONSE_TEXT = 1;

    // The key of message stored server returned data.
    private static final String KEY_RESPONSE_TEXT = "KEY_RESPONSE_TEXT";

    // Request method GET. The value must be uppercase.
    private static final String REQUEST_METHOD_GET = "GET";

    // Request web page url input text box.
    private EditText requestUrlEditor = null;

    // Send http request button.
    private Button requestUrlButton = null;

    // TextView to display server returned page html text.
    private TextView responseTextView = null;

    // This handler used to listen to child thread show return page html text message and display those text in responseTextView.
    private Handler uiUpdater = null;

    private void startSendHttpRequestThread(final String reqUrl)
    {
        Thread sendHttpRequestThread = new Thread()
        {
            @Override
            public void run() {
                // Maintain http url connection.
                HttpURLConnection httpConn = null;

                // Read text input stream.
                InputStreamReader isReader = null;

                // Read text into buffer.
                BufferedReader bufReader = null;

                // Save server response text.
                StringBuffer readTextBuf = new StringBuffer();

                try {
                    // Create a URL object use page url.
                    // URL 설정
                    //  URL url = new URL(reqUrl);
                    URL url = new URL("http://3.36.128.133:2416/command/text_to_speech");


                    // Open http connection to web server.
                    httpConn = (HttpURLConnection)url.openConnection();

                    // Set http request method to get.
                    httpConn.setRequestMethod(REQUEST_METHOD_GET);

                    // Set connection timeout and read timeout value.
                    httpConn.setConnectTimeout(10000);
                    httpConn.setReadTimeout(10000);

                    // Get input stream from web url connection.
                    InputStream inputStream = httpConn.getInputStream();

                    // Create input stream reader based on url connection input stream.
                    isReader = new InputStreamReader(inputStream);

                    // Create buffered reader.
                    bufReader = new BufferedReader(isReader);

                    // Read line of text from server response.
                    String line = bufReader.readLine();

                    // Loop while return line is not null.
                    while(line != null)
                    {
                        // Append the text to string buffer.
                        readTextBuf.append(line);

                        // Continue to read text line.
                        line = bufReader.readLine();
                    }

                    // Send message to main thread to update response text in TextView after read all.
                    Message message = new Message();

                    // Set message type.
                    message.what = REQUEST_CODE_SHOW_RESPONSE_TEXT;

                    // Create a bundle object.
                    Bundle bundle = new Bundle();
                    // Put response text in the bundle with the special key.
                    bundle.putString(KEY_RESPONSE_TEXT, readTextBuf.toString());
                    // Set bundle data in message.
                    message.setData(bundle);
                    // Send message to main thread Handler to process.
                  //  uiUpdater.sendMessage(message);


                    String lin=readTextBuf.toString();
                    System.out.println(lin);

                    if(lin.startsWith("{")) {
                        JSONObject jobject=new JSONObject(lin);
                        String function = jobject.getString("body");
                        JSONObject jobject2=new JSONObject(function);
                        String function2 = jobject2.getString("text");
                        System.out.println(function);
                        System.out.println(function2);
                        TtsRequest ttsRequest = TtsRequest.create(function2.trim(), true);
                        robot.speak(ttsRequest);

                    }


                }catch(MalformedURLException ex)
                {
                    Log.e(TAG_HTTP_URL_CONNECTION, ex.getMessage(), ex);
                }catch(IOException ex)
                {
                    Log.e(TAG_HTTP_URL_CONNECTION, ex.getMessage(), ex);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bufReader != null) {
                            bufReader.close();
                            bufReader = null;
                        }

                        if (isReader != null) {
                            isReader.close();
                            isReader = null;
                        }

                        if (httpConn != null) {
                            httpConn.disconnect();
                            httpConn = null;
                        }
                    }catch (IOException ex)
                    {
                        Log.e(TAG_HTTP_URL_CONNECTION, ex.getMessage(), ex);
                    }
                }
            }
        };
        // Start the child thread to request web page.
        sendHttpRequestThread.start();
    }

    private void startSendHttpRequestThread2(final String reqUrl)
    {
        Thread sendHttpRequestThread2 = new Thread()
        {
            @Override
            public void run() {
                // Maintain http url connection.
                HttpURLConnection httpConn = null;

                // Read text input stream.
                InputStreamReader isReader = null;

                // Read text into buffer.
                BufferedReader bufReader = null;

                // Save server response text.
                StringBuffer readTextBuf = new StringBuffer();

                try {
                    // Create a URL object use page url.
                    // URL 설정
                    //  URL url = new URL(reqUrl);
                    URL url2 = new URL("http://3.36.128.133:2416/sensor_data/sound_to_text");


                    // Open http connection to web server.
                    httpConn = (HttpURLConnection)url2.openConnection();

                    // Set http request method to get.
                   // httpConn.setRequestMethod(REQUEST_METHOD_GET);

                    // Set connection timeout and read timeout value.
                    httpConn.setConnectTimeout(10000);
                    httpConn.setReadTimeout(10000);

                    httpConn.setRequestMethod("POST");
                    httpConn.setDoOutput(true);




                    //json으로 message를 전달하고자 할 때
                    httpConn.setRequestProperty("Content-Type", "application/json");
                    httpConn.setDoInput(true);
                    httpConn.setUseCaches(false);
                    httpConn.setDefaultUseCaches(false);
                    String strMessage= "{\"header\":{\"time\":\"02:16:51\",\"module\":\"sound_to_text\"},\"body\":{\"text\":\"마이봄\"}}";



                    OutputStreamWriter wr = new OutputStreamWriter(httpConn.getOutputStream());
                    printLog("ok");
                    wr.write(strMessage); //json 형식의 message 전달
                    wr.flush();

                    StringBuilder sb = new StringBuilder();
                    if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));

                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        br.close();

                        postan = sb.toString();
                        //         Log.e("GET:HTTP_OK", "" + sb.toString());
                    } else {
                        Log.e("POST:Else", httpConn.getResponseMessage());
                    }
                } catch (Exception e){
                    Log.e("POST:Exception", e.toString());
                }





/*


                // Get input stream from web url connection.
                    InputStream inputStream = httpConn.getInputStream();

                    // Create input stream reader based on url connection input stream.
                    isReader = new InputStreamReader(inputStream);

                    // Create buffered reader.
                    bufReader = new BufferedReader(isReader);

                    // Read line of text from server response.
                    String line = bufReader.readLine();

                    // Loop while return line is not null.
                    while(line != null)
                    {
                        // Append the text to string buffer.
                        readTextBuf.append(line);

                        // Continue to read text line.
                        line = bufReader.readLine();
                    }

                    // Send message to main thread to update response text in TextView after read all.
                    Message message = new Message();

                    // Set message type.
                    message.what = REQUEST_CODE_SHOW_RESPONSE_TEXT;

                    // Create a bundle object.
                    Bundle bundle = new Bundle();
                    // Put response text in the bundle with the special key.
                    bundle.putString(KEY_RESPONSE_TEXT, readTextBuf.toString());
                    // Set bundle data in message.
                    message.setData(bundle);
                    // Send message to main thread Handler to process.
                    //  uiUpdater.sendMessage(message);


                    String lin=readTextBuf.toString();
                    System.out.println(lin);

                    if(lin.startsWith("{")) {
                        JSONObject jobject=new JSONObject(lin);
                        String function = jobject.getString("body");
                        JSONObject jobject2=new JSONObject(function);
                        String function2 = jobject2.getString("text");
                        System.out.println(function);
                        System.out.println(function2);
                        TtsRequest ttsRequest = TtsRequest.create(function2.trim(), true);
                        robot.speak(ttsRequest);

                    }


                }catch(MalformedURLException ex)
                {
                    Log.e(TAG_HTTP_URL_CONNECTION, ex.getMessage(), ex);
                }catch(IOException ex)
                {
                    Log.e(TAG_HTTP_URL_CONNECTION, ex.getMessage(), ex);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bufReader != null) {
                            bufReader.close();
                            bufReader = null;
                        }

                        if (isReader != null) {
                            isReader.close();
                            isReader = null;
                        }

                        if (httpConn != null) {
                            httpConn.disconnect();
                            httpConn = null;
                        }
                    }catch (IOException ex)
                    {
                        Log.e(TAG_HTTP_URL_CONNECTION, ex.getMessage(), ex);
                    }
                }

                */
            }
        };
        // Start the child thread to request web page.
        sendHttpRequestThread2.start();
    }


    public String _post(String strBaseURL, String strPath, String strMessage) {
        Log.e("POST:_post", strMessage);
        String strResponse = "";

        try {
            URL url = new URL(strBaseURL + strPath);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setConnectTimeout(m_nTimeOutConnection); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(m_nTimeOutRead); // InputStream 읽어 오는 Timeout 시간 설정
            // con.addRequestProperty("x-api-key", RestTestCommon.API_KEY); //key값 설정

            con.setRequestMethod("POST");
            con.setDoOutput(true); //POST 데이터를 OutputStream으로 넘겨 주겠다는 설정

            //json으로 message를 전달하고자 할 때
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(strMessage); //json 형식의 message 전달
            wr.flush();

            StringBuilder sb = new StringBuilder();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();

                strResponse = sb.toString();
                //         Log.e("GET:HTTP_OK", "" + sb.toString());
            } else {
                Log.e("POST:Else", con.getResponseMessage());
            }
        } catch (Exception e){
            Log.e("POST:Exception", e.toString());
        }

        return strResponse;
    }





    /**
     * Hiding keyboard after every button press
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to grant permissions
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * Setting up all the event listeners
     */
    @Override
    protected void onStart() {
        super.onStart();
        robot.addOnRobotReadyListener(this);
        robot.addNlpListener(this);
        robot.addOnBeWithMeStatusChangedListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addConversationViewAttachesListenerListener(this);
        robot.addWakeupWordListener(this);
        robot.addTtsListener(this);
        robot.addOnLocationsUpdatedListener(this);
        robot.addOnConstraintBeWithStatusChangedListener(this);
        robot.addOnDetectionStateChangedListener(this);
        robot.addAsrListener(this);
        robot.addOnDistanceToLocationChangedListener(this);
        robot.addOnCurrentPositionChangedListener(this);
        robot.addOnSequencePlayStatusChangedListener(this);
        robot.addOnRobotLiftedListener(this);
        robot.addOnDetectionDataChangedListener(this);
        robot.addOnUserInteractionChangedListener(this);
        robot.addOnConversationStatusChangedListener(this);
        robot.addOnTtsVisualizerWaveFormDataChangedListener(this);
        robot.addOnTtsVisualizerFftDataChangedListener(this);
        robot.addOnReposeStatusChangedListener(this);
        robot.showTopBar();
    }

    /**
     * Removing the event listeners upon leaving the app.
     */
    @Override
    protected void onStop() {
        super.onStop();
        robot.removeOnRobotReadyListener(this);
        robot.removeNlpListener(this);
        robot.removeOnBeWithMeStatusChangedListener(this);
        robot.removeOnGoToLocationStatusChangedListener(this);
        robot.removeConversationViewAttachesListenerListener(this);
        robot.removeWakeupWordListener(this);
        robot.removeTtsListener(this);
        robot.removeOnLocationsUpdateListener(this);
        robot.removeOnDetectionStateChangedListener(this);
        robot.removeAsrListener(this);
        robot.removeOnDistanceToLocationChangedListener(this);
        robot.removeOnCurrentPositionChangedListener(this);
        robot.removeOnSequencePlayStatusChangedListener(this);
        robot.removeOnRobotLiftedListener(this);
        robot.removeOnDetectionDataChangedListener(this);
        robot.addOnUserInteractionChangedListener(this);
        robot.stopMovement();
        robot.stopFaceRecognition();
        robot.removeOnConversationStatusChangedListener(this);
        robot.removeOnTtsVisualizerWaveFormDataChangedListener(this);
        robot.removeOnTtsVisualizerFftDataChangedListener(this);
        robot.removeOnReposeStatusChangedListener(this);
    }

    /**
     * Places this application in the top bar for a quick access shortcut.
     */
    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                final ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                // Robot.getInstance().onStart() method may change the visibility of top bar.
                robot.onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews();
        verifyStoragePermissions(this);
        robot = Robot.getInstance(); // get an instance of the robot in order to begin using its features.
        robot.addOnRequestPermissionResultListener(this);
        robot.addOnTelepresenceEventChangedListener(this);
        robot.addOnFaceRecognizedListener(this);
        robot.addOnSdkExceptionListener(this);
    }

    @Override
    protected void onDestroy() {
        robot.removeOnRequestPermissionResultListener(this);
        robot.removeOnTelepresenceEventChangedListener(this);
        robot.removeOnFaceRecognizedListener(this);
        robot.removeOnSdkExceptionListener(this);
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
        }
        super.onDestroy();
    }

    public void initViews() {
       // etSpeak = findViewById(R.id.etSpeak);
       // etSpeak2 = findViewById(R.id.etSpeak2);

       // etSaveLocation = findViewById(R.id.etSaveLocation);
      //  etGoTo = findViewById(R.id.etGoTo);
      //  etGoTo2= findViewById(R.id.etGoTo2);
       // etDistance = findViewById(R.id.etDistance);
        tvLog = findViewById(R.id.tvLog);
        tvLog.setMovementMethod(new ScrollingMovementMethod());
        //etX = findViewById(R.id.etX);
       // etY = findViewById(R.id.etY);
      //  etYaw = findViewById(R.id.etYaw);
        //etNlu = findViewById(R.id.etNlu);
        ivFace = findViewById(R.id.imageViewFace);
        ttsVisualizerView = findViewById(R.id.visualizerView);
    }

    /**
     * Have the robot speak while displaying what is being said.
     */
    public void speak(View view) {
        /////
        ///
        //
        TtsRequest ttsRequest = TtsRequest.create(etSpeak.getText().toString().trim(), true);
        robot.speak(ttsRequest);
        hideKeyboard();
        //
        ///
        ////
    }

    /**
     * This is an example of saving locations.
     */
    public void saveLocation(View view) {
        String location = etSaveLocation.getText().toString().toLowerCase().trim();
        boolean result = robot.saveLocation(location);
        if (result) {
            robot.speak(TtsRequest.create("I've successfully saved the " + location + " location.", true));
        } else {
            robot.speak(TtsRequest.create("Saved the " + location + " location failed.", true));
        }
        hideKeyboard();
    }

    /**
     * goTo checks that the location sent is saved then goes to that location.
     */
    public void goTo(View view) {
       // nu=0;
        /*
        for (String location : robot.getLocations()) {
            if (location.equals(etGoTo.getText().toString().toLowerCase().trim())) {
                //nu++;

                robot.goTo(etGoTo.getText().toString().toLowerCase().trim());

                hideKeyboard();
            }
        }
        */
        // sender=etGoTo.getText().toString();
        if(receive==1){
            TtsRequest ttsRequest2 = TtsRequest.create(sentance+message, true);
            robot.speak(ttsRequest2);
            sig = null;
            call=null;
            hideKeyboard();
        }
    }
    public void goTo2(View view) {
        retu="홈베이스 로 복귀 하겠습니다.";
        TtsRequest ttsRequest3 = TtsRequest.create(retu, true);
        robot.speak(ttsRequest3);
        tier=0;
        receive=0;
        sentance=null;
        message=null;
        call=null;
        sig=null;
        recog=null;
        hideKeyboard();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.goTo(home.trim());
        hideKeyboard();
    }


    public void goTo3(View view) {
        // nu=0;
        /*
        for (String location : robot.getLocations()) {
            if (location.equals(etGoTo.getText().toString().toLowerCase().trim())) {
                //nu++;

                robot.goTo(etGoTo.getText().toString().toLowerCase().trim());

                hideKeyboard();
            }
        }
        */
        // sender=etGoTo.getText().toString();
        tier=0;
        receive=0;
        sentance=null;
        message=null;
        call=null;
        sig=null;
        recog=null;
        printLog("대화가 초기화 되었습니다. 처음부터 진행해 주세요");

        //  TtsRequest ttsRequest4 = TtsRequest.create("대화가 초기화 되었습니다. 처음부터 재입력 해주세요", true);
        // robot.speak(ttsRequest4);

        hideKeyboard();

    }

    public void goTo4(View view) {
        // nu=0;
        /*
        for (String location : robot.getLocations()) {
            if (location.equals(etGoTo.getText().toString().toLowerCase().trim())) {
                //nu++;

                robot.goTo(etGoTo.getText().toString().toLowerCase().trim());

                hideKeyboard();
            }
        }
        */
        // sender=etGoTo.getText().toString();
        tier=0;
        receive=0;
        sentance=null;
        message=null;
        call=null;
        sig=null;
        recog=null;

        //  TtsRequest ttsRequest4 = TtsRequest.create("대화가 초기화 되었습니다. 처음부터 재입력 해주세요", true);
        // robot.speak(ttsRequest4);

        hideKeyboard();
        String reqUrl=null;
        startSendHttpRequestThread(reqUrl);
    }

    public void goTo5(View view) {
        // nu=0;
        /*
        for (String location : robot.getLocations()) {
            if (location.equals(etGoTo.getText().toString().toLowerCase().trim())) {
                //nu++;

                robot.goTo(etGoTo.getText().toString().toLowerCase().trim());

                hideKeyboard();
            }
        }
        */
        // sender=etGoTo.getText().toString();
        tier=0;
        receive=0;
        sentance=null;
        message=null;
        call=null;
        sig=null;
        recog=null;

        //  TtsRequest ttsRequest4 = TtsRequest.create("대화가 초기화 되었습니다. 처음부터 재입력 해주세요", true);
        // robot.speak(ttsRequest4);

        hideKeyboard();

/*

        String post= "{\"header\":{\"time\":\"02:16:51\",\"module\":\"sound_to_text\"},\"body\":{\"text\":\"마이봄\"}}";

       String respon= _post("http://3.36.128.133:2416","sensor_data/sound_to_text",post);
       if(respon==null){
           String stand="반응이 없습니다.";
           TtsRequest ttsRequest7 = TtsRequest.create(stand.trim(), true);
           robot.speak(ttsRequest7);
           System.out.println(stand);
       }
        TtsRequest ttsRequest6 = TtsRequest.create(respon.trim(), true);
        robot.speak(ttsRequest6);
        */
        String reqUrl=null;
        startSendHttpRequestThread2(reqUrl);
    }

    public void goTo6(View view) {
        if(auto=="on"){
            auto="off";
            printLog("자동 복귀 기능 OFF");
        }
        else{
            auto="on";
            printLog("자동 복귀 기능 ON");
        }

        tier=0;
        receive=0;
        sentance=null;
        message=null;
        call=null;
        sig=null;
        recog=null;





        hideKeyboard();
    }


    /**
     * stopMovement() is used whenever you want the robot to stop any movement
     * it is currently doing.
     */
    public void stopMovement(View view) {
        robot.stopMovement();
        robot.speak(TtsRequest.create("And so I have stopped", true));
    }

    /**
     * Simple follow me example.
     */
    public void followMe(View view) {
        TtsRequest ttsRequest = TtsRequest.create("yes i follow you, master", true);
        robot.speak(ttsRequest);
        //hideKeyboard();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        robot.beWithMe();
        hideKeyboard();
    }

    /**
     * Manually navigate the robot with skidJoy, tiltAngle, turnBy and tiltBy.
     * skidJoy moves the robot exactly forward for about a second. It controls both
     * the linear and angular velocity. Float numbers must be between -1.0 and 1.0
     */
    public void skidJoy(View view) {
        long t = System.currentTimeMillis();
        long end = t + 70;
        while (System.currentTimeMillis() < end) {
            robot.skidJoy(0.05F, 0F);
        }
    }
    public void skidJoy2(View view){
        long t = System.currentTimeMillis();
        long end = t + 300;
        while (System.currentTimeMillis() < end) {
            robot.skidJoy(-1F, 0F);
        }
    }

    /**
     * tiltAngle controls temi's head by specifying which angle you want
     * to tilt to and at which speed. 23
     */
    public void tiltAngle(View view) {
        robot.tiltAngle(10);
    }

    /**
     * turnBy allows for turning the robot around in place. You can specify
     * the amount of degrees to turn by and at which speed.
     */
    public void turnBy(View view) {
        robot.turnBy(15);
    }
    public void turnBy2(View view) {
        robot.turnBy(-15);
    }


    /**
     * tiltBy is used to tilt temi's head from its current position.
     */
    public void tiltBy(View view) {
        robot.tiltBy(15);
    }

    /**
     * getBatteryData can be used to return the current battery status.
     */
    public void getBatteryData(View view) {
        BatteryData batteryData = robot.getBatteryData();
        if (batteryData == null) {
            printLog("getBatteryData()", "batteryData is null");
            return;
        }
        if (batteryData.isCharging()) {
            TtsRequest ttsRequest = TtsRequest.create(batteryData.getBatteryPercentage() + " percent battery and charging.", true);
            robot.speak(ttsRequest);
        } else {
            TtsRequest ttsRequest = TtsRequest.create(batteryData.getBatteryPercentage() + " percent battery and not charging.", true);
            robot.speak(ttsRequest);
        }
    }

    /**
     * Display the saved locations in a dialog
     */
    public void savedLocationsDialog(View view) {
        hideKeyboard();
        locations = robot.getLocations();
        mAdapter = new CustomAdapter(MainActivity.this, android.R.layout.simple_selectable_list_item, locations);
        AlertDialog.Builder versionsDialog = new AlertDialog.Builder(MainActivity.this);
        versionsDialog.setTitle("Saved Locations: (Click to delete the location)");
        versionsDialog.setPositiveButton("OK", null);
        versionsDialog.setAdapter(mAdapter, null);
        AlertDialog dialog = versionsDialog.create();
        dialog.getListView().setOnItemClickListener((parent, view1, position, id) -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Delete location \"" + mAdapter.getItem(position) + "\" ?");
            builder.setPositiveButton("No thanks", (dialog1, which) -> {

            });
            builder.setNegativeButton("Yes", (dialog1, which) -> {
                String location = mAdapter.getItem(position);
                if (location == null) {
                    return;
                }
                boolean result = robot.deleteLocation(location);
                if (result) {
                    locations.remove(position);
                    robot.speak(TtsRequest.create(location + "delete successfully!", false));
                    mAdapter.notifyDataSetChanged();
                } else {
                    robot.speak(TtsRequest.create(location + "delete failed!", false));
                }
            });
            Dialog deleteDialog = builder.create();
            deleteDialog.show();
        });
        dialog.show();
    }

    /**
     * When adding the Nlp Listener to your project you need to implement this method
     * which will listen for specific intents and allow you to respond accordingly.
     * <p>
     * See AndroidManifest.xml for reference on adding each intent.
     */
    @Override
    public void onNlpCompleted(NlpResult nlpResult) {
        //do something with nlp result. Base the action specified in the AndroidManifest.xml
        Toast.makeText(MainActivity.this, nlpResult.action, Toast.LENGTH_SHORT).show();
        switch (nlpResult.action) {
            case ACTION_HOME_WELCOME:
                robot.tiltAngle(23);
                break;

            case ACTION_HOME_DANCE:
                long t = System.currentTimeMillis();
                long end = t + 5000;
                while (System.currentTimeMillis() < end) {
                    robot.skidJoy(0F, 1F);
                }
                break;

            case ACTION_HOME_SLEEP:
                robot.goTo(HOME_BASE_LOCATION);
                break;
        }
    }

    /**
     * callOwner is an example of how to use telepresence to call an individual.
     */
    public void callOwner(View view) {
        UserInfo admin = robot.getAdminInfo();
        if (admin == null) {
            printLog("callOwner()", "adminInfo is null.");
            return;
        }
        robot.startTelepresence(admin.getName(), admin.getUserId());
    }

    /**
     * publishToActivityStream takes an image stored in the resources folder
     * and uploads it to the mobile application under the Activities tab.
     */
    public void publishToActivityStream(View view) {
        ActivityStreamObject activityStreamObject;
        if (robot != null) {
            final String fileName = "puppy.png";
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.puppy);
            File puppiesFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), fileName);
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(puppiesFile);
                bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            activityStreamObject = ActivityStreamObject.builder()
                    .activityType(ActivityStreamObject.ActivityType.PHOTO)
                    .title("Puppy")
                    .media(MediaObject.create(MediaObject.MimeType.IMAGE, puppiesFile))
                    .build();

            try {
                robot.shareActivityObject(activityStreamObject);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            robot.speak(TtsRequest.create("Uploading Image", false));
        }
    }

    public void hideTopBar(View view) {
        robot.hideTopBar();
    }

    public void showTopBar(View view) {
        robot.showTopBar();
    }

    @Override
    public void onWakeupWord(@NotNull String wakeupWord, int direction) {
        // Do anything on wakeup. Follow, go to location, or even try creating dance moves.
        printLog("onWakeupWord", wakeupWord + ", " + direction);
    }

    @Override
    public void onTtsStatusChanged(@NotNull TtsRequest ttsRequest) {
        // Do whatever you like upon the status changing. after the robot finishes speaking



    }

    @Override
    public void onBeWithMeStatusChanged(String status) {
        //  When status changes to "lock" the robot recognizes the user and begin to follow.
        switch (status) {
            case OnBeWithMeStatusChangedListener.ABORT:
                // do something i.e. speak
                robot.speak(TtsRequest.create("Abort", false));
                break;

            case OnBeWithMeStatusChangedListener.CALCULATING:
                robot.speak(TtsRequest.create("Calculating", false));
                break;

            case OnBeWithMeStatusChangedListener.LOCK:
                robot.speak(TtsRequest.create("Lock", false));
                break;

            case OnBeWithMeStatusChangedListener.SEARCH:
                robot.speak(TtsRequest.create("search", false));
                break;

            case OnBeWithMeStatusChangedListener.START:
                robot.speak(TtsRequest.create("Start", false));
                break;

            case OnBeWithMeStatusChangedListener.TRACK:
                robot.speak(TtsRequest.create("Track", false));
                break;
        }
    }

    @Override
    public void onGoToLocationStatusChanged(@NotNull String location, String status, int descriptionId, @NotNull String description) {
        printLog("GoToStatusChanged", "status=" + status + ", descriptionId=" + descriptionId + ", description=" + description);
        robot.speak(TtsRequest.create(description, false));
        switch (status) {
            case OnGoToLocationStatusChangedListener.START:
                //robot.speak(TtsRequest.create("Starting", false));
                receive=0;
                break;

            case OnGoToLocationStatusChangedListener.CALCULATING:
               // robot.speak(TtsRequest.create("Calculating", false));

                break;

            case OnGoToLocationStatusChangedListener.GOING:
               // robot.speak(TtsRequest.create("Going", false));
                break;

            case OnGoToLocationStatusChangedListener.COMPLETE:
               // robot.speak(TtsRequest.create("Completed", false));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if(sig!=null) {

                        receive = 1;
                        TtsRequest ttsRequest1 = TtsRequest.create(call, true);
                        robot.speak(ttsRequest1);
                        sig = null;
                        call = null;
                        hideKeyboard();



                }

                if(auto=="on"){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    TtsRequest ttsRequest2 = TtsRequest.create(sentance+message+abc, true);
                    robot.speak(ttsRequest2);

                    sig = null;
                    call=null;
                    tier=0;
                    receive=0;
                    sentance=null;
                    message=null;
                    recog=null;

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    auto="off";


                    robot.goTo(home.trim());
                }

              //  sentance=null;

                /*
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                 */
                 /**
                 * 발화 부분 vvvvvvvvvvv
                 */
/*
                if(nu==1) {
                    TtsRequest ttsRequest = TtsRequest.create(etSpeak.getText().toString().trim(), true);
                    robot.speak(ttsRequest);
                    hideKeyboard();

                    if(etNlu!=null){
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        robot.startDefaultNlu(etNlu.getText().toString());  // this is startNlu original code 나머지는 다 수정 한것
                    }
                }
                if(nu==2){
                    TtsRequest ttsRequest = TtsRequest.create(etSpeak2.getText().toString().trim(), true);
                    robot.speak(ttsRequest);
                    hideKeyboard();

                    if(etNlu2!=null){
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        robot.startDefaultNlu(etNlu2.getText().toString());  // this is startNlu original code 나머지는 다 수정 한것
                    }
                }

*/
                /**
                 * 발화 부분 ^^^^^^^^^
                 */
/*
                if(nu<2) {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (String location2 : robot.getLocations()) {
                        if (location2.equals(etGoTo2.getText().toString().toLowerCase().trim())) {
                            nu++;

                            robot.goTo(etGoTo2.getText().toString().toLowerCase().trim());

                            hideKeyboard();
                        }
                    }
                }
*/
                break;
            case OnGoToLocationStatusChangedListener.ABORT:
               // robot.speak(TtsRequest.create("Cancelled", false));
                /*
                if(nu==3){
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.speak(TtsRequest.create("Too much obstacle, So i can't move", true));
                }
                if(nu==1) {

                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                        for (String location2 : robot.getLocations()) {
                            if (location2.equals(etGoTo2.getText().toString().toLowerCase().trim())) {
                                nu++;

                                robot.goTo(etGoTo2.getText().toString().toLowerCase().trim());

                                hideKeyboard();
                            }
                    }
                }
                if(nu==2) {

                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    for (String location3 : robot.getLocations()) {
                        if (location3.equals(nam.toLowerCase().trim())) {
                            nu++;

                            robot.goTo(nam.toLowerCase().trim());

                            hideKeyboard();
                        }

                    }
                }
*/
                break;
        }
    }

    @Override
    public void onConversationAttaches(boolean isAttached) {
        //Do something as soon as the conversation is displayed.
        printLog("onConversationAttaches", "isAttached:" + isAttached);
    }

    @Override
    public void onPublish(@NotNull ActivityStreamPublishMessage message) {
        //After the activity stream finished publishing (photo or otherwise).
        //Do what you want based on the message returned.
        robot.speak(TtsRequest.create("Uploaded.", false));
    }

    @Override
    public void onLocationsUpdated(@NotNull List<String> locations) {
        //Saving or deleting a location will update the list.
        Toast.makeText(this, "Locations updated :\n" + locations, Toast.LENGTH_LONG).show();
    }

    public void disableWakeup(View view) {
        robot.toggleWakeup(true);
    }

    public void enableWakeup(View view) {
        robot.toggleWakeup(false);
    }

    public void toggleNavBillboard(View view) {
        if (requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
            return;
        }
        robot.toggleNavigationBillboard(!robot.isNavigationBillboardDisabled());
    }

    @Override
    public void onConstraintBeWithStatusChanged(boolean isConstraint) {
        printLog("onConstraintBeWith", "status = " + isConstraint);
    }

    @Override
    public void onDetectionStateChanged(int state) {
        printLog("onDetectionStateChanged: state = " + state);
        if (state == OnDetectionStateChangedListener.DETECTED) {
            robot.constraintBeWith();
        } else if (state == OnDetectionStateChangedListener.IDLE) {
            robot.stopMovement();
        }
    }

    /**
     * If you want to cover the voice flow in Launcher OS,
     * please add following meta-data to AndroidManifest.xml.
     * <pre>
     * <meta-data
     *     android:name="com.robotemi.sdk.metadata.KIOSK"
     *     android:value="true" />
     *
     * <meta-data
     *     android:name="com.robotemi.sdk.metadata.OVERRIDE_NLU"
     *     android:value="true" />
     * <pre>
     * And also need to select this App as the Kiosk Mode App in Settings > Kiosk Mode.
     *
     * @param asrResult The result of the ASR after waking up temi.
     */
    @Override
    public void onAsrResult(final @NonNull String asrResult) {
        printLog("onAsrResult", "asrResult = " + asrResult);
        try {
            Bundle metadata = getPackageManager()
                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA)
                    .metaData;
            if (metadata == null) return;
            if (!robot.isSelectedKioskApp()) return;
            if (!metadata.getBoolean(SdkConstants.METADATA_OVERRIDE_NLU)) return;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }
        /*
        if(sig!=null){
            sentance=asrResult;
            robot.finishConversation();
            robot.goTo(user1);

        }
        */
        if(recog!=null) {
            if(tier==2){
                tier=0;
                message=asrResult;
                robot.finishConversation();
                robot.goTo(sig);

            }


            else if (asrResult.equalsIgnoreCase("발신")&&tier==0) {
                robot.askQuestion("수신자 이름을 말씀해 주세요");

                tier=1;
                receive=0;
                sentance = recog+"님이 발신 했습니다.";

            }
/*
            else if (asrResult.equalsIgnoreCase("Play music")) {
                robot.speak(TtsRequest.create("Okay, please enjoy.", false));
                robot.finishConversation();
                playMusic();
            } else if (asrResult.equalsIgnoreCase("Play movie")) {
                robot.speak(TtsRequest.create("Okay, please enjoy.", false));
                robot.finishConversation();
                playMovie();
            } else if (asrResult.toLowerCase().contains("follow me")) {

                robot.finishConversation();
                robot.beWithMe();

            }
        else if (asrResult.toLowerCase().contains("home base")) {
            sentance=asrResult;
            robot.finishConversation();
            robot.goTo("home base");
        }
        else if (asrResult.toLowerCase().contains("door")) {
            sentance=asrResult;
            robot.finishConversation();
            robot.goTo("door");
        }
        else if (asrResult.toLowerCase().contains("street")) {

            sentance=asrResult;

            robot.finishConversation();
            robot.goTo("street");
        }
        else if (asrResult.toLowerCase().contains("table")) {
            sentance=asrResult;
            robot.finishConversation();
            robot.goTo("table");
        }
        else if (asrResult.toLowerCase().contains("tv")) {
            sentance=asrResult;
            robot.finishConversation();
            robot.goTo("tv");
        }
        else if (asrResult.toLowerCase().contains("corner")) {
            sentance=asrResult;
            robot.finishConversation();
            robot.goTo("corner");
        }
        else if (asrResult.toLowerCase().contains("water")) {
            sentance=asrResult;
            robot.finishConversation();
            robot.goTo("water");
        }
        else if (asrResult.toLowerCase().contains("hallway")) {
            sentance=asrResult;
            robot.finishConversation();
            robot.goTo("hallway");
        }
        */ else if (asrResult.equalsIgnoreCase(user1)&&tier==1) {
                tier=2;
                sig = user1;
                call=sig+"님 호출 입니다. 수신 버튼을 눌러 주세요";
                robot.askQuestion("전달할 메시지를 말씀해 주세요");
            }
            else if (asrResult.equalsIgnoreCase(user2)&&tier==1) {
                tier=2;
                sig = user2;
                call=sig+"님 호출 입니다. 수신 버튼을 눌러 주세요";
                robot.askQuestion("전달할 메시지를 말씀해 주세요");
            }
            else if (asrResult.equalsIgnoreCase(user3)&&tier==1) {
                tier=2;
                sig = user3;
                call=sig+"님 호출 입니다. 수신 버튼을 눌러 주세요";
                robot.askQuestion("전달할 메시지를 말씀해 주세요");
            }
            else if (asrResult.equalsIgnoreCase(user4)&&tier==1) {
                tier=2;
                sig = user4;
                call=sig+"님 호출 입니다. 수신 버튼을 눌러 주세요";
                robot.askQuestion("전달할 메시지를 말씀해 주세요");
            }
            else if (asrResult.equalsIgnoreCase(user5)&&tier==1) {
                tier=2;
                sig = user5;
                call=sig+"님 호출 입니다. 수신 버튼을 눌러 주세요";
                robot.askQuestion("전달할 메시지를 말씀해 주세요");
            }
            else if (asrResult.equalsIgnoreCase(user6)&&tier==1) {
                tier=2;
                sig = user6;
                call=sig+"님 호출 입니다. 수신 버튼을 눌러 주세요";
                robot.askQuestion("전달할 메시지를 말씀해 주세요");
            }
            else if (asrResult.equalsIgnoreCase(user7)&&tier==1) {
                tier=2;
                sig = user7;
                call=sig+"님 호출 입니다. 수신 버튼을 눌러 주세요";
                robot.askQuestion("전달할 메시지를 말씀해 주세요");
            }

            else {
                robot.askQuestion("잘 못 들었습니다. 다시 말씀 해주세요.");
            }
        }
    }

    private void playMovie() {
        // Play movie...
        printLog("onAsrResult", "Play movie...");
    }

    private void playMusic() {
        // Play music...
        printLog("onAsrResult", "Play music...");
    }

    public void privacyModeOn(View view) {
        robot.setPrivacyMode(true);
        Toast.makeText(this, robot.getPrivacyMode() + "", Toast.LENGTH_SHORT).show();
    }

    public void privacyModeOff(View view) {
        robot.setPrivacyMode(false);
        Toast.makeText(this, robot.getPrivacyMode() + "", Toast.LENGTH_SHORT).show();
    }

    public void getPrivacyModeState(View view) {
        Toast.makeText(this, robot.getPrivacyMode() + "", Toast.LENGTH_SHORT).show();
    }

    public void isHardButtonsEnabled(View view) {
        Toast.makeText(this, robot.isHardButtonsDisabled() + "", Toast.LENGTH_SHORT).show();
    }

    public void disableHardButtons(View view) {
        robot.setHardButtonsDisabled(true);
        Toast.makeText(this, robot.isHardButtonsDisabled() + "", Toast.LENGTH_SHORT).show();
    }

    public void enableHardButtons(View view) {
        robot.setHardButtonsDisabled(false);
        Toast.makeText(this, robot.isHardButtonsDisabled() + "", Toast.LENGTH_SHORT).show();
    }

    public void getOSVersion(View view) {
        String osVersion = String.format("LauncherOs: %s, RoboxVersion: %s", robot.getLauncherVersion(), robot.getRoboxVersion());
        Toast.makeText(this, osVersion, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTelepresenceEventChanged(@NotNull CallEventModel callEventModel) {
        printLog("onTelepresenceEvent", callEventModel.toString());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onRequestPermissionResult(@NotNull Permission permission, int grantResult, int requestCode) {
        String log = String.format("Permission: %s, grantResult: %d", permission.getValue(), grantResult);
        Toast.makeText(this, log, Toast.LENGTH_SHORT).show();
        printLog("onRequestPermission", log);
        if (grantResult == Permission.DENIED) {
            return;
        }
        // Permission is granted. Continue the action or workflow in your app.
        switch (permission) {
            case FACE_RECOGNITION:
                if (requestCode == REQUEST_CODE_FACE_START) {
                    robot.startFaceRecognition();
                } else if (requestCode == REQUEST_CODE_FACE_STOP) {
                    robot.stopFaceRecognition();
                }
                break;
            case SEQUENCE:
                if (requestCode == REQUEST_CODE_SEQUENCE_FETCH_ALL) {
                    getAllSequences();
                } else if (requestCode == REQUEST_CODE_SEQUENCE_PLAY) {
                    playFirstSequence(true);
                } else if (requestCode == REQUEST_CODE_SEQUENCE_PLAY_WITHOUT_PLAYER) {
                    playFirstSequence(false);
                }
                break;
            case MAP:
                if (requestCode == REQUEST_CODE_MAP) {
                    getMap();
                }
                break;
            case SETTINGS:
                if (requestCode == REQUEST_CODE_START_DETECTION_WITH_DISTANCE) {
                    startDetectionWithDistance();
                }
                break;
        }
    }

    public void requestFace(View view) {
        if (robot.checkSelfPermission(Permission.FACE_RECOGNITION) == Permission.GRANTED) {
            Toast.makeText(this, "You already had FACE_RECOGNITION permission.", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.FACE_RECOGNITION);
        robot.requestPermissions(permissions, REQUEST_CODE_NORMAL);
    }

    public void requestMap(View view) {
        if (robot.checkSelfPermission(Permission.MAP) == Permission.GRANTED) {
            Toast.makeText(this, "You already had MAP permission.", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MAP);
        robot.requestPermissions(permissions, REQUEST_CODE_NORMAL);
    }

    public void requestSettings(View view) {
        if (robot.checkSelfPermission(Permission.SETTINGS) == Permission.GRANTED) {
            Toast.makeText(this, "You already had SETTINGS permission.", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.SETTINGS);
        robot.requestPermissions(permissions, REQUEST_CODE_NORMAL);
    }

    public void requestSequence(View view) {
        if (robot.checkSelfPermission(Permission.SEQUENCE) == Permission.GRANTED) {
            Toast.makeText(this, "You already had SEQUENCE permission.", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.SEQUENCE);
        robot.requestPermissions(permissions, REQUEST_CODE_NORMAL);
    }

    public void requestAll(View view) {
        List<Permission> permissions = new ArrayList<>();
        for (Permission permission : Permission.values()) {
            if (robot.checkSelfPermission(permission) == Permission.GRANTED) {
                Toast.makeText(this, String.format("You already had '%s' permission.", permission.toString()), Toast.LENGTH_SHORT).show();
                continue;
            }
            permissions.add(permission);
        }
        robot.requestPermissions(permissions, REQUEST_CODE_NORMAL);
    }

    public void startFaceRecognition(View view) {
        if (requestPermissionIfNeeded(Permission.FACE_RECOGNITION, REQUEST_CODE_FACE_START)) {
            return;
        }
        robot.startFaceRecognition();
    }

    public void stopFaceRecognition(View view) {
        robot.stopFaceRecognition();
    }

    public void setGoToSpeed(View view) {
        if (requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
            return;
        }
        List<String> speedLevels = new ArrayList<>();
        speedLevels.add(SpeedLevel.HIGH.getValue());
        speedLevels.add(SpeedLevel.MEDIUM.getValue());
        speedLevels.add(SpeedLevel.SLOW.getValue());
        final CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_selectable_list_item, speedLevels);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select Go To Speed Level")
                .setAdapter(adapter, null)
                .create();
        dialog.getListView().setOnItemClickListener((parent, view1, position, id) -> {
            robot.setGoToSpeed(SpeedLevel.valueToEnum(Objects.requireNonNull(adapter.getItem(position))));
            Toast.makeText(MainActivity.this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        dialog.show();
    }

    public void setGoToSafety(View view) {
        if (requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
            return;
        }
        List<String> safetyLevel = new ArrayList<>();
        safetyLevel.add(SafetyLevel.HIGH.getValue());
        safetyLevel.add(SafetyLevel.MEDIUM.getValue());
        final CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_selectable_list_item, safetyLevel);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select Go To Safety Level")
                .setAdapter(adapter, null)
                .create();
        dialog.getListView().setOnItemClickListener((parent, view1, position, id) -> {
            robot.setNavigationSafety(SafetyLevel.valueToEnum(Objects.requireNonNull(adapter.getItem(position))));
            Toast.makeText(MainActivity.this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        dialog.show();
    }

    public void toggleTopBadge(View view) {
        if (requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
            return;
        }
        robot.setTopBadgeEnabled(!robot.isTopBadgeEnabled());
    }

    public void toggleDetectionMode(View view) {
        if (requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
            return;
        }
        robot.setDetectionModeOn(!robot.isDetectionModeOn());
    }

    public void toggleAutoReturn(View view) {
        if (requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
            return;
        }
        robot.setAutoReturnOn(!robot.isAutoReturnOn());
    }

    public void toggleTrackUser(View view) {
        if (requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
            return;
        }
        robot.setTrackUserOn(!robot.isTrackUserOn());
    }

    public void getVolume(View view) {
        printLog("Current volume is: " + robot.getVolume());
    }

    public void setVolume(View view) {
        if (requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
            return;
        }
        List<String> volumeList = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        final CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_selectable_list_item, volumeList);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Set Volume")
                .setAdapter(adapter, null)
                .create();
        dialog.getListView().setOnItemClickListener((parent, view1, position, id) -> {
            robot.setVolume(Integer.parseInt(Objects.requireNonNull(adapter.getItem(position))));
            Toast.makeText(MainActivity.this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        dialog.show();
    }

    public void requestToBeKioskApp(View view) {
        if (robot.isSelectedKioskApp()) {
            Toast.makeText(this, this.getString(R.string.app_name) + " was the selected Kiosk App.", Toast.LENGTH_SHORT).show();
            return;
        }
        robot.requestToBeKioskApp();
    }

    @SuppressLint("DefaultLocale")
    public void startDetectionModeWithDistance(View view) {
        hideKeyboard();
        if (requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_START_DETECTION_WITH_DISTANCE)) {
            return;
        }
        startDetectionWithDistance();
    }

    private void startDetectionWithDistance() {
        String distanceStr = etDistance.getText().toString();
        if (distanceStr.isEmpty()) distanceStr = "0";
        try {
            float distance = Float.parseFloat(distanceStr);
            robot.setDetectionModeOn(true, distance);
            printLog("Start detection mode with distance: " + distance);
        } catch (Exception e) {
            printLog("startDetectionModeWithDistance", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDistanceToLocationChanged(@NotNull Map<String, Float> distances) {
        for (String location : distances.keySet()) {
            printLog("onDistanceToLocation", "location:" + location + ", distance:" + distances.get(location));
        }
    }

    @Override
    public void onCurrentPositionChanged(@NotNull Position position) {
        printLog("onCurrentPosition", position.toString());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onSequencePlayStatusChanged(int status) {
        printLog(String.format("onSequencePlayStatus status:%d", status));
        if (status == OnSequencePlayStatusChangedListener.ERROR
                || status == OnSequencePlayStatusChangedListener.IDLE) {
            robot.showTopBar();
        }
    }

    @Override
    public void onRobotLifted(boolean isLifted, @NotNull String reason) {
        printLog("onRobotLifted: isLifted: " + isLifted + ", reason: " + reason);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }

    @CheckResult
    private boolean requestPermissionIfNeeded(Permission permission, int requestCode) {
        if (robot.checkSelfPermission(permission) == Permission.GRANTED) {
            return false;
        }
        robot.requestPermissions(Collections.singletonList(permission), requestCode);
        return true;
    }

    @Override
    public void onDetectionDataChanged(@NotNull DetectionData detectionData) {
        printLog("onDetectionDataChanged", detectionData.toString());
    }

    @Override
    public void onUserInteraction(boolean isInteracting) {
        printLog("onUserInteraction", "isInteracting:" + isInteracting);
    }

    public void getAllSequences(View view) {
        if (requestPermissionIfNeeded(Permission.SEQUENCE, REQUEST_CODE_SEQUENCE_FETCH_ALL)) {
            return;
        }
        getAllSequences();
    }

    private volatile List<SequenceModel> allSequences;

    private void getAllSequences() {
        new Thread(() -> {
            allSequences = robot.getAllSequences();
            runOnUiThread(() -> {
                for (SequenceModel sequenceModel : allSequences) {
                    if (sequenceModel == null) {
                        continue;
                    }
                    printLog(sequenceModel.toString());
                }
            });
        }).start();
    }

    public void playFirstSequence(View view) {
        if (requestPermissionIfNeeded(Permission.SEQUENCE, REQUEST_CODE_SEQUENCE_PLAY)) {
            return;
        }
        playFirstSequence(true);
    }

    public void playFirstSequenceWithoutPlayer(View view) {
        if (requestPermissionIfNeeded(Permission.SEQUENCE, REQUEST_CODE_SEQUENCE_PLAY_WITHOUT_PLAYER)) {
            return;
        }
        playFirstSequence(false);
    }

    private void playFirstSequence(boolean withPlayer) {
        if (allSequences != null && !allSequences.isEmpty()) {
            robot.playSequence(allSequences.get(0).getId(), withPlayer);
        }
    }

    public void getMap(View view) {
        if (requestPermissionIfNeeded(Permission.MAP, REQUEST_CODE_MAP)) {
            return;
        }
        getMap();
    }

    private void getMap() {
        startActivity(new Intent(this, MapActivity.class));
    }

    @Override
    public void onFaceRecognized(@NotNull List<ContactModel> contactModelList) {
        for (ContactModel contactModel : contactModelList) {
            printLog("onFaceRecognized", contactModel.toString());
            recog=contactModel.getFirstName();
            showFaceRecognitionImage(contactModel.getImageKey());
        }
    }

    private void showFaceRecognitionImage(String mediaKey) {
        if (mediaKey.isEmpty()) {
            ivFace.setImageResource(R.drawable.app_icon);
            return;
        }
        executorService.execute(() -> {
            InputStream inputStream = robot.getInputStreamByMediaKey(ContentType.FACE_RECOGNITION_IMAGE, mediaKey);
            if (inputStream == null) {
                return;
            }
            runOnUiThread(() -> {
                ivFace.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void printLog(String msg) {
        printLog("", msg);
    }

    private void printLog(String tag, String msg) {
        Log.d(tag, msg);
        tvLog.setGravity(Gravity.BOTTOM);
        tvLog.append(String.format("%s %s\n", "· ", msg));
    }

    public void btnClearLog(View view) {
        tvLog.setText("");
    }

    public void startNlu(View view) {
        robot.startDefaultNlu(etNlu.getText().toString());  // this is startNlu original code 나머지는 다 수정 한것




        //



    }

    @Override
    public void onSdkError(@NotNull SdkException sdkException) {
        printLog("onSdkError: " + sdkException.toString());
    }

    public void getAllContacts(View view) {
        List<UserInfo> allContacts = robot.getAllContact();
        for (UserInfo userInfo : allContacts) {
            printLog("UserInfo: " + userInfo.toString());
        }
    }

    public void goToPosition(View view) {
        try {
            float x = Float.parseFloat(etX.getText().toString());
            float y = Float.parseFloat(etY.getText().toString());
            float yaw = Float.parseFloat(etYaw.getText().toString());
            robot.goToPosition(new Position(x, y, yaw, 0));
        } catch (Exception e) {
            e.printStackTrace();
            printLog(e.getMessage());
        }
    }

    @Override
    public void onConversationStatusChanged(int status, @NotNull String text) {
        printLog("Conversation", "status=" + status + ", text=" + text);
    }

    @Override
    public void onTtsVisualizerWaveFormDataChanged(@NotNull byte[] waveForm) {
        ttsVisualizerView.updateVisualizer(waveForm);
    }

    @Override
    public void onTtsVisualizerFftDataChanged(@NotNull byte[] fft) {
        Log.d("TtsVisualizer", Arrays.toString(fft));
//        ttsVisualizerView.updateVisualizer(fft);
    }

    public void startTelepresenceToCenter(View view) {
        UserInfo target = robot.getAdminInfo();
        if (target == null) {
            printLog("target is null.");
            return;
        }
        robot.startTelepresence(target.getName(), target.getUserId(), Platform.TEMI_CENTER);
    }

    public void startPage(View view) {
        List<String> systemPages = new ArrayList<>();
        for (Page page : Page.values()) {
            systemPages.add(page.toString());
        }
        final CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_selectable_list_item, systemPages);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select System Page")
                .setAdapter(adapter, null)
                .create();
        dialog.getListView().setOnItemClickListener((parent, view1, position, id) -> {
            robot.startPage(Page.values()[position]);
            dialog.dismiss();
        });
        dialog.show();
    }

    public void restartTemi(View view) {
        robot.restart();
    }

    public void getMembersStatus(View view) {
        List<MemberStatusModel> memberStatusModels = robot.getMembersStatus();
        for (MemberStatusModel memberStatusModel : memberStatusModels) {
            printLog(memberStatusModel.toString());
        }
    }

    public void repose(View view) {
        robot.repose();
    }

    @Override
    public void onReposeStatusChanged(int status, @NotNull String description) {
        printLog("repose status: " + status + ", description: " + description);
    }
}
