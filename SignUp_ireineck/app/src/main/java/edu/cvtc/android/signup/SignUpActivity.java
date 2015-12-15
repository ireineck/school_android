package edu.cvtc.android.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText;
    private EditText emailEditText;
    private Button submitButton;

    public static boolean isEmailValid(String email) {
        boolean isValidEmail = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email.trim();

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValidEmail = true;
        }
        return isValidEmail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup);
        setContentView(R.layout.layout_signup);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String name = nameEditText.getText().toString();
        final String email = emailEditText.getText().toString();

        if (name != null && !name.isEmpty() && email !=null && !email.isEmpty() && isEmailValid(email) ){
            final Intent intent = new Intent(this, ThankYouActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("email", email);

            startActivity(intent);
        } else {
            if (name.isEmpty()){
                Toast.makeText(this, "Please enter a name to continue...", Toast.LENGTH_SHORT).show();

            } else if (email.isEmpty()){
                Toast.makeText(this, "Please enter a email to continue...", Toast.LENGTH_SHORT).show();

            } else if (!isEmailValid(email)){
                Toast.makeText(this, "You didn't enter a valid email address. \n " +
                        "Please enter a valid email to continue.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Oops something went wrong...", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
