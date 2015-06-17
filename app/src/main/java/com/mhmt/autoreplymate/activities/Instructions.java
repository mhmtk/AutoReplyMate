package com.mhmt.autoreplymate.activities;

import com.mhmt.autoreplymate.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.widget.TextView;

/**
 * 
 * @author Mehmet Kologlu
 * @version May 29, 2015
 * 
 */
public class Instructions extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructions);
		
		TextView instructionsTextView = (TextView)findViewById(R.id.instructions_instructions);
		instructionsTextView.setText(Html.fromHtml(getString(R.string.instructions)));
	}
}
