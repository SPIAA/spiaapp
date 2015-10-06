package com.spiaa.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spiaa.R;
import com.spiaa.api.SpiaaService;
import com.spiaa.dados.DatabaseHelper;
import com.spiaa.dao.UsuarioDAO;
import com.spiaa.modelo.IsXLargeScreen;
import com.spiaa.modelo.Usuario;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //definição da orientação das telas da aplicação
        if (!new IsXLargeScreen().isXLargeScreen(getApplicationContext())) {
            //set phones to portrait;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            //Tablets como Landscape
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Button login = (Button) findViewById(R.id.botao_login);
        login.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    ///Depois que fez o login, da tela Main, não deixar voltar pra tela de login
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.botao_login) {
            //Fazer login
            TextView usuarioLogin = (TextView) findViewById(R.id.usuario_login);
            TextView senha = (TextView) findViewById(R.id.senha_login);
            Usuario agenteSaude = new Usuario();
            agenteSaude.setUsuario(usuarioLogin.getText().toString());
            agenteSaude.setSenha(senha.getText().toString());

            RestAdapter restAdapter = new RestAdapter.Builder()
                    //.setEndpoint("http://192.168.4.97:8084/Spiaa")
                    .setEndpoint("http://spiaa.herokuapp.com")
                    .build();

            SpiaaService service = restAdapter.create(SpiaaService.class);
            service.login(agenteSaude, new Callback<Usuario>() {
                @Override
                public void success(Usuario agente, Response response) {
                    if (agente != null) {

                        try {
                            //Cria banco de dados da aplicação ao fazer o login pela primeira vez
                            new DatabaseHelper(LoginActivity.this);
                        }catch (Exception e){
                            Log.e("SPIAA", "Erro ao tentar criar banco de dados", e);
                        }

                        try {
                            //Guarda usuário no banco de dados
                            new UsuarioDAO(LoginActivity.this).insert(agente);
                        } catch (Exception e) {
                            Log.e("SPIAA", "Erro no INSERT de Usuário logado", e);
                        }

                        SharedPreferences.Editor dadosUsuario = getSharedPreferences("UsuarioLogado", MODE_PRIVATE).edit();
                        dadosUsuario.putString("email", agente.getEmail());
                        dadosUsuario.putString("numero", agente.getNumero());
                        dadosUsuario.putString("nome", agente.getNome());
                        dadosUsuario.putString("senha", agente.getSenha());
                        dadosUsuario.putString("tipo", agente.getTipo());
                        dadosUsuario.putString("turma", agente.getTurma());
                        dadosUsuario.putString("usuario", agente.getUsuario());
                        dadosUsuario.putLong("id", agente.getId());
                        dadosUsuario.commit();

                        //Vai para a página inicial da aplicação
                        Intent intent = new Intent(LoginActivity.this, SincronizarActivity.class);
                        startActivity(intent);
                    } else {
                        Snackbar.make(v, "Falha no login. Verifique os dados de acesso.", Snackbar.LENGTH_LONG).show();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LoginActivity.this, "Erro no login!", Toast.LENGTH_LONG).show();

                    /*Vai para a página inicial da aplicação
                    Código temporário, apenas para a apresentação para a pré-banca FAITEC em 01/10/2015
                    Depois será removido este código
                    Vou deixar entrar na aplicação mesmo se ocorrer falha no login, para mostrar
                    a aplicação à banca nete dia
                     */
                    Intent intent = new Intent(LoginActivity.this, SincronizarActivity.class);
                    startActivity(intent);
                }
            });

        }
    }
}
