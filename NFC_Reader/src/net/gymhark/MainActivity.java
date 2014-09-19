package net.gymhark;

import java.io.IOException;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    Tag mTag;
    public TextView mTextView;
    private NfcAdapter mNfcAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
	        	
	        }
	    });    
	    handleIntent(getIntent());
	}

	
	@Override
    protected void onResume() {
        super.onResume();
        //It's important, that the activity is in the foreground (resumed). Otherwise
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
		System.out.println("ja nfc ist da!!!! heeeeeeeeeeeeeeeeeeee");
			//handleIntent(intent);
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String nfcMessage = "hallo du penis!";
	        Toast.makeText(this, this.getString(R.string.ok_detection), Toast.LENGTH_LONG ).show();
	        writeTag(this, tag, nfcMessage);
	}
	
	private void handleIntent(Intent intent) { 
		
		mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		  String action = intent.getAction();
		    if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
		    	System.out.println("ACTION_NDEF_DISCOVERED");
		        String type = intent.getType();
		        if (MIME_TEXT_PLAIN.equals(type)) {
		 
		            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		            new NdefReaderTask();
					NdefReaderTask.setContext(this);
		            new NdefReaderTask().execute(tag);
		             
		        } else {
		        	System.out.println("wrong mime type" + type);
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
		            	new NdefReaderTask();
						NdefReaderTask.setContext(this);
		                new NdefReaderTask().execute(tag);
		                break;
		            }
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
	    
	    public static boolean writeTag(Context context, Tag tag, String data) {
	        // Record to launch Play Store if app is not installed
	        NdefRecord appRecord = NdefRecord.createApplicationRecord(context.getPackageName());

	        // Record with actual data we care about
	        NdefRecord relayRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
	                                                new String("application/" + context.getPackageName())
	                                                .getBytes(Charset.forName("US-ASCII")),
	                                                null, data.getBytes());

	        // Complete NDEF message with both records
	        NdefMessage message = new NdefMessage(new NdefRecord[] {relayRecord, appRecord});

	        try {
	            // If the tag is already formatted, just write the message to it
	            Ndef ndef = Ndef.get(tag);
	            if(ndef != null) {
	                ndef.connect();

	                // Make sure the tag is writable
	                if(!ndef.isWritable()) {	           
	                    return false;
	                }

	                // Check if there's enough space on the tag for the message
	                

	                try {
	                    // Write the data to the tag
	                    ndef.writeNdefMessage(message);
	                    return true;
	                } catch (TagLostException tle) {
	               
	                    return false;
	                } catch (IOException ioe) {
	                    
	                    return false;
	                } catch (FormatException fe) {
	                  
	                    return false;
	                }
	            // If the tag is not formatted, format it with the message
	            } else {
	                NdefFormatable format = NdefFormatable.get(tag);
	                if(format != null) {
	                    try {
	                        format.connect();
	                        format.format(message);
	                        return true;
	                    } catch (TagLostException tle) {
	                        return false;
	                    } catch (IOException ioe) {
	                        return false;
	                    } catch (FormatException fe) {
	                        return false;
	                    }
	                } else {
	                    return false;
	                }
	            }
	        } catch(Exception e) {
	        }

	        return false;
	    }

}
