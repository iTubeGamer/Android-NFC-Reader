package net.gymhark;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    Tag mTag;
    public TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private boolean readmode;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (readmode = true){
		} else if (readmode = false) {
		} else {
			readmode = true;
		};
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    Button btnWrite = (Button) findViewById(R.id.button);
	    mTextView = (TextView)findViewById(R.id.textView1);
	    mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
	    
	    if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
     
        if (!mNfcAdapter.isEnabled()) {
        	System.out.println("NFC disabled");
        	Toast.makeText(this, "Your NFC is disabled!", Toast.LENGTH_LONG).show();
        } else {
        	System.out.println("Ob das so richtig ist...");
        }
        
	    btnWrite.setOnClickListener(new View.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) {	
	        	TextView tvmode = (TextView) findViewById(R.id.tvMode);
	        	if (readmode ==(true)){
	        		readmode = false;
	        		tvmode.setText("Writing Mode");
	        	} else {
	        		readmode = true;
	        		tvmode.setText("Reading Mode");
	        	}
	        }
	    });    
	    
	    
	    handleIntent(getIntent());
	}

	
	@Override
    protected void onResume() {
        super.onResume();
        //It's important, that the activity is in the foreground (resumed).
        setupForegroundDispatch(this, mNfcAdapter);
    }
     
    @Override
    protected void onPause() {
        //  Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }
	
	
	@Override
	protected void onNewIntent(Intent intent){
			handleIntent(intent);
			
	}
	
	
	private void handleIntent(Intent intent) { 
		
		if (readmode ==(true)){
			Toast.makeText(this, "Reading...", Toast.LENGTH_SHORT ).show();
			System.out.println("reading...");
			mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			  String action = intent.getAction();
			    if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			    	System.out.println("ACTION_NDEF_DISCOVERED");
			        String type = intent.getType();
			        if (MIME_TEXT_PLAIN.equals(type)) {
			        	System.out.println("Nice: plain Text!");
			            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			            new NdefReaderTask();
						NdefReaderTask.setContext(this);
			            new NdefReaderTask().execute(tag);
			             
			        } else {
			        	Toast.makeText(this, "No Plain Text!", Toast.LENGTH_SHORT ).show();
			        	System.out.println("not plain text:" + type);
			            Log.d(TAG, "Wrong mime type: " + type);
			        }
			    } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			    	System.out.println("ACTION_TECH_DISCOVERED");
			        // In case we would still use the Tech Discovered Intent
			        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			        String[] techList = tag.getTechList();
			        String searchedTech = Ndef.class.getName();
			         
			        for (String tech : techList) {
			            if (searchedTech.equals(tech)) {
			            	String type = intent.getType();
			            	 if (MIME_TEXT_PLAIN.equals(type)) {
			            	new NdefReaderTask();
							NdefReaderTask.setContext(this);
			                new NdefReaderTask().execute(tag);
			                break;
			            	 }
			            }
			        }
			    } else {
			    	 System.out.println("Tag not recognized!");
			    	 Toast.makeText(this, "Tag not recognized!", Toast.LENGTH_SHORT ).show();
			    }
    	} else {
    		EditText etwrite = (EditText) findViewById(R.id.etTagWrite);
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String nfcMessage = etwrite.getText().toString();
	        try {
				writeTag(this, tag, nfcMessage);
			} catch (IOException | FormatException e) {
				System.out.println("ioexception while writing");
				e.printStackTrace();
			}
    	}
	
		
    }
	
	 public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
	        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
	        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	 
	        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
	 
	        IntentFilter[] filters = new IntentFilter[1];
	        String[][] techList = new String[][]{};
	 
	        // Notice that this is the same filter as in our manifest.
	        filters[0] = new IntentFilter();
	        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
	        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
	        try {
	            filters[0].addDataType(MIME_TEXT_PLAIN);
	        } catch (MalformedMimeTypeException e) {
	            throw new RuntimeException("Check your mime type.");
	        }
	         
	        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
	    }
	 
	    /**
	     * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
	     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
	     */
	 
	 
	    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
	        adapter.disableForegroundDispatch(activity);
	    }
	    
	    
	    private void writeTag(Context context, Tag tag, String data) throws IOException, FormatException {
	    	 NdefRecord[] records = { createRecord(data)};
	    	    NdefMessage message = new NdefMessage(records); 
	    	    Ndef ndef = Ndef.get(tag);
	    	    ndef.connect();
	    	    try {
                    // Write the data to the tag
                    ndef.writeNdefMessage(message);
                } catch (TagLostException tle) {
                	Toast.makeText(this, "exception tag lost", Toast.LENGTH_LONG ).show();
                } catch (IOException ioe) {
                	Toast.makeText(this, "IO excpetion", Toast.LENGTH_LONG ).show();
                } catch (FormatException fe) {
                	Toast.makeText(this, "format exception", Toast.LENGTH_LONG ).show();
                }
	    	    ndef.close();
	    }
	    
	    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {

	        //create the message in according with the standard
	        String lang = "de";
	        byte[] textBytes = text.getBytes();
	        byte[] langBytes = lang.getBytes("US-ASCII");
	        int langLength = langBytes.length;
	        int textLength = textBytes.length;

	        byte[] payload = new byte[1 + langLength + textLength];
	        payload[0] = (byte) langLength;

	        // copy langbytes and textbytes into payload
	        System.arraycopy(langBytes, 0, payload, 1, langLength);
	        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

	        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
	        return recordNFC;
	    }

}
