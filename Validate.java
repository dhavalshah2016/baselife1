package com.example.baselife.util;

/**
 * Created by dhaval on 15/8/16.
 */
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import android.widget.EditText;

public class Validate {

    public static boolean Check_Lengh(EditText editText) {

        boolean validated = false;

        String text = editText.getText().toString().trim();

        if (text.length() == 0) {
            editText.setText(text);
            validated = true;
        }

        return validated;
    }

    public static boolean isEqual(EditText edittext1, EditText edittext2) {

        boolean validated = false;

        String text1 = edittext1.getText().toString().trim();
        String text2 = edittext2.getText().toString().trim();

        if (text1.equalsIgnoreCase(text2)) {
            validated = true;
        }

        return validated;
    }

    public static boolean checkEmail(EditText inputMail) {
        boolean validated;

        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

        Matcher m = p.matcher(inputMail.getText());

        boolean matchFound = m.matches();

        StringTokenizer st = new StringTokenizer(inputMail.toString(), ".");
        String lastToken = null;
        while (st.hasMoreTokens()) {
            lastToken = st.nextToken();
        }

        if (matchFound && lastToken.length() >= 2
                && inputMail.length() - 1 != lastToken.length()) {

            // validate the country code
            validated = true;
        } else
            validated = false;

        Log.v("log_tag", "on " + validated);

        return validated;
    }

}
