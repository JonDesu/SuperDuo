package barqsoft.footballscores.data;

import java.io.IOException;

import barqsoft.footballscores.model.TeamInfoResponse;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

public class RetroFootballApi {

    FootballDataApi mApi;

    private interface FootballDataApi {
        @GET("/v1/teams/{team_id}")
        Call<TeamInfoResponse> getTeamInformation(
                @Header("X-Auth-Token") String apiKey,
                @Path("team_id") String teamId
        );
    }

    public RetroFootballApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.football-data.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApi = retrofit.create(FootballDataApi.class);
    }

    public TeamInfoResponse getTeamInformation(String apiKey, String teamId) throws IOException {
        return mApi.getTeamInformation(apiKey, teamId).execute().body();
    }

}
