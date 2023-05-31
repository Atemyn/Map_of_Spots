package com.example.mapofspotsdrawer.api;

import com.example.mapofspotsdrawer.model.Comment;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentsAPI {
    @GET("/api/comments/get-by-spot-id/{spotId}")
    Call<List<Comment>> getSpotComments(@Path("spotId") Long spotId);

    @POST("/api/comments/add-comment-by-spot-id/{spotId}")
    Call<ResponseBody> postComment(@Path("spotId") Long spotId,
                                   @Header("Authorization") String jwtToken, @Body RequestBody requestBody);

    @DELETE("/api/comments/delete-by-id/{commentId}")
    Call<ResponseBody> deleteComment(@Path("commentId") Long commentId,
                                     @Header("Authorization") String jwtToken);
}
