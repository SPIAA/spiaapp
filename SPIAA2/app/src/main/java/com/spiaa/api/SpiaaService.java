package com.spiaa.api;

import com.spiaa.modelo.Bairro;
import com.spiaa.modelo.Criadouro;
import com.spiaa.modelo.Inseticida;
import com.spiaa.modelo.TipoImoveis;
import com.spiaa.modelo.TratamentoAntiVetorial;
import com.spiaa.modelo.Denuncia;
import com.spiaa.modelo.Usuario;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;

/**
 * Created by eless on 29/09/2015.
 */
public interface SpiaaService {
    @POST("/api/agente/login")
    void login(@Body Usuario agenteSaude, Callback<Usuario> callback);

    @POST("/api/agente/alterar")
    void alterarDadosUsuario(@Body Usuario usuario, Callback<String> callback);

    @POST("/api/bairroquarteirao/agente")
    void getBairrosQuarteiroes(@Body Usuario agenteSaude, Callback<List<Bairro>> callback);

    @POST("/api/tipoimovel")
    void getTiposImoveis(@Body Usuario agenteSaude, Callback<List<TipoImoveis>> callback);

    @POST("/api/inseticida")
    void getInseticidas(@Body Usuario agenteSaude, Callback<List<Inseticida>> callback);

    @POST("/api/criadouro")
    void getCriadouros(@Body Usuario agenteSaude, Callback<List<Criadouro>> callback);

    @POST("/api/denuncia/agente")
    void getDenuncias(@Body Usuario agenteSaude, Callback<List<Denuncia>> callback);

    @PUT("/api/denuncia/update")
    void setDenuncias(@Body List<Denuncia> denunciaList, Callback<String> callback);

    @PUT("/api/boletim/createjson")
    void setBoletim(@Body List<TratamentoAntiVetorial> tratamentoAntiVetorialList, Callback<String> callback);

}
