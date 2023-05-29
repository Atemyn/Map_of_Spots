package com.example.mapofspotsdrawer.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.api.UserAPI;
import com.example.mapofspotsdrawer.databinding.FragmentProfileDataBinding;
import com.example.mapofspotsdrawer.model.ImageInfoDto;
import com.example.mapofspotsdrawer.model.User;
import com.example.mapofspotsdrawer.retrofit.RetrofitService;
import com.example.mapofspotsdrawer.ui.adapter.ResponseBodyImageSliderAdapter;
import com.example.mapofspotsdrawer.ui.auth.AuthFragment;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDataFragment extends Fragment {

    private FragmentProfileDataBinding binding;

    private ProfileDataViewModel profileDataViewModel;

    private List<String> imagesUrls = new ArrayList<>();

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private boolean noImage = true;

    private RetrofitService retrofitService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileDataViewModel
                = new ViewModelProvider(this).get(ProfileDataViewModel.class);

        retrofitService = new RetrofitService(getString(R.string.server_url));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileDataBinding.inflate(inflater, container, false);

        addNameTextChangedListener();
        addEmailTextChangedListener();
        addPhoneTextChangedListener();
        addBirthdayTextChangedListener();
        addRegistrationDateTextChangedListener();

        binding.btnSignOut.setOnClickListener(view -> {
            // Обнуление jwtToken'а в SharedPreferences.
            PreferenceManager.getDefaultSharedPreferences(requireActivity())
                    .edit().putString("jwtToken", null).apply();
            showLoginFragment();
        });

        String name = profileDataViewModel.getName();
        String email = profileDataViewModel.getEmail();
        String phone = profileDataViewModel.getPhone();
        String birthday = profileDataViewModel.getBirthday();
        String registrationDate = profileDataViewModel.getRegistrationDate();

        if (name != null && email != null && phone != null
                && birthday != null && registrationDate != null) {
            binding.tvName.setText(name);
            binding.tvEmail.setText(email);
            binding.tvPhoneNumber.setText(phone);
            binding.tvBirthDate.setText(birthday);
            binding.tvRegistrationDate.setText(registrationDate);
        }
        else {
            getUserInfo();
        }

        ResponseBodyImageSliderAdapter adapter =
                new ResponseBodyImageSliderAdapter(requireActivity(),
                        profileDataViewModel.getImagesUrls());
        binding.imageSliderProfileData.setAdapter(adapter);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();
                    if (resultCode == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        addUserImage(result.getData().getData().toString());
                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(requireActivity(),
                                ImagePicker.getError(result.getData()), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(requireActivity(),
                                "Добавление фотографии отменено", Toast.LENGTH_LONG).show();
                    }
                });

        binding.btnAddUserImage.setOnClickListener(
                view -> ImagePicker.Companion.with(requireActivity())
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .createIntent(intent -> {
                            imagePickerLauncher.launch(intent);
                            return null;
                        }));

        binding.btnDeleteUserImage.setOnClickListener(view -> {
            if (!noImage) {
                if (profileDataViewModel.getImagesUrls().size() <= 1) {
                    profileDataViewModel.setImagesUrls(new ArrayList<>());
                    profileDataViewModel.addImageUri(getString(R.string.no_image_url));
                    noImage = true;
                }
                else {
                    profileDataViewModel.removeImageUriAt(
                            binding.imageSliderProfileData.getCurrentItem());
                }

                ResponseBodyImageSliderAdapter deleteSliderAdapter =
                        new ResponseBodyImageSliderAdapter(requireActivity(),
                                profileDataViewModel.getImagesUrls());
                binding.imageSliderProfileData.setAdapter(deleteSliderAdapter);

                if (!noImage) {
                    setAdapterAndIndicatorConfigs(deleteSliderAdapter);
                }
            }
            else {
                Toast.makeText(requireActivity(),
                        "Фотографий нет: удаление невозможно", Toast.LENGTH_LONG).show();
            }
        });

        return binding.getRoot();
    }

    private void setImages(User user) {
        List<String> viewModelImagesUrls =
                profileDataViewModel.getImagesUrls();
        if (viewModelImagesUrls != null && !viewModelImagesUrls.isEmpty()) {
            imagesUrls = viewModelImagesUrls;
        }
        else if (user != null && user.getImageInfoDtoList() != null
                && !user.getImageInfoDtoList().isEmpty()) {
            List<ImageInfoDto> imagesInfosFromServer = user.getImageInfoDtoList();
            for (ImageInfoDto info : imagesInfosFromServer) {
                imagesUrls.add(info.getUrl());
            }
            profileDataViewModel.setImagesUrls(imagesUrls);
        }
        else {
            imagesUrls.add(getString(R.string.no_image_url));
        }
    }

    private void setAdapterAndIndicatorConfigs(ResponseBodyImageSliderAdapter adapter) {
        binding.indicatorProfileData.setViewPager(binding.imageSliderProfileData);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                binding.indicatorProfileData.setViewPager(binding.imageSliderProfileData);
                adapter.setCurrentIndex(binding.imageSliderProfileData.getCurrentItem());
            }
        });
        binding.imageSliderProfileData.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                adapter.setCurrentIndex(position);
            }
        });

        adapter.setCurrentIndex(binding.imageSliderProfileData.getCurrentItem());
    }

    public void addNameTextChangedListener() {
        binding.tvName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    profileDataViewModel.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void addEmailTextChangedListener() {
        binding.tvEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                profileDataViewModel.setEmail(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void addPhoneTextChangedListener() {
        binding.tvPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                profileDataViewModel.setPhone(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void addBirthdayTextChangedListener() {
        binding.tvBirthDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                profileDataViewModel.setBirthday(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void addRegistrationDateTextChangedListener() {
        binding.tvRegistrationDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                profileDataViewModel.setRegistrationDate(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getUserInfo() {

        String bearer = "Bearer " + PreferenceManager.getDefaultSharedPreferences(requireActivity())
                .getString("jwtToken", null);

        binding.progressBar.setVisibility(View.VISIBLE);

        // Создание API для совершения запроса к серверу.
        UserAPI userAPI = retrofitService.getRetrofit().create(UserAPI.class);

        userAPI.getUserInfo(bearer)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call,
                                           @NonNull Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();
                            if (user == null) {
                                disableProgressBarAndShowNotification("Ошибка получения тела ответа");
                                return;
                            }

                            setUserInfoTextViews(user);
                            setImages(user);
                            requireActivity().runOnUiThread(()
                                    -> binding.progressBar.setVisibility(View.GONE));
                        }
                        else {
                            disableProgressBarAndShowNotification("Ошибка обработки запроса на сервере");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call,
                                          @NonNull Throwable t) {
                        disableProgressBarAndShowNotification("Ошибка отправки запроса на сервер");
                    }
                });
    }

    private void addUserImage(String imageStringUrl) {
        try {
            String bearer = "Bearer " + PreferenceManager.getDefaultSharedPreferences(requireActivity())
                    .getString("jwtToken", null);

            // Получение объекта File картинки пользователя.
            URL imageUrl = new URL(imageStringUrl);
            File imageFile = new File(imageUrl.getFile());

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part filePart =
                    MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

            binding.progressBar.setVisibility(View.VISIBLE);

            // Создание API для совершения запроса к серверу.
            UserAPI userAPI = retrofitService.getRetrofit().create(UserAPI.class);

            userAPI.uploadUserImage(bearer, filePart)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call,
                                               @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                if (noImage) {
                                    profileDataViewModel.setImagesUrls(new ArrayList<>());
                                    noImage = false;
                                }
                                profileDataViewModel.addImageUri(imageStringUrl);

                                ResponseBodyImageSliderAdapter addSliderAdapter =
                                        new ResponseBodyImageSliderAdapter(requireActivity(),
                                                profileDataViewModel.getImagesUrls());
                                binding.imageSliderProfileData.setAdapter(addSliderAdapter);

                                setAdapterAndIndicatorConfigs(addSliderAdapter);
                            }
                            else {
                                disableProgressBarAndShowNotification("Ошибка обработки запроса на сервере");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call,
                                              @NonNull Throwable t) {
                            disableProgressBarAndShowNotification("Ошибка отправки запроса на сервер");
                        }
                    });
        }
        catch (MalformedURLException e) {
            disableProgressBarAndShowNotification("Ошибка получения фотографии");
        }
    }

    private void setUserInfoTextViews(User user) {
        String name = user.getName();
        if (name != null && !name.isEmpty()) {
            binding.tvName.setText(name);
        }

        String email = user.getEmail();
        if (email != null && !email.isEmpty()) {
            binding.tvEmail.setText(email);
        }

        String phone = user.getPhoneNumber();
        if (phone != null && !phone.isEmpty()) {
            binding.tvPhoneNumber.setText(phone);
        }

        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault());


        if (user.getBirthday() != null) {
            LocalDate birthday =
                    user.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            binding.tvBirthDate.setText(birthday.format(formatter));
        }

        if (user.getRegDate() != null) {
            LocalDate registrationDate =
                    user.getRegDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            binding.tvRegistrationDate.setText(registrationDate.format(formatter));
        }
    }

    private void disableProgressBarAndShowNotification(String message) {
        requireActivity().runOnUiThread(() ->
                binding.progressBar.setVisibility(View.GONE));
        Toast.makeText(getActivity(),
                message, Toast.LENGTH_LONG).show();
    }

    public void showLoginFragment() {
        Bundle fragmentProfileIndicator = new Bundle();
        fragmentProfileIndicator.putString(getString(R.string.fragment_indicator_key),
                getString(R.string.fragment_profile_indicator));

        AuthFragment authFragment = new AuthFragment();
        authFragment.setArguments(fragmentProfileIndicator);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_profile, authFragment)
                .commit();
    }

}