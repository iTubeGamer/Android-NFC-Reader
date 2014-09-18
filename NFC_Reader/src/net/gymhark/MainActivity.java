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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    Button btnWrite = (Button) findViewById(R.id.button);
	    final TextView message = (TextView)findViewById(R.id.textView1);
	    btnWrite.setOnClickListener(new View.OnClickListener()
	    {
	        @Override
	        public void onClick(View v) {
	        	
	        }
	    });    
	}

	
	@Override
	protected void onNewIntent(Intent intent){
		System.out.println("ja nfc ist da!!!! heeeeeeeeeeeeeeeeeeee");
		Toast.makeText(this, this.getString(R.string.ok_detection) + mytag.toString(), Toast.LENGTH_LONG ).show();
	    if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
	      
			handleIntent(intent);
	        Toast.makeText(this, this.getString(R.string.ok_detection) + mytag.toString(), Toast.LENGTH_LONG ).show();
	    }
	}
	
	private void handleIntent(Intent intent) {
        
    }
	
	

}
