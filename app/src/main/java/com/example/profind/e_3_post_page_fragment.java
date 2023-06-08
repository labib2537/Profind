package com.example.profind;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class e_3_post_page_fragment extends Fragment implements View.OnClickListener, TextWatcher {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    CircleImageView profile_img;
    ImageView img_1, img_2, img_3, img_4;
    VideoView vdo_1, vdo_2, vdo_3, vdo_4;
    LinearLayout oops_layout, post_layout,
            img_vdo_1, img_vdo_1_1, img_vdo_1_2, img_vdo_2, img_vdo_2_1, img_vdo_2_2;

    TextView name_place_tag_txt, extra_img_vdo_txt;
    EditText post_edttxt;
    Button gallery_btn, location_btn, post_btn;
    ImageButton cancel_img_btn;
    int pic = 0;
    String name, phone, place, profession, pro_account;
    StringBuffer sb = new StringBuffer();
    StringBuffer img_vdo_indicator = new StringBuffer();
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    Map<String, String> post_item = new HashMap<>();
    ArrayList<Uri> imag_array = new ArrayList<>();
    int width_half;

    FirebaseStorage storage;
    FirebaseFirestore db;


    public e_3_post_page_fragment(int width_half) {
        this.width_half = width_half;
    }

    public static e_3_post_page_fragment newInstance(String param1, String param2, int width_half) {
        e_3_post_page_fragment fragment = new e_3_post_page_fragment(width_half);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e_3_post_page_fragment, container, false);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        progressBar = new ProgressBar(view.getContext());
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressDrawable(null);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        b_0_1_Session session = new b_0_1_Session(view.getContext());
        HashMap<String, String> userDetails = session.getuserdetailFromSession();
        name = userDetails.get(b_0_1_Session.KEY_NAME);
        phone = userDetails.get(b_0_1_Session.KEY_PHONENUMBER);

        profile_img = view.findViewById(R.id.post_fr_profile_image_id);
        name_place_tag_txt = view.findViewById(R.id.post_fr_profile_name_id);
        name_place_tag_txt.setText(Html.fromHtml("<b>" + name + "</b>"));
        post_edttxt = view.findViewById(R.id.post_fr_edttext_id);
        extra_img_vdo_txt = view.findViewById(R.id.post_fr_extra_img_vdo_txt_id);

        oops_layout = view.findViewById(R.id.post_fr_oops_layout_id);
        post_layout = view.findViewById(R.id.post_fr_post_layout_id);

        //Toast.makeText(getContext(), width_half+"", Toast.LENGTH_SHORT).show();

        db.collection("users")
                .whereEqualTo("Phone_Number", phone)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (!document.getString("Profile_Photo").isEmpty()) {
                                String urlp = document.getString("Profile_Photo");
                                profession = document.getString("Profession");
                                Picasso.get()
                                        .load(urlp)
                                        .placeholder(R.drawable.profile_img)
                                        .into(profile_img);

                            }
                        }
                    }
                });

        img_vdo_1 = view.findViewById(R.id.post_fr_img_vdo_1_id);
        img_vdo_1_1 = view.findViewById(R.id.post_fr_img_vdo_1_1_id);
        img_vdo_1_2 = view.findViewById(R.id.post_fr_img_vdo_1_2_id);
        img_vdo_2 = view.findViewById(R.id.post_fr_img_vdo_2_id);
        img_vdo_2_1 = view.findViewById(R.id.post_fr_img_vdo_2_1_id);
        img_vdo_2_2 = view.findViewById(R.id.post_fr_img_vdo_2_2_id);


        img_1 = view.findViewById(R.id.post_fr_img_1_id);
        img_2 = view.findViewById(R.id.post_fr_img_2_id);
        img_3 = view.findViewById(R.id.post_fr_img_3_id);
        img_4 = view.findViewById(R.id.post_fr_img_4_id);
        vdo_1 = view.findViewById(R.id.post_fr_vdo_1_id);
        vdo_2 = view.findViewById(R.id.post_fr_vdo_2_id);
        vdo_3 = view.findViewById(R.id.post_fr_vdo_3_id);
        vdo_4 = view.findViewById(R.id.post_fr_vdo_4_id);

        cancel_img_btn = view.findViewById(R.id.post_fr_cancel_img_vdo_id);
        gallery_btn = view.findViewById(R.id.post_fr_gallery_btn_id);
        location_btn = view.findViewById(R.id.post_fr_location_btn_id);
        post_btn = view.findViewById(R.id.post_fr_post_btn_id);


        post_btn.setEnabled(false);
        post_edttxt.addTextChangedListener(this);


        cancel_img_btn.setOnClickListener(this);
        gallery_btn.setOnClickListener(this);
        location_btn.setOnClickListener(this);
        post_btn.setOnClickListener(this);


        return view;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.post_fr_gallery_btn_id) {
            if (Build.VERSION.SDK_INT < 19) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/* video/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 11);
            } else {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/* video/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 11);
            }
        } else if (view.getId() == R.id.post_fr_location_btn_id) {
            Intent intent = new Intent(getContext(), d_0_0_9_post_location_page.class);
            startActivityForResult(intent, 12);

        }  else if (view.getId() == R.id.post_fr_post_btn_id) {
            progressDialog.show();
            String img_vdo_name;
            final String time = new Date().getTime() + "";
            String txts = post_edttxt.getText().toString().trim();
            post_item.put("phone", phone);
            post_item.put("post_type", "personal");
            post_item.put("group_id", "");
            post_item.put("description", txts);
            post_item.put("posted_at", time);
            post_item.put("profession", profession);
            post_item.put("reacts","0");
            post_item.put("comments","0");
            post_item.put("reports","0");
            if (place == null) {
                place = "";
            }
            post_item.put("place", place);
            if (!imag_array.isEmpty()) {
                for (int i = 0; i < imag_array.size(); i++) {
                    img_vdo_name = i + ".img_vdo";
                    String extnsn = get_extension(imag_array.get(i));
                    if (extnsn.equals("webp") || extnsn.equals("svg") || extnsn.equals("png") || extnsn.equals("jpeg") ||
                            extnsn.equals("jpg") || extnsn.equals("gif") || extnsn.equals("apng")) {
                        img_vdo_indicator.append("0 ");
                    } else if (extnsn.equals("webm") || extnsn.equals("mkv") || extnsn.equals("flv") || extnsn.equals("avi") ||
                            extnsn.equals("3gp") || extnsn.equals("mp4") || extnsn.equals("wmv") || extnsn.equals("mov")) {
                        img_vdo_indicator.append("1 ");
                    }
                    StorageReference storageRef = storage.getReference().child("posts")
                            .child(phone).child(time).child(img_vdo_name + "." + extnsn);
                    storageRef.putFile(imag_array.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    append_uri(uri.toString());
                                    // post_item.put(post_item.size()+1+".img_vdo",uri.toString());
                                }
                            });
                        }
                    });


                }
            } else {
                post_item.put("images", "");
            }
            if (img_vdo_indicator.length() == 0) {
                post_item.put("img_vdo_indicator", "");
            }
            post_item.put("img_vdo_indicator", img_vdo_indicator.toString().trim());
            Thread thread = new Thread() {
                public void run() {
                    try {
                        sleep(20000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        db.collection("posts")
                                .add(post_item)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Successfully Posted", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }
            };
            thread.start();

        } else if (view.getId() == R.id.post_fr_cancel_img_vdo_id) {
            LinearLayout.LayoutParams lout_empty = new LinearLayout.LayoutParams(0, 0);
            img_vdo_1.setLayoutParams(lout_empty);
            img_vdo_2.setLayoutParams(lout_empty);
            cancel_img_btn.setLayoutParams(lout_empty);
            pic = 0;
            String txts = post_edttxt.getText().toString();
            if (txts.length() == 0 && pic == 0) {
                post_btn.setEnabled(false);
            } else {
                post_btn.setEnabled(true);
            }
        }
    }

    private String get_extension(Uri uri) {
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void append_uri(String str) {
        sb.append(str + " ");
        post_item.put("images", sb.toString().trim());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (!imag_array.isEmpty()) {
                imag_array.clear();
            }

            int layout_width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = width_half;

            LinearLayout.LayoutParams lout_empty = new LinearLayout.LayoutParams(0, 0);
            LinearLayout.LayoutParams btn_apr = new LinearLayout.LayoutParams(100, 100);
            LinearLayout.LayoutParams lout_not_empty = new LinearLayout.LayoutParams(layout_width, height);
            LinearLayout.LayoutParams lout_not_empty_vdo = new LinearLayout.LayoutParams(layout_width, height);
            LinearLayout.LayoutParams lout_not_empty_2 = new LinearLayout.LayoutParams(width_half, height);
            LinearLayout.LayoutParams lout_not_empty_3 = new LinearLayout.LayoutParams(width_half, height);

            btn_apr.setMargins(0, 10, 10, 10);
            lout_not_empty_3.setMargins(-width_half, 0, 0, 0);

            if (requestCode == 11 && data.getClipData() != null) {
                pic = 1;
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    imag_array.add(uri);
                    if (i == 0) {
                        if (uri.toString().contains("image")) {
                            img_1.setImageURI(uri);
                            img_1.setLayoutParams(lout_not_empty_2);
                            img_vdo_1.setLayoutParams(lout_not_empty);
                            img_vdo_1_1.setLayoutParams(lout_not_empty_2);
                            img_vdo_1_1.setPadding(10, 10, 5, 10);
                            vdo_1.setLayoutParams(lout_empty);
                            img_vdo_2.setLayoutParams(lout_empty);
                            cancel_img_btn.setLayoutParams(btn_apr);
                        } else if (uri.toString().contains("video")) {
                            vdo_1.setVideoURI(uri);
                            vdo_1.setLayoutParams(lout_not_empty_2);
                            img_vdo_1.setLayoutParams(lout_not_empty);
                            img_vdo_1_1.setLayoutParams(lout_not_empty_2);
                            img_vdo_1_1.setPadding(10, 10, 5, 10);
                            img_1.setLayoutParams(lout_empty);
                            img_vdo_2.setLayoutParams(lout_empty);
                            cancel_img_btn.setLayoutParams(btn_apr);
                            video_controler(vdo_1);
                        }
                    } else if (i == 1) {
                        if (uri.toString().contains("image")) {
                            img_2.setImageURI(uri);
                            img_2.setLayoutParams(lout_not_empty_2);
                            img_vdo_1.setLayoutParams(lout_not_empty);
                            img_vdo_1_2.setLayoutParams(lout_not_empty_2);
                            img_vdo_1_2.setPadding(5, 10, 10, 10);
                            vdo_2.setLayoutParams(lout_empty);
                            img_vdo_2.setLayoutParams(lout_empty);

                        } else if (uri.toString().contains("video")) {
                            vdo_2.setVideoURI(uri);
                            vdo_2.setLayoutParams(lout_not_empty_2);
                            img_vdo_1.setLayoutParams(lout_not_empty);
                            img_vdo_1_2.setLayoutParams(lout_not_empty_2);
                            img_vdo_1_2.setPadding(5, 10, 10, 10);
                            img_2.setLayoutParams(lout_empty);
                            img_vdo_2.setLayoutParams(lout_empty);
                            video_controler(vdo_2);
                        }
                    } else if (i == 2) {
                        if (uri.toString().contains("image")) {
                            img_3.setImageURI(uri);
                            img_3.setLayoutParams(lout_not_empty_vdo);
                            img_vdo_2_1.setLayoutParams(lout_not_empty_vdo);
                            img_vdo_2_1.setPadding(10, 0, 5, 10);
                            img_vdo_2_2.setLayoutParams(lout_empty);
                            vdo_3.setLayoutParams(lout_empty);
                            img_vdo_2.setLayoutParams(lout_not_empty_vdo);

                        } else if (uri.toString().contains("video")) {
                            vdo_3.setVideoURI(uri);
                            vdo_3.setLayoutParams(lout_not_empty_vdo);
                            img_vdo_2_1.setLayoutParams(lout_not_empty_vdo);
                            img_vdo_2_1.setPadding(10, 0, 5, 10);
                            img_vdo_2_2.setLayoutParams(lout_empty);
                            img_3.setLayoutParams(lout_empty);
                            img_vdo_2.setLayoutParams(lout_not_empty_vdo);
                            video_controler(vdo_3);
                        }
                    } else if (i == 3) {
                        if (uri.toString().contains("image")) {
                            img_4.setImageURI(uri);
                            img_4.setLayoutParams(lout_not_empty_2);
                            img_vdo_2_1.setLayoutParams(lout_not_empty_2);
                            img_vdo_2_2.setLayoutParams(lout_not_empty_2);
                            img_vdo_2_2.setPadding(5, 0, 10, 10);
                            img_vdo_2.setLayoutParams(lout_not_empty_vdo);
                            vdo_4.setLayoutParams(lout_empty);
                            extra_img_vdo_txt.setLayoutParams(lout_empty);


                        } else if (uri.toString().contains("video")) {
                            vdo_4.setVideoURI(uri);
                            vdo_4.setLayoutParams(lout_not_empty_2);
                            img_vdo_2_1.setLayoutParams(lout_not_empty_2);
                            img_vdo_2_2.setLayoutParams(lout_not_empty_2);
                            img_vdo_2_2.setPadding(5, 0, 10, 10);
                            img_4.setLayoutParams(lout_empty);
                            extra_img_vdo_txt.setLayoutParams(lout_empty);
                            img_vdo_2.setLayoutParams(lout_not_empty_vdo);
                            video_controler(vdo_4);
                        }
                    } else if (i > 3) {
                        extra_img_vdo_txt.setText("+" + (count - 3));
                        extra_img_vdo_txt.setLayoutParams(lout_not_empty_3);
                    }

                }
                count = 0;

            } else if (requestCode == 11 && data.getData() != null) {
                pic = 1;
                img_vdo_2.setLayoutParams(lout_empty);
                Uri uri = data.getData();
                imag_array.add(uri);

                if (uri.toString().contains("image")) {
                    img_1.setImageURI(uri);
                    img_1.setLayoutParams(lout_not_empty);
                    img_2.setLayoutParams(lout_empty);
                    vdo_1.setLayoutParams(lout_empty);
                    vdo_2.setLayoutParams(lout_empty);
                    img_vdo_1.setLayoutParams(lout_not_empty);
                    img_vdo_1_1.setLayoutParams(lout_not_empty);
                    img_vdo_1_1.setPadding(10, 10, 5, 10);
                    img_vdo_1_2.setLayoutParams(lout_empty);
                    cancel_img_btn.setLayoutParams(btn_apr);

                } else if (uri.toString().contains("video")) {
                    vdo_1.setVideoURI(uri);
                    img_1.setLayoutParams(lout_empty);
                    img_2.setLayoutParams(lout_empty);
                    vdo_1.setLayoutParams(lout_not_empty);
                    vdo_2.setLayoutParams(lout_empty);
                    img_vdo_1.setLayoutParams(lout_not_empty);
                    img_vdo_1_1.setLayoutParams(lout_not_empty);
                    img_vdo_1_1.setPadding(10, 10, 5, 10);
                    img_vdo_1_2.setLayoutParams(lout_empty);
                    cancel_img_btn.setLayoutParams(btn_apr);
                    video_controler(vdo_1);
                }


            } else if (requestCode == 12) {

                place = data.getStringExtra("place_key");
                String at;
                if (place.length() == 0) {
                    at = "";
                } else {
                    at = " - is in ";
                }

                name_place_tag_txt.setText(Html.fromHtml("<b>" + name + "</b>"
                        + at + " <b>" + place + "</b>"));
            }


        } catch (Exception e) {

        }
        if (pic == 0) {
            post_btn.setEnabled(false);
        } else {
            post_btn.setEnabled(true);
        }

    }

    public void video_controler(VideoView v) {
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(v);
        v.setMediaController(mediaController);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String txts = post_edttxt.getText().toString();
        if (txts.length() == 0 && pic == 0) {
            post_btn.setEnabled(false);
        } else {
            post_btn.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}