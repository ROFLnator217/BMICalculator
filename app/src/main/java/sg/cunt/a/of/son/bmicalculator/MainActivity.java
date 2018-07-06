package sg.cunt.a.of.son.bmicalculator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etWeight;
    EditText etHeight;

    Button btnCal;
    Button btnReset;

    TextView tvDate;
    TextView tvBMI;
    TextView tvStatement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);

        btnCal = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);

        tvDate = findViewById(R.id.tvDate);
        tvBMI = findViewById(R.id.tvBMI);
        tvStatement = findViewById(R.id.tvStatement);

        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String w = etWeight.getText().toString().trim();
                String h = etHeight.getText().toString().trim();

                if ((w.equalsIgnoreCase("")) || (h.equalsIgnoreCase(""))) {
                    Toast.makeText(MainActivity.this,
                            "Weight or Height cannot be empty!", Toast.LENGTH_LONG).show();
                }
                else {
                    Float weight = Float.parseFloat(w);
                    Float height = Float.parseFloat(h);

                    if (weight < 2 || weight > 400) {
                        Toast.makeText(MainActivity.this,
                                "Weight limit between 2 and 400 kilograms!", Toast.LENGTH_LONG).show();
                    }
                    else if (height < 0.4 || height > 3) {
                        Toast.makeText(MainActivity.this,
                                "Height limit between 0.4 and 3 metres!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Float bmi = weight / (height * height);

                        String result = String.format("%s%.2f",getResources().getString(R.string.lastbmi),bmi);
                        Calendar now = Calendar.getInstance();
                        String datetime = String.format("%s%d/%d/%d %d:%02d",
                                getResources().getString(R.string.lastdate),
                                now.get(Calendar.DAY_OF_MONTH),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.YEAR),
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE));

                        String state = "";
                        if (bmi < 18.5) {
                            state = "You are underweight!";
                        }
                        else if (bmi < 25) {
                            state = "Your BMI is normal.";
                        }
                        else if (bmi < 30) {
                            state = "You are overweight!";
                        }
                        else if (bmi >= 30) {
                            state = "You are obese!";
                        }
                        else {
                            state = "Unable to determine your BMI.";
                        }

                        tvDate.setText(datetime);
                        tvBMI.setText(result);
                        tvStatement.setText(state);
                    }
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etWeight.setText("");
                etHeight.setText("");
                tvDate.setText(getResources().getString(R.string.lastdate));
                tvBMI.setText(getResources().getString(R.string.lastbmi));
                tvStatement.setText("");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        String date = tvDate.getText().toString().trim();
        String bmi = tvBMI.getText().toString().trim();
        String state = tvStatement.getText().toString().trim();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();

        prefEdit.putString("date",date);
        prefEdit.putString("bmi",bmi);
        prefEdit.putString("state",state);

        prefEdit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String date = prefs.getString("date",getResources().getString(R.string.lastdate));
        String bmi = prefs.getString("bmi",getResources().getString(R.string.lastbmi));
        String state = prefs.getString("state","");

        tvDate.setText(date);
        tvBMI.setText(bmi);
        tvStatement.setText(state);
    }
}
