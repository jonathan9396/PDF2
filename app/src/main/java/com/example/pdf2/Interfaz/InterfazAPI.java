package com.example.pdf2.Interfaz;

import com.example.pdf2.Inconsistencia.Inconsistencia;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface InterfazAPI {

    @Headers("x-api-key:9050e2053e607e81d0efb261512a497f")

    @GET("/censos-api/api/cuestionarios/single/{Llave}")
    public Call<Inconsistencia> find(@Path("Llave") String id);
}


