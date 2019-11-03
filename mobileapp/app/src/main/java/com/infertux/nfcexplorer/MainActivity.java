package com.infertux.nfcexplorer;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.ImageRequest;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class MainActivity extends Activity {
    static private ArrayList<TagWrapper> tags = new ArrayList<TagWrapper>();
    static private int currentTagIndex = -1;

    private NfcAdapter adapter = null;
    private PendingIntent pendingIntent = null;

    private TextView currentTagView, mTextViewResult;
    private ExpandableListView expandableListView;

    private float touchDownX, touchUpX;

//    private RequestType requestType = RequestType.GET;

//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mTextViewResult = findViewById(R.id.text_view_result);
////        this.viewSwipeRefresh.setOnRefreshListener(this);
////        this.viewSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.hk));
////        this.mclvStory = (MultiColumnListView) findViewById(R.id.mclv_story);
////        this.mAdapter = new StoryAdapter(this, this.mDataList);
////        this.mclvStory.setAdapter(this.mAdapter);
//        requestJson();
//    }
//
//    private void requestJson() {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = (new StringRequest(Request.Method.GET, "https://reqres.in/api/users?page=2", new Response.Listener<String>() {
//            public void onResponse(String response) {
//                try {
////                    List<Story> stories = Story.parseStory(response);
////                    if (stories != null && stories.size() > 0) {
//                        mTextViewResult.setText(response);
////                        stories.clear();
////                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Log.i(TAG,"SUCCESS");
////                MainActivity.this.setRefresh(false);
//            }
//        }, new Response.ErrorListener() {
//            public void onErrorResponse(VolleyError error) {
//                    Log.i(TAG,"FAILED");
////                Helper.showToast(error.toString());
////                MainActivity.this.setRefresh(false);
//            }
//        }));
//
//        queue.add(stringRequest);
////        setRefresh(true);
//    }

    @Override
    public void onCreate(final Bundle savedState) {
        super.onCreate(savedState);

//        RequestQueue queue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);

        currentTagView = (TextView) findViewById(R.id.currentTagView);
        mTextViewResult = findViewById(R.id.text_view_result);
        currentTagView.setText("Loading...");

//        String url = "https://reqres.in/api/users?page=2";
//
        final String TAG = MainActivity.class.getCanonicalName();
//
//        try {
//            // Request a string response from the provided URL.
//            JsonObjectRequest stringRequest = new JsonObjectRequest(requestType.getMethod(), url,null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            // Display the first 500 characters of the response string.
////                            Log.i("response", response);
//                            mTextViewResult.setText("Response is: " + response.toString());
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            mTextViewResult.setText("That didn't work!");
//                        }
//                    });
//            Log.i("response", String.valueOf(stringRequest));
//
//            stringRequest.setShouldCache(false);
//
//            queue.add(stringRequest);
//
//        } catch (Exception e) {
//            Log.e(TAG, "Something went horribly wrong.", e);
//            mTextViewResult.setText("That didn't work!");
//        }

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float swipeThreshold = 150;

                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        touchDownX = event.getX();
                        break;

                    case MotionEvent.ACTION_UP:
                        touchUpX = event.getX();
                        final float deltaX = touchUpX - touchDownX;

                        if (deltaX > swipeThreshold) {
                            showPreviousTag();
                        } else if (deltaX < -swipeThreshold) {
                            showNextTag();
                        }

                        break;
                }

                return false;
            }
        });

        adapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!adapter.isEnabled()) {
            Utils.showNfcSettingsDialog(this);
            return;
        }

        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

            currentTagView.setText("Welcome to VResume: Please scan a tag!");
        }

        showTag();

        adapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {

        Log.d("onNewIntent", "Discovered tag with intent " + intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagId = Utils.bytesToHex(tag.getId());
        TagWrapper tagWrapper = new TagWrapper(tagId);

        ArrayList<String> misc = new ArrayList<String>();
        misc.add("scanned at: " + Utils.now());

        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        String tagData = "";

        if (rawMsgs != null) {

            NdefMessage msg = (NdefMessage) rawMsgs[0];
            NdefRecord cardRecord = msg.getRecords()[0];
            try {
                tagData = readRecord(cardRecord.getPayload());
            } catch (UnsupportedEncodingException e) {
                Log.e("TagScan", e.getMessage());
                return;
            }
        }

        misc.add("tag data: " + tagData);
        tagWrapper.techList.put("Misc", misc);

        OkHttpClient client = new OkHttpClient();

        String url = "35.236.246.192/associate";

//        Request request = new Request.Builder().url(url).build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    final String myResponse = response.body().string();
//
//                    MainActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mTextViewResult.setText(myResponse);
//                        }
//                    });
//                }
//
//            }
//        });



        RequestBody formBody = new FormBody.Builder()
                .add("studentID", "800909222")
                .add("employerID", "27i4ZNzITdfpzOrXAnv40uBaigI2")
                .add("firstName", "Roldan")
                .add("lastName", "Biete")
                .add("tags", "['Java', 'Git']")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();

            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String tech : tag.getTechList()) {
            tech = tech.replace("android.nfc.tech.", "");
            List<String> info = getTagInfo(tag, tech);
            tagWrapper.techList.put("Technology: " + tech, info);
        }

        if (tags.size() == 1) {
            Toast.makeText(this, "Swipe right to see previous tags", Toast.LENGTH_LONG).show();
        }

        tags.add(tagWrapper);
        currentTagIndex = tags.size() - 1;
        showTag();
    }

    String readRecord(byte[] payload) throws UnsupportedEncodingException {
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        int languageCodeLength = payload[0] & 63;

        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }


    private void showPreviousTag() {
        if (--currentTagIndex < 0) currentTagIndex = tags.size() - 1;

        showTag();
    }

    private void showNextTag() {
        if (++currentTagIndex >= tags.size()) currentTagIndex = 0;

        showTag();
    }

    private void showTag() {

        if (tags.size() == 0) return;

        final TagWrapper tagWrapper = tags.get(currentTagIndex);
        final TagTechList techList = tagWrapper.techList;
        final ArrayList<String> expandableListTitle = new ArrayList<String>(techList.keySet());

        expandableListView.setAdapter(
                new CustomExpandableListAdapter(this, expandableListTitle, techList));

        final int count = expandableListView.getCount();
        for (int i = 0; i < count; i++) expandableListView.expandGroup(i);

        currentTagView.setText("Tag " + tagWrapper.getId() +
                " (" + (currentTagIndex+1) + "/" + tags.size() + ")");
    }

    private final List<String> getTagInfo(final Tag tag, final String tech) {
        List<String> info = new ArrayList<String>();

        switch (tech) {
            case "NfcA":
                info.add("aka ISO 14443-3A");

                NfcA nfcATag = NfcA.get(tag);
                info.add("atqa: " + Utils.bytesToHexAndString(nfcATag.getAtqa()));
                info.add("sak: " + nfcATag.getSak());
                info.add("maxTransceiveLength: " + nfcATag.getMaxTransceiveLength());
                break;

            case "NfcF":
                info.add("aka JIS 6319-4");

                NfcF nfcFTag = NfcF.get(tag);
                info.add("manufacturer: " + Utils.bytesToHex(nfcFTag.getManufacturer()));
                info.add("systemCode: " + Utils.bytesToHex(nfcFTag.getSystemCode()));
                info.add("maxTransceiveLength: " + nfcFTag.getMaxTransceiveLength());
                break;

            case "NfcV":
                info.add("aka ISO 15693");
                
                NfcV nfcVTag = NfcV.get(tag);
                info.add("dsfId: " + nfcVTag.getDsfId());
                info.add("responseFlags: " + nfcVTag.getResponseFlags());
                info.add("maxTransceiveLength: " + nfcVTag.getMaxTransceiveLength());
                break;

            case "Ndef":
                Ndef ndefTag = Ndef.get(tag);
                NdefMessage ndefMessage = null;

                try {
                    ndefTag.connect();
                    ndefMessage = ndefTag.getNdefMessage();
                    ndefTag.close();

                    for (final NdefRecord record : ndefMessage.getRecords()) {
                        final String id = record.getId().length == 0 ? "null" : Utils.bytesToHex(record.getId());
                        info.add("record[" + id + "].tnf: " + record.getTnf());
                        info.add("record[" + id + "].type: " + Utils.bytesToHexAndString(record.getType()));
                        info.add("record[" + id + "].payload: " + Utils.bytesToHexAndString(record.getPayload()));
                    }

                    info.add("messageSize: " + ndefMessage.getByteArrayLength());

                } catch (final Exception e) {
                    e.printStackTrace();
                    info.add("error reading message: " + e.toString());
                }

                HashMap<String, String> typeMap = new HashMap<String, String>();
                typeMap.put(Ndef.NFC_FORUM_TYPE_1, "typically Innovision Topaz");
                typeMap.put(Ndef.NFC_FORUM_TYPE_2, "typically NXP MIFARE Ultralight");
                typeMap.put(Ndef.NFC_FORUM_TYPE_3, "typically Sony Felica");
                typeMap.put(Ndef.NFC_FORUM_TYPE_4, "typically NXP MIFARE Desfire");

                String type = ndefTag.getType();
                if (typeMap.get(type) != null) {
                    type += " (" + typeMap.get(type) + ")";
                }
                info.add("type: " + type);

                info.add("canMakeReadOnly: " + ndefTag.canMakeReadOnly());
                info.add("isWritable: " + ndefTag.isWritable());
                info.add("maxSize: " + ndefTag.getMaxSize());
                break;

            case "NdefFormatable":
                info.add("nothing to read");

                break;

            case "MifareUltralight":
                MifareUltralight mifareUltralightTag = MifareUltralight.get(tag);
                info.add("type: " + mifareUltralightTag.getType());
                info.add("tiemout: " + mifareUltralightTag.getTimeout());
                info.add("maxTransceiveLength: " + mifareUltralightTag.getMaxTransceiveLength());
                break;

            case "IsoDep":
                info.add("aka ISO 14443-4");

                IsoDep isoDepTag = IsoDep.get(tag);
                info.add("historicalBytes: " + Utils.bytesToHexAndString(isoDepTag.getHistoricalBytes()));
                info.add("hiLayerResponse: " + Utils.bytesToHexAndString(isoDepTag.getHiLayerResponse()));
                info.add("timeout: " + isoDepTag.getTimeout());
                info.add("extendedLengthApduSupported: " + isoDepTag.isExtendedLengthApduSupported());
                info.add("maxTransceiveLength: " + isoDepTag.getMaxTransceiveLength());
                break;

            default:
                info.add("unknown tech!");
        }

        return info;
    }

//    private enum RequestType {
//        GET(Request.Method.GET),
//        PUT(Request.Method.PUT),
//        POST(Request.Method.POST),
//        DELETE(Request.Method.DELETE),
//        HEAD(Request.Method.HEAD),
//        PATCH(Request.Method.PATCH);
//
//        private int method;
//
//        RequestType(int method) {
//            this.method = method;
//        }
//
//        public int getMethod() {
//            return method;
//        }
//    }
}