package ml.shahidkamal.udacitybakingapp.fragments;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.shahidkamal.udacitybakingapp.R;
import ml.shahidkamal.udacitybakingapp.model.Steps;
import ml.shahidkamal.udacitybakingapp.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment implements ExoPlayer.EventListener {

    public static final String TAG = StepsFragment.class.getSimpleName();
    List<Steps> stepsList;
    int position;
    @BindView(R.id.tv_step_description)
    TextView tvStepDesc;
    @BindView(R.id.iv_step_image)
    ImageView ivStepThumb;
    @BindView(R.id.exo_step_video)
    SimpleExoPlayerView simpleExoPlayerView;
    private PlaybackStateCompat.Builder playbackBuilder;
    SimpleExoPlayer simpleExoPlayer;
    MediaSessionCompat mediaSessionCompat;

    long positionPlayer;
    boolean playWhenReady;

    public StepsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            String stepsJSON = bundle.getString(Constants.INTENT_KEY_STEPS_FRAGMENT);
            position = bundle.getInt(Constants.INTENT_KEY_STEPS_POSITION);
            Gson gson = new Gson();
            stepsList = gson.fromJson(stepsJSON, new TypeToken<List<Steps>>(){}.getType());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, view);
        Steps steps = stepsList.get(position);
        Log.d(TAG, "Step:" + steps.getDescription()  + ", " + steps.getThumbnailURL() + ", " + steps.getVideoURL());
        Log.d(TAG, "Adapter postition: "+ position);
        if(!steps.getVideoURL().isEmpty()){
            Log.d(TAG, "Has VideoView" + steps.getVideoURL());
            initMedia();
            initializePlayer(Uri.parse(steps.getVideoURL()));
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT)  {
                hideUI();
                simpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                simpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
        }else{
            Log.d(TAG, "Has image"+steps.getThumbnailURL());
            simpleExoPlayerView.setVisibility(View.GONE);
            if(!steps.getThumbnailURL().isEmpty()) {
                ivStepThumb.setVisibility(View.VISIBLE);
                Picasso.get().load(steps.getThumbnailURL())
                        .centerInside()
                        .into(ivStepThumb);
            }
        }
        tvStepDesc.setText(steps.getDescription());
        return view;
    }

    private void initMedia() {
        mediaSessionCompat = new MediaSessionCompat(getActivity(), TAG);
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSessionCompat.setMediaButtonReceiver(null);
        playbackBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSessionCompat.setPlaybackState(playbackBuilder.build());
        mediaSessionCompat.setCallback(new SessionCallBacks());
        mediaSessionCompat.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance
                    (getActivity(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getActivity(),
                    getActivity().getString(R.string.application_name_exo_player));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getActivity(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            playbackBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            playbackBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        }
        mediaSessionCompat.setPlaybackState(playbackBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void hideUI() {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            //Use Google's "LeanBack" mode to get fullscreen in landscape
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.KEY_VISIBILITY_EXO_PLAYER, simpleExoPlayerView.getVisibility());
        outState.putInt(Constants.KEY_VISIBILITY_PLACEHOLDER, ivStepThumb.getVisibility());
        //Saving current Position before rotation
        outState.putLong(Constants.MEDIA_POS, positionPlayer);
        //for preserving state of exoplayer
        outState.putBoolean(Constants.KEY_PLAY_WHEN_READY, playWhenReady);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    private class SessionCallBacks extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();
            simpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            simpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            simpleExoPlayer.seekTo(0);
        }
    }

}
