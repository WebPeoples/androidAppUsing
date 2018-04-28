package com.example.goku.using.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goku.using.R;
import com.example.goku.using.prefs.PreferencesManager;


/**
 * Created by Goku on 13/05/2017.
 */

public class BaseActivity extends AppCompatActivity {
     protected DrawerLayout drawerLayout;
     private Context context;
    //Método responsável por definir a toolbar como ActionBar
        protected void setUpToolBar(){
        //Instanciando nossa toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Verificando se nossa toolbar está nula
             if (toolbar != null){
                 //Definindo a ActionBar como nossa Toolbar
                 setSupportActionBar(toolbar);
                 //Definindo o título da Toolbar como nulo para melhor posicionar o logo Using
                 setTitle("");
                 }
             }

    //Método responsável por definir o menu lateral em uma activity
        protected void setUpNavDrawer(){
        //Chamamos método que define nossa toolbar pois precisaremos dela para definir nosso menu lateral
            setUpToolBar();

        //Drawer Layout
        //Como definimos o ActionBar como a Toolbar podemos configurá-la como se foose uma ActionBar
            final ActionBar actionBar = getSupportActionBar();

        //Ícone que dispara o menu lateral
            assert actionBar != null;
            actionBar.setHomeAsUpIndicator(R.drawable.ic_list_black_24dp_);
            actionBar.setDisplayHomeAsUpEnabled(true);

        //Instaciando o drawerLayout
            drawerLayout = (DrawerLayout) findViewById(R.id.activity_menu);


        //Instanciando menu lateral
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            NavigationView navigationViewcheck = (NavigationView) findViewById(R.id.maps_activity_nav_check_box);
            View view = navigationView.getHeaderView(0);
            TextView email = (TextView) view.findViewById(R.id.navheader_email);
            TextView nome = (TextView) view.findViewById(R.id.navheader_nome);
            TextView cduser = (TextView) view.findViewById(R.id.navheader_cduser);
            email.setText(PreferencesManager.getString(this, "email"));
            nome.setText(PreferencesManager.getString(this, "username"));
            cduser.setText(PreferencesManager.getString(this, "cduser"));

        //Verificamos se nossa variável (JAVA) de navigationView e drawerLayout estão vazias para evitar excessões
            if(navigationView != null && drawerLayout != null){
                //Atualiza a imagem e os textos do header
                navigationView.setNavigationItemSelectedListener(
                        new NavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                //Controle se este item é exibido com uma marca de seleção.
                                item.setChecked(true);

                                //Fecha todos drawers abertos no momento.
                                drawerLayout.closeDrawers();

                                //Método responsável por dar uma ação para os itens do menu lateral
                                onNavDrawerItemSelected(item);

                                return false;
                            }
                        }
                );
            }
        }

    //Método responsável por dar uma ação aos itens da Toolbar
        @Override
        public boolean onOptionsItemSelected(MenuItem item){
               switch (item.getItemId()){
                   case android.R.id.home:
                       if (drawerLayout != null){
                           //Abre o menu lateral.
                           openDrawer();
                           return true;
                       }
               }
            return super.onOptionsItemSelected(item);
        }

    //Método responsável por dar uma ação para os itens do menu lateral
        private void onNavDrawerItemSelected(MenuItem item){

            switch (item.getItemId()){
                case R.id.navdrawer_home:
                    Intent intent2 = new Intent(this, MenuActivity.class);
                    startActivity(intent2);
                    break;


                case R.id.navdrawer_busca:
                    Intent intent = new Intent( this, CategoriaActivity.class );
                    startActivity(intent);
                    break;

                case R.id.navdrawer_fav:
                    Intent intent3 = new Intent( this, FavoritoActivity.class );
                    startActivity(intent3);
                    break;

                case R.id.navdrawer_promo:
                    Intent intent4 = new Intent( this, PromoActivity.class );
                    startActivity(intent4);
                    break;

                case R.id.navdrawer_config:
                    Intent intent5 = new Intent( this, SettingsActivity.class );
                    startActivity(intent5);
                    break;

                case R.id.navdrawer_local:
                    Intent intent6 = new Intent( this, MapsActivity.class );
                    startActivity(intent6);
                    break;

                case R.id.navdrawer_sair:
                    PreferencesManager.setString(this, "email", "");
                    PreferencesManager.setString(this, "username", "");
                    PreferencesManager.setString(this, "cduser", "");
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
            }
        }
    //Abre o menu lateral.
        protected void openDrawer(){
            if (drawerLayout != null){
                drawerLayout.openDrawer(GravityCompat.START);

            }
        }


    //Adiciona o fragment ao centro da tela de  CATEGORIA
        protected  void replaceFragment(Fragment frag){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,frag,"TAG").commit();
        }

    //Adiciona o fragment ao centro da tela de  SUB CATEGORIA
        protected void replaceFragmentSub(Fragment frag){
            getSupportFragmentManager().beginTransaction().replace(R.id.container_subcategoria,frag,"TAG").commit();
        }

    //Método responsável por criar o dialog inicial pedindo as permissões necessárias
    //Exemplo: pede permissão para o usuário ceder a posição dele. (API 21+)
        public static void alert(Context context, int title, int message,int okButton, final Runnable runnable) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
            builder.setTitle(title).setMessage(message);
            String okString = okButton > 0 ? context.getString(okButton) : "OK";
            // Add the buttons
            builder.setPositiveButton(okString, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.show();
        }

    //Método responsável por criar um toast dinâmico
        public void toast(Context context,String t){
            Toast.makeText(context, t, Toast.LENGTH_SHORT).show();
        }


    //Método responsável por criar um dialog dinâmico
        protected void WarningDialog(String text){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            View view = getLayoutInflater().inflate(R.layout.alert_dialog_gps, null);

            Button ok = (Button) view.findViewById(R.id.alert_dialog_btOk);
            TextView textView = (TextView) view.findViewById(R.id.text_dialog);

            textView.setText(text);

            builder.setView(view);
            final AlertDialog dialog = builder.create();

            dialog.show();
            dialog.setCanceledOnTouchOutside(false);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.hide();
                }
            });
        }


    //Método responsável por testar a conexão com a internet
        protected boolean testInternet(Context context){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if ((netInfo != null) && netInfo.isConnectedOrConnecting() && netInfo.isAvailable()){
                return true;
            }else{
                return false;
            }
        }

    protected static int getDPI(int size, DisplayMetrics metrics){
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    // MÉTODOS COM POSSÍVEIS ÚTILIDADE FUTURAMENTE
    //--------------------------------------------------------------------------------
    /*protected void closeDrawer(){
        if (drawerLayout != null){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }*/
}
