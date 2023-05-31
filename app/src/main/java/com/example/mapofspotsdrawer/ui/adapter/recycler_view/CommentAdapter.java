package com.example.mapofspotsdrawer.ui.adapter.recycler_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.model.Comment;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final FragmentActivity activity;

    private final List<Comment> comments;

    public CommentAdapter(FragmentActivity activity, List<Comment> comments) {
        this.activity = activity;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comments, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment currentComment = comments.get(position);

        // Установка имени комментатора.
        holder.commentatorName.setText(currentComment.getCommentatorDto().getName());

        // Установка текста комментария.
        holder.commentText.setText(currentComment.getText());

        // Установка даты добавления комментария.
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault());
        LocalDate addingDate =
                currentComment.getUploadDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        holder.commentDate.setText(addingDate.format(formatter));

        if (currentComment.getCommentatorDto().getImageInfoDtoList().size() == 0) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://" + currentComment.getCommentatorDto().getImageInfoDtoList().get(0).getUrl())
                .build();

        // Установка первой фотографии комментатора.
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call,
                                  @NonNull IOException e) {
                Toast.makeText(activity,
                        "Ошибка совершения запроса к серверу", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NonNull Call call,
                                   @NonNull Response response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    activity.runOnUiThread(() -> holder.commentatorImage.setImageBitmap(bitmap));
                }
                else {
                    Toast.makeText(activity,
                            "Ошибка получения фото спота с сервера", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView commentatorImage;
        public TextView commentatorName;
        public TextView commentText;
        public TextView commentDate;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentatorImage = itemView.findViewById(R.id.commentator_image);
            commentatorName = itemView.findViewById(R.id.commentator_name);
            commentText = itemView.findViewById(R.id.comment_text);
            commentDate = itemView.findViewById(R.id.comment_date);
        }
    }
}
