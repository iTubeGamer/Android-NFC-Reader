package net.gymhark;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	NfcAdapter adapter;
	PendingIntent pendingIntent;
	IntentFilter writeTagFilters[];
	boolean writeMode;
	Tag mytag;
	Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ctx=this;
	    Button btnWrite = (Button) findViewById(R.id.button);
	    final TextView message = (TextView)findViewById(R.id.textView1);
	    btnWrite.setOnClickListener(new View.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) {
	        	System.out.println("ja nfc ist da!!!! heeeeeeeeeeeeeeeeeeee");
	            try {
	                if(mytag==null){
	                    Toast.makeText(ctx, ctx.getString(R.string.error_detected), Toast.LENGTH_LONG ).show();
	                }else{
	                    write(message.getText().toString(),mytag);
	                    Toast.makeText(ctx, ctx.getString(R.string.ok_writing), Toast.LENGTH_LONG ).show();
	                }
	            } catch (IOException e) {
	                Toast.makeText(ctx, ctx.getString(R.string.error_writing), Toast.LENGTH_LONG ).show();
	                e.printStackTrace();
	            } catch (FormatException e) {
	                Toast.makeText(ctx, ctx.getString(R.string.error_writing) , Toast.LENGTH_LONG ).show();
	                e.printStackTrace();
	            }
	        }
	    });

	    adapter = NfcAdapter.getDefaultAdapter(this);
	    pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	    IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
	    tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
	    writeTagFilters = new IntentFilter[] { tagDetected };
	}

	
	public void onResume(Intent intent) {
	    super.onResume();
	    Toast toast = Toast.makeText(this, "1111111111111111", Toast.LENGTH_LONG);
	    NdefMessage[] msgs;
	    if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	        if (rawMsgs != null) {
	            msgs = new NdefMessage[rawMsgs.length];
	            for (int i = 0; i < rawMsgs.length; i++) {
	                msgs[i] = (NdefMessage) rawMsgs[i];
	            }
	        }
	    }
	    //process the msgs array
	    //msgs[0].getRecords()[0].toString()
	    toast.show();
	    
	}
	
	
	NdefRecord uriRecord = new NdefRecord(
		    NdefRecord.TNF_ABSOLUTE_URI ,
		    "http://gymnasium-harksheide.de/start".getBytes(Charset.forName("US-ASCII")),
		    new byte[0], new byte[0]);
	
	public NdefRecord createTextRecord(String payload, Locale locale, boolean encodeInUtf8) {
	    byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
	    Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
	    byte[] textBytes = payload.getBytes(utfEncoding);
	    int utfBit = encodeInUtf8 ? 0 : (1 << 7);
	    char status = (char) (utfBit + langBytes.length);
	    byte[] data = new byte[1 + langBytes.length + textBytes.length];
	    data[0] = (byte) status;
	    System.arraycopy(langBytes, 0, data, 1, langBytes.length);
	    System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
	    NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
	    NdefRecord.RTD_TEXT, new byte[0], data);
	    return record;
	}
	
	
	
	@Override
	protected void onNewIntent(Intent intent){
		System.out.println("ja nfc ist da!!!! heeeeeeeeeeeeeeeeeeee");
	    //if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
	       // mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			handleIntent(intent);
	        Toast.makeText(this, this.getString(R.string.ok_detection) + mytag.toString(), Toast.LENGTH_LONG ).show();
	    //}
	}
	
	private void handleIntent(Intent intent) {
        
    }
	
	private NdefRecord createRecord(String text) throws UnsupportedEncodingException {

	    //create the message in according with the standard
	    String lang = "en";
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

	private void write(String text, Tag tag) throws IOException, FormatException {

	    NdefRecord[] records = { createRecord(text) };
	    NdefMessage message = new NdefMessage(records); 
	    Ndef ndef = Ndef.get(tag);
	    ndef.connect();
	    ndef.writeNdefMessage(message);
	    ndef.close();
	}
	
	
	private NdefRecord createRecord() throws UnsupportedEncodingException {
	    String text       = "Hello, World!";
	    String lang       = "en";
	    byte[] textBytes  = text.getBytes();
	    byte[] langBytes  = lang.getBytes("US-ASCII");
	    int    langLength = langBytes.length;
	    int    textLength = textBytes.length;
	    byte[] payload    = new byte[1 + langLength + textLength];

	    // set status byte (see NDEF spec for actual bits)
	    payload[0] = (byte) langLength;

	    // copy langbytes and textbytes into payload
	    System.arraycopy(langBytes, 0, payload, 1,              langLength);
	    System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

	    NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, 
	                                       NdefRecord.RTD_TEXT, 
	                                       new byte[0], 
	                                       payload);

	    return record;
	}
	
	
	private void write(Tag tag) throws IOException, FormatException {
	    NdefRecord[] records = { createRecord() };
	    NdefMessage  message = new NdefMessage(records);

	    // Get an instance of Ndef for the tag.
	    Ndef ndef = Ndef.get(tag);

	    // Enable I/O
	    ndef.connect();

	    // Write the message
	    ndef.writeNdefMessage(message);

	    // Close the connection
	    ndef.close();
	    Toast toast = Toast.makeText(this, "Jo, hab was geschrieben!", Toast.LENGTH_LONG);
	    toast.show();
	}
	


}
