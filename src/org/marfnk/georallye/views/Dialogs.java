package org.marfnk.georallye.views;

import org.marfnk.georallye.R;
import org.marfnk.georallye.data.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class Dialogs {

    public static void showErrorDialog(Activity activity, int errorStringId, int buttonStringId) {

        String errorMessage = activity.getResources().getString(errorStringId);
        String buttonText = activity.getResources().getString(buttonStringId);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(errorMessage).setTitle("Fehler!").setPositiveButton(buttonText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showCompleteMessageDialog(Activity activity, String title, String buttonText, final Runnable next,
            String... messages) {
        if (messages == null || (messages.length == 1 && messages[0] == null)) {
            //nothing to show!
            return;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.quest_dialog, null);
            
            builder.setView(view).setPositiveButton(buttonText, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (next != null) {
                        next.run();
                    }
                }
            });
    
            String message = "";
            for (int i = 1; i <= messages.length; i++) {
                message += messages[i - 1] + "\n\n";
            }
            
            TextView tv = (TextView) view.findViewById(R.id.message);
            tv.setText(message);
            
            tv.setTypeface(Constants.TEXT_TYPEFACE);
            
            final ScrollView scrollWrapper = (ScrollView) view.findViewById(R.id.scroller);
            scrollWrapper.post(new Runnable() {
                @Override
                public void run() {
                    scrollWrapper.fullScroll(View.FOCUS_DOWN);
                }
            });
            
            TextView titleText = (TextView) view.findViewById(R.id.dialogheaderText);
            titleText.setText(title);
            titleText.setTypeface(Constants.TEXT_TYPEFACE);
            
            AlertDialog dialog = builder.create();
            
            builder.setCancelable(false);
            
            dialog.show();
        }
    }
}
