package petcare.com.mypetcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import petcare.com.mypetcare.Adapter.NavigationListViewAdapter;
import petcare.com.mypetcare.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setCustomActionBar();
//        setLogo();
        return super.onPrepareOptionsMenu(menu);
    }

    private void setCustomActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbarView = inflater.inflate(R.layout.main_custom_actionbar, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionbarView, lp1);

        Toolbar parent = (Toolbar) actionbarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ImageButton ibHamburger = (ImageButton) findViewById(R.id.ibHeaderHamburger);
        ibHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        final ListView lv = (ListView) findViewById(R.id.nav_lst);
        NavigationListViewAdapter adapter = new NavigationListViewAdapter(this);
        lv.setAdapter(adapter);

        adapter.addItem(R.drawable.ic_list, R.string.nav_diary, null);
        adapter.addItem(R.drawable.ic_add_info, R.string.nav_info, null);
        adapter.addItem(R.drawable.ic_share, R.string.nav_share, null);
        adapter.addItem(R.drawable.ic_info, R.string.nav_inquiry, null);
        adapter.addItem(R.drawable.ic_event_notice, R.string.nav_notice, R.drawable.ic_event_notice);
        adapter.addItem(R.drawable.ic_setting_t, R.string.nav_setting, null);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.tv_nav_list_title);
                String title = getApplicationContext().getResources().getString(R.string.nav_info);
                if (StringUtils.equals(title, tv.getText())) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addinfo) {
            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
        }

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
