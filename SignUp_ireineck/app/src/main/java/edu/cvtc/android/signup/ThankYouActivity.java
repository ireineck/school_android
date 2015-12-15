package edu.cvtc.android.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ThankYouActivity extends AppCompatActivity {
    private TextView responseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_thankyou);

        responseTextView = (TextView) findViewById(R.id.responseTextView);
        final Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String email = intent.getStringExtra("email");

        if (null != name && !name.isEmpty()) {
            responseTextView.setText("Thank you " + name + " you can expect an email from us soon to your " +
                    "email address:  " + email + ".");
        } else {
            if (name.isEmpty()) {
                responseTextView.setText("Oops! Somehow we got here without a name.");
            } else if (email.isEmpty()) {
                responseTextView.setText("Oops! Somehow we got here without a email.");
            }
        }
    }
}


