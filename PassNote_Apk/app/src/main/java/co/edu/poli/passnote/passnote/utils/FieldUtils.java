package co.edu.poli.passnote.passnote.utils;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

public class FieldUtils {
    public static String getTextFromField(EditText editText) {
        String text = null;
        if (editText != null) {
            Editable editable = editText.getText();
            if (editable != null) {
                text = editable.toString();
            }
        }
        return StringUtils.trim(text);
    }

    public static String getTextFromField(View view) {
        String text = null;
        if (view instanceof EditText) {
            text = getTextFromField((EditText) view);
        }
        return StringUtils.trim(text);
    }
}
