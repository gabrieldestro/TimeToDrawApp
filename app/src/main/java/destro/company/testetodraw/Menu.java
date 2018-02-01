/* Time to Draw (ou qualquer que seja o nome) v1.0
*  Gabriel Augusto Destro
*  07/2017 */

package destro.company.testetodraw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    Spinner spnTime;
    Button btnBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        spnTime = (Spinner) findViewById(R.id.SpnTime);
        btnBegin = (Button) findViewById(R.id.btnBegin);

        btnBegin.setOnClickListener(this);

        configuraSpinner ();
    }

    /* Called when a button is clicked */
    public void onClick (View view) {
        switch (view.getId()) {
            /* Starts the activity responsable for showing the images randomly
             * Passes a String from spnTome selected line as parameter */
            case R.id.btnBegin:
                Intent begin = new Intent(this, Main.class);
                String selectedTime = spnTime.getSelectedItem().toString();
                begin.putExtra("selectedTime", selectedTime);
                startActivity(begin);
                break;
        }
    }

    /* Configure and activate SpnTime */
    public void configuraSpinner () {

        String[] times = new String[] {
                "30 seconds",
                "1 minute",
                "3 minutes",
                "5 minutes",
                "10 minutes"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTime.setAdapter(adapter);
    }
}
