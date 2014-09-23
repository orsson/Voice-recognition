package com.test.voicerecognition;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.app.SearchManager;


public class Rozpoznawanie extends Activity{

	private static final int REQUEST_CODE = 1234;
	private ListView predictions;
	private TextToSpeech mTextToSpeech;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Button speakButton = (Button) findViewById(R.id.speakButton);
		predictions = (ListView) findViewById(R.id.list);

		
		PackageManager pm = getPackageManager();	
		List<ResolveInfo> activities = pm.queryIntentActivities(
				new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0)
		{
			speakButton.setEnabled(false);
			speakButton.setText("Recognizer not present");
		}


	}

	public void speakButtonClicked(View v)
	{
		startVoiceRecognitionActivity();
	}


	private void startVoiceRecognitionActivity()
	{
		Intent voiceRecognitionintent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		voiceRecognitionintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		voiceRecognitionintent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
		startActivityForResult(voiceRecognitionintent, REQUEST_CODE);
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_CODE  && resultCode == RESULT_OK)
		{
		
			ArrayList<String> listOfPredictions = data.getStringArrayListExtra(
					RecognizerIntent.EXTRA_RESULTS);
			predictions.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listOfPredictions));		
			
			predictions.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) 
				{
					 Object object = predictions.getItemAtPosition(position);
						
					 String q = object.toString();
					 Intent intent = new Intent(Intent.ACTION_WEB_SEARCH );
					 intent.putExtra(SearchManager.QUERY, q);
					 startActivity(intent);
					 
				}

			});
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onDestroy()
	{
	
		if (mTextToSpeech != null)
		{
			mTextToSpeech.stop();
			mTextToSpeech.shutdown();
		}
		super.onDestroy();
	}

}
