package org.marfnk.georallye;

import org.marfnk.georallye.R;
import org.marfnk.georallye.adapter.MenuListAdapter;
import org.marfnk.georallye.adapter.Observer;
import org.marfnk.georallye.data.Constants;
import org.marfnk.georallye.services.Compass;
import org.marfnk.georallye.services.GameEngine;
import org.marfnk.georallye.services.Navigator;
import org.marfnk.georallye.services.PersistanceManager;
import org.marfnk.georallye.services.QuestManager;
import org.marfnk.georallye.views.AccuracyTextView;
import org.marfnk.georallye.views.CompassView;
import org.marfnk.georallye.views.DistanceTextView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
    private Compass myCompass;
    private Navigator myNavigator;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private QuestManager questManager;
    private Button okButton;
    private EditText codeInput;
    private GameEngine gameEngine;
    private CompassView compassView;
    private DistanceTextView distanceText;
    private PersistanceManager persistanceManager;
    private MenuListAdapter menuListAdapter;
    private AccuracyTextView accuracyText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constants.TEXT_TYPEFACE = Typeface.createFromAsset(getAssets(), "fonts/SpecialElite.ttf");
        Constants.LOCALE = getResources().getConfiguration().locale;

        okButton = (Button) findViewById(R.id.codeButton);
        codeInput = (EditText) findViewById(R.id.codeInput);
        compassView = (CompassView) findViewById(R.id.compassView);
        distanceText = (DistanceTextView) findViewById(R.id.distanceText);
        accuracyText = (AccuracyTextView) findViewById(R.id.accuracyText);

        if (!gpsEnabled()) {
            // GPS OFF
            askToEnableGPS();
        }
        
        initializeCodeInput();
        intializeServices();
        initializeDrawer();

        compassView.setNavigator(myNavigator);
        compassView.setCompass(myCompass);
        distanceText.setNavigator(myNavigator);
        accuracyText.setNavigator(myNavigator);

        persistanceManager.restoreGameState();
    }

    private void askToEnableGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, please enable it!").setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                            @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean gpsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void intializeServices() {
        myCompass = new Compass(this);
        myNavigator = new Navigator(this);
        questManager = new QuestManager();
        gameEngine = new GameEngine(this, questManager, myNavigator);
        persistanceManager = new PersistanceManager(getPreferences(MODE_PRIVATE), gameEngine);
    }

    private void initializeCodeInput() {
        codeInput.setTypeface(Constants.TEXT_TYPEFACE);
        okButton.setTypeface(Constants.TEXT_TYPEFACE);
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeInput.getText().toString();
                MainActivity.this.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                gameEngine.processCode(code);
                codeInput.setText("");
            }
        });
    }

    private void initializeDrawer() {
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        menuListAdapter = new MenuListAdapter(this, questManager);
        mDrawerList.setAdapter(menuListAdapter);

        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerLayout.closeDrawer(mDrawerList);
                gameEngine.showInfoForQuestAtPosition(position);
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.open_drawer,
                R.string.close_drawer) {

            /**
             * completely closed state
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.app_name);
            }

            /**
             * completely open state
             * */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.drawer_open);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        questManager.addQuestActivationListener(new Observer() {
            @Override
            public void onNotified() {
                menuListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        myCompass.registerListener(SensorManager.SENSOR_DELAY_NORMAL);
        myNavigator.registerListener();
        super.onResume();
    }

    @Override
    protected void onPause() {
        myCompass.unregisterListener();
        myNavigator.unregisterListener();

        // Saves the state of the game
        persistanceManager.saveGameState();

        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.reset_app) {
            gameEngine.resetGame();
            menuListAdapter.notifyDataSetChanged();
        }

        if (id == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}