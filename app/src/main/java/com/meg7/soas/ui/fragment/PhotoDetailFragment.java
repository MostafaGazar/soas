package com.meg7.soas.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.meg7.soas.R;
import com.meg7.soas.SoasApplication;
import com.meg7.soas.data.Photo;

import org.parceler.Parcels;

/**
 * A fragment representing a single Photo detail screen.
 * This fragment is either contained in a {@link com.meg7.soas.ui.PhotosListActivity}
 * in two-pane mode (on tablets) or a {@link com.meg7.soas.ui.PhotoDetailActivity}
 * on handsets.
 */
public class PhotoDetailFragment extends Fragment {

    /**
     * The fragment argument representing the photo that this fragment represents.
     */
    public static final String ARG_PHOTO = "arg_photo";

    private Photo mPhoto;

    public static PhotoDetailFragment newInstance(Parcelable photo) {
        PhotoDetailFragment fragment = new PhotoDetailFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable(PhotoDetailFragment.ARG_PHOTO, photo);
        fragment.setArguments(arguments);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PhotoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args.containsKey(ARG_PHOTO)) {
            mPhoto = Parcels.unwrap(args.getParcelable(ARG_PHOTO));

            if (mPhoto != null && !TextUtils.isEmpty(mPhoto.photoTitle)) {
                getActivity().setTitle(mPhoto.photoTitle);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo_detail, container, false);

        if (mPhoto != null) {
            NetworkImageView photo = (NetworkImageView) rootView.findViewById(R.id.photo);
            photo.setImageUrl(mPhoto.photoFileUrl, SoasApplication.getImageLoader(getActivity()));
            photo.setDefaultImageResId(R.drawable.default_photo);

            TextView photoName = (TextView) rootView.findViewById(R.id.photoName);
            photoName.setText(mPhoto.photoTitle);

            TextView photoOwner = (TextView) rootView.findViewById(R.id.photoOwner);
            photoOwner.setText(mPhoto.ownerName);
        }

        return rootView;
    }
}
