package net.gymhark;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class WriteToTag extends Activity {
	private NfcAdapter mNfcAdapter;
	public static final String MIME_TEXT_PLAIN = "text/plain";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_to_tag);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		Intent intent = getIntent();
		String schuelerid = intent.getStringExtra(MainActivity.SCHUELER_ID);
		String schuelername = intent.getStringExtra(MainActivity.SCHUELER_NAME);
		
		TextView tvId = (TextView) findViewById(R.id.tvId);
		TextView tvSchueler = (TextView) findViewById(R.id.tvSchueler);
		tvId.setText(schuelerid);
		tvSchueler.setText(schuelername);
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
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		TextView tvId = (TextView) findViewById(R.id.tvId);
		String nfcMessage = tvId.getText().toString();
        try {
			writeTag(this, tag, nfcMessage);
		} catch (IOException | FormatException e) {
			System.out.println("ioexception while writing");
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
                Toast.makeText(this, "Erfolg!", Toast.LENGTH_SHORT ).show();
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
