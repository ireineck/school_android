package edu.cvtc.android.jokelist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class JokeListActivity extends AppCompatActivity {

    private static final String TAG = "joke list";

    /** Contains the list Jokes the Activity will present to the user. */
    private ArrayList<Joke> m_arrJokeList = new ArrayList<>();

    /** LinearLayout used for maintaining a list of Views that each display Jokes. */
    private LinearLayout m_vwJokeLayout;

    /** EditText used for entering text for a new Joke to be added to m_arrJokeList. */
    private EditText m_vwJokeEditText;


    /** Button used for creating and adding a new Joke to m_arrJokeList using the
     * text entered in m_vwJokeEditText. */
    private Button m_vwJokeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLayout();

        String[] jokeList = this.getResources().getStringArray(R.array.jokeList);
        for (String joke : jokeList){
            addJoke(joke);
        }

        initAddJokeListeners();
    }

    /**
     * Method used to encapsulate the code that initializes and sets the Layout
     * for this Activity.
     */
    private void initLayout() {

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        LinearLayout addJokeLayout = new LinearLayout(this);
        addJokeLayout.setOrientation(LinearLayout.HORIZONTAL);

        m_vwJokeButton = new Button(this);
        m_vwJokeButton.setText("add Joke");

        addJokeLayout.addView(m_vwJokeButton);


        m_vwJokeEditText = new EditText(this);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        m_vwJokeEditText.setLayoutParams(params);
        m_vwJokeEditText.setHint("Enter joke here!");
        addJokeLayout.addView(m_vwJokeEditText);

        m_vwJokeLayout = new LinearLayout(this);
        m_vwJokeLayout.setOrientation(LinearLayout.VERTICAL);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(m_vwJokeLayout);

        container.addView(addJokeLayout);
        container.addView(scrollView);

        setContentView(container);
    }

    /**
     * Method used to encapsulate the code that initializes and sets the Event
     * Listeners which will respond to requests to "Add" a new Joke to the
     * list.
     */
    private void initAddJokeListeners() {

        m_vwJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJokeFromEditText();
            }
        });

        m_vwJokeEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_NUMPAD_ENTER:
                        addJokeFromEditText();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void addJokeFromEditText(){
        String jokeText = m_vwJokeEditText.getText().toString().trim();
        addJoke(jokeText);
        m_vwJokeEditText.setText("");
    }

    /**
     * Method used for encapsulating the logic necessary to properly initialize
     * a new joke, add it to m_arrJokeList, and display it on screen.
     *
     * @param strJoke
     *            A string containing the text of the Joke to add.
     */
    private void addJoke(String strJoke) {

        if(null != strJoke && !strJoke.isEmpty()){

            Log.d(TAG,"Adding a new joke: " + strJoke);

            Joke joke = new Joke(strJoke);
            m_arrJokeList.add(joke);

            TextView textView =  new TextView(this);
            textView.setText(joke.getJoke());
            textView.setTextColor(Color.WHITE);

            if (m_arrJokeList.size() % 2 == 1){
                textView.setBackgroundColor(this.getResources().getColor(R.color.light));
            } else {
                textView.setBackgroundColor(this.getResources().getColor(R.color.dark));
            }

            m_vwJokeLayout.addView(textView);

        } else {
            Toast toast = Toast.makeText(getApplicationContext(),"Opps, something happened and we couldn't add your joke", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
