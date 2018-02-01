package destro.company.testetodraw;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class Main extends AppCompatActivity implements View.OnClickListener {

    int timeToDraw, oneSecInMillis = 1000;
    int progress = 0, numberOfImages = 10, id, idPreviousImage;
    int minRemaing = 0, secRemaning;
    boolean pause = false;

    String selectedTime, imgName, txtProgressBar;

    ImageView currentImage;
    TextView txtTimeReamaining;
    ProgressBar timeBar;
    CountDownTimer countDownTimer;
    Random random;
    Button btnStart, btnPause, btnNext, btnPrevious;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Locks in portrait mode in this activity
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Sets TimeToDraw with recovered spnTime data
        Intent begin = getIntent();
        selectedTime = begin.getStringExtra("selectedTime");
        setTimeToDraw();

        currentImage = (ImageView) findViewById (R.id.currentImage);
        txtTimeReamaining = (TextView) findViewById (R.id.myTextProgress);
        timeBar = (ProgressBar) findViewById (R.id.progressbar);

        btnNext = (Button) findViewById(R.id.btnNext);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnPrevious = (Button) findViewById(R.id.btnPrevious);

        btnNext.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);

        countDownTimer = new Timer(timeToDraw, oneSecInMillis);
        random = new Random();

        // disable previous and start buttons
        DisableBtnPrevious();
        DisableBtnStart();

        // loads first image
        generateImageName ();
        setImageId ();
        loadImage(id);
    }

    /* Generates a random number, then changes imgName based on that number so that the next image
     * loaded will be randomized */
    public void generateImageName () {
        int rndNumeber = random.nextInt(numberOfImages);
        imgName = "img_" + rndNumeber;
    }

    /* Generates an id for the next image to be loaded, also saves the id from the actual image */
    public void setImageId () {
        idPreviousImage = id;
        id = getResources().getIdentifier(imgName, "drawable", getPackageName());
    }

    /* Loads a new image on the screen
     * Parameter: (Integer) ID of the image to be loaded */
    public void loadImage(int ImageToBeLoadedID) {
        timeBar.setProgress(progress);
        currentImage.setImageResource(ImageToBeLoadedID);
        countDownTimer.start();
    }

    /* Called when a button is clicked */
    public void onClick (View view) {
        switch (view.getId()) {
            /* Next button is clicked */
            case R.id.btnNext:
                btnNext ();
                break;
            /* Start button is clicked */
            case R.id.btnStart:
                DisableBtnStart ();
                EnableBtnPause ();
                UnPause ();
                break;
            /* Pause button is clicked */
            case R.id.btnPause:
                DisableBtnPause ();
                EnableBtnStart ();
                Pause ();
                break;
            /*Previous button is clicked*/
            case R.id.btnPrevious:
                btnPrevious ();
                break;
        }
    }

    /* Back button is clicked */
    public void onBackPressed () {
        // Go back to the start screen
        Intent back = new Intent (this, Menu.class);
        startActivity(back);
    }

    // Converts the String received as parameter to a interger with the time in milliseconds (timeToDraw)
    public void setTimeToDraw () {
        switch (selectedTime) {
            case "30 seconds":
                timeToDraw = 30000;
                break;
            case "1 minute":
                timeToDraw = 60000;
                break;
            case "3 minutes":
                timeToDraw = 60000*3;
                break;
            case "5 minutes":
                timeToDraw = 60000*5;
                break;
            case "10 minutes":
                timeToDraw = 60000*10;
                break;

        }
    }

    /* Next button click */
    public void btnNext () {
        //reestart progress bar
        countDownTimer.cancel();
        reestartProgress ();
        UnPause ();
        DisableBtnStart();
        EnableBtnPause();
        // loads new image randomily
        generateImageName();
        setImageId();
        loadImage(id);
        EnableBtnPrevious ();
    }

    /* Previous button click */
    public void btnPrevious () {
        //reestart progress bar
        countDownTimer.cancel();
        reestartProgress ();
        UnPause ();
        DisableBtnStart();
        EnableBtnPause();
        // loads previous image showed
        loadImage(idPreviousImage);
        DisableBtnPrevious();
    }

    /* Calculates the time remaining to the image change occur
     * Parameter: (Integer) the percentage of progress of the timeBar */
    public void calculateRemainingTime (int progressPercentage) {
        int timePassed = progressPercentage * timeToDraw;
        int timeRemaining = timeToDraw - (timePassed / 100);
        secRemaning = timeRemaining / oneSecInMillis;

        // sets the minutes and seconds remaining
        minRemaing = secRemaning / 60;
        secRemaning = secRemaning % 60;


    }

    // sets progress to zero
    public void reestartProgress () {
        progress = 0;
    }
    // enable buttons
    public void EnableBtnPause () { btnPause.setEnabled(true); }
    public void EnableBtnPrevious () { btnPrevious.setEnabled(true); }
    public void EnableBtnStart () { btnStart.setEnabled(true); }
    // disable buttons
    public void DisableBtnPrevious () { btnPrevious.setEnabled(false); }
    public void DisableBtnPause () { btnPause.setEnabled(false); }
    public void DisableBtnStart () { btnStart.setEnabled(false); }
    // sets pause to true or false
    public void Pause () {
        pause = true;
    }
    public void UnPause () {
        pause = false;
    }

    // Timer class
    public class Timer extends CountDownTimer {

        public Timer (long timeToDraw, long oneSecInMillis) {
            super(timeToDraw, oneSecInMillis);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progressPercentage;

            // if the bar is not paused
            if (!pause) {
                // updates progress and remaining time
                progress++;
                progressPercentage = progress * 100 / (int) (timeToDraw / oneSecInMillis);
                calculateRemainingTime(progressPercentage);
                // update text
                txtProgressBar = "Time remaining: " + minRemaing + " min " + secRemaning + " s";
                txtTimeReamaining.setText(txtProgressBar);
                // advance bar
                timeBar.setProgress(progressPercentage);
            }
        }

        @Override
        public void onFinish() {
            // reestart timeBar
            reestartProgress ();
            timeBar.setProgress(progress);
            // Loads new random image
            generateImageName();
            setImageId();
            loadImage(id);
            EnableBtnPrevious ();
        }
    }
}
