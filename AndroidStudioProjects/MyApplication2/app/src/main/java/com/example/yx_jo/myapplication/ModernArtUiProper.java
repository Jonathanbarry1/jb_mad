package com.example.yx_jo.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ModernArtUiProper extends AppCompatActivity {

    public DialogFragment mDialog;
    public SeekBar colourbar;
    private TextView mtextView1;
    private TextView mtextView2;
    private TextView mtextView3;
    private TextView mtextView5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modern_art_ui_proper);

        mtextView1 = (TextView)findViewById(R.id.textView1);
        mtextView2 = (TextView)findViewById(R.id.textView2);
        mtextView3 = (TextView)findViewById(R.id.textView3);
        mtextView5 = (TextView)findViewById(R.id.textView5);



        colourbar = (SeekBar) findViewById(R.id.seekBar1);


        colourbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


                mtextView1.setBackgroundColor(Color.rgb(0, 123, 0+i)); //0, 123, 0
                mtextView2.setBackgroundColor(Color.rgb(135, 0 + i, 90)); //135, 0, 90
                mtextView3.setBackgroundColor(Color.rgb(155+i, 0, 0)); //155, 0, 0
                mtextView5.setBackgroundColor(Color.rgb(0,255-i, 0)); //0, 255, 0




            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.info:
                mDialog = AlertDialogFragment.newInstance();
                mDialog.show(getFragmentManager(), "Alert");
                return true;
            default:
                return false;
        }
    }

    public static class AlertDialogFragment extends DialogFragment {


        public static AlertDialogFragment newInstance() {
            return new AlertDialogFragment();
        }


        // Build AlertDialog using AlertDialog.Builder
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage("Inspired by the works of artists in Moma! " +
                            "Click below to learn more!")

                    //make this prettier!
                    .setPositiveButton("Visit Moma",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {

                                    Uri uri = Uri.parse("https://www.moma.org/");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);


                                }
                            })

                    // Set up No Button - do i need to have something specific here?
                    .setNegativeButton("Not Now",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                }
                            }).create();






        }
    }


}
