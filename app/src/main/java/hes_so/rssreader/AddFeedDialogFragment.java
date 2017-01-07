package hes_so.rssreader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Project: RSSReader
 * Author: Armand Delessert, Dessingy Edward
 * Date: 05.01.2017
 */

public class AddFeedDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Création du la boite de dialogue d'ajout d'un flux RSS
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.add_feed_view, null));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                addNewFeed(((EditText) findViewById(R.id.newFeedView_FeedUrl_EditText)).getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AddFeedDialogFragment.this.getDialog().cancel();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
