package net.gymhark;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTabChangeListener{
	public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    Tag mTag;
    long starttime;
    public TextView mTextView;
    private NfcAdapter mNfcAdapter;
    public final static String SCHUELER_ID = "net.gymhark.nfcreader.schuelerid";
    public final static String SCHUELER_NAME = "net.gymhark.nfcreader.schuelername";
    public final static String TAB_1_TAG = "Zuweisen";
    public final static String TAB_2_TAG = "Auslesen";
    public final static String TAB_3_TAG = "Verlauf";
    ArrayAdapter<String> tagVerlaufAdapter;
    List<String> TagList = new ArrayList<String>();
    private ScanTagDataSource datasource = new ScanTagDataSource(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TabHost tabHost=(TabHost)findViewById(android.R.id.tabhost);
		tabHost.setup();

		TabSpec spec1=tabHost.newTabSpec("Zuweisen");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("Zuweisen");

		TabSpec spec2=tabHost.newTabSpec("Auslesen");
		spec2.setIndicator("Auslesen");
		spec2.setContent(R.id.tab2);
		
		TabSpec spec3=tabHost.newTabSpec("Verlauf");
		spec3.setIndicator("Veraluf");
		spec3.setContent(R.id.tab3);
		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		
		
		tabHost.setOnTabChangedListener(this);
		
		
	    Button btnWrite = (Button) findViewById(R.id.btnZuweisen);
	    Button btclear = (Button) findViewById(R.id.btClear);
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
        
        btclear.setOnClickListener(new View.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) {
	        	//löschen...
	        }
	    });
        
	    btnWrite.setOnClickListener(new View.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) {
	        	sendMessage(v);
	        }
	    });    
	   // handleIntent(getIntent());
	}

	public void sendMessage(View view) {
    	EditText etId = (EditText) findViewById(R.id.etId);
    	EditText etSchueler = (EditText) findViewById(R.id.etSchueler);
		Intent intent = new Intent(this, WriteToTag.class);
    	intent.putExtra(SCHUELER_ID, etId.getText().toString());
    	intent.putExtra(SCHUELER_NAME, etSchueler.getText().toString());
    	startActivity(intent);
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
	    
	    public void makeToast (String message) {
	    	Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	    }

		@Override
		public void onTabChanged(String tabId) {
			  if(TAB_1_TAG.equals(tabId)) {
			    
			    }
			    if(TAB_2_TAG.equals(tabId)) {
			    	
			    }
			    if(TAB_3_TAG.equals(tabId)) {
			    	TagList.clear();
			    	try {
		       	 		datasource.open();
		       	 		TagList = datasource.getAllScanTags();
		       	 		datasource.close();
		        	} catch (Exception ex){
		        		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
		        	}
			    	
			    	tagVerlaufAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, TagList);
			    	ListView lvTags = (ListView) findViewById(R.id.lvTags);
			    	lvTags.setAdapter(tagVerlaufAdapter);
			    }
		}

}
