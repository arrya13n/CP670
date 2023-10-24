package com.example.androidassignments;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.androidassignments.databinding.ActivityTestToolbarBinding;

public class TestToolbar extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityTestToolbarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestToolbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_test_toolbar);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.lettericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sd = getResources().getString(R.string.lettericon);
                Snackbar.make(view, "You clicked on letter icon", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.lettericon)
                        .setAction("Action", null).show();
            }
        });

    }
    String new_text = "You selected option 1";
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.custom_layout, null);
        EditText editTextMessage = dialogView.findViewById(R.id.editTextMessage);

        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new_text = editTextMessage.getText().toString();
                        showSnackbar(new_text);
                    }

                    private void showSnackbar(String newText) {
                        Snackbar.make(findViewById(R.id.lettericon), newText,Snackbar.LENGTH_LONG);
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
}

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuitem) {
    int id = menuitem.getItemId();
    if(R.id.action1==id){
        Log.d("Toolbar","Option 1 is selected");
        Snackbar.make(findViewById(R.id.lettericon), new_text, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.lettericon)
                .setAction("Action",null).show();}
    else if (R.id.action2==id) {
        Log.d("Toolbar","Option 2 is selected");
        AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);
        builder.setTitle(R.string.toolbar_alert);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    else if(R.id.action3==id) {
        Log.d("Toolbar", "Option 3 is selected");
        showCustomDialog();
    }

    else if (R.id.action4==id) {
        Toast.makeText(this, "Version 1.0 by Aryan Vaghela", Toast.LENGTH_SHORT).show();
    }
        return true;
    }

}