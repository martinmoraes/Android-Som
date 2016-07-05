package br.com.earcadia.som;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;

public class Principal extends AppCompatActivity {
    private ImageButton mTocaImageButton;
    private ImageButton mGravaImageButton;
    private MediaRecorder mMediaRecorder;

    private int mMusicaAtual = 0;
    private boolean mTocando = false;
    private boolean mGravando = false;
    MediaPlayer mMediaPlayer = null;
    private int mListaMusicas[] = {
            R.raw.bhagavan,
            R.raw.ombagavan,
            R.raw.sbatankaur
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        mTocaImageButton = (ImageButton) findViewById(R.id.tocaImageButton);
        mGravaImageButton = (ImageButton) findViewById(R.id.gravarImageButton);

        final String arquivoSaida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio.3gp";

        mGravaImageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mGravando = true;
                mMediaRecorder = new MediaRecorder();
                try {
                    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mMediaRecorder.setOutputFile(arquivoSaida);
                    mMediaRecorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mGravaImageButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mMediaRecorder.start();
                return false;
            }
        });


        mGravaImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mGravando && mMediaRecorder != null) {
                    mGravando = false;
                    mGravaImageButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mMediaRecorder.stop();
                    mMediaRecorder.release();
                    mMediaRecorder = null;
                }

                para();
                MediaPlayer mediaPlayer = new MediaPlayer();

                try {
                    Log.d("MEUAPP",arquivoSaida);
                    mediaPlayer.setDataSource(arquivoSaida);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    tocando();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            para();
                            toca(null);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void toca(View view) {

        if (mTocando) {
            para();
        } else {
            mMediaPlayer = MediaPlayer.create(this, mListaMusicas[mMusicaAtual]);
            mMediaPlayer.start();
           tocando();
        }
    }

    public void anterior(View view) {
        if (mMusicaAtual == 0)
            mMusicaAtual = 2;
        else
            mMusicaAtual--;
        para();
        toca(null);
    }

    public void proximo(View view) {
        if (mMusicaAtual == 2)
            mMusicaAtual = 0;
        else
            mMusicaAtual++;
        para();
        toca(null);
    }

    private void tocando(){
        mTocaImageButton.setImageResource(android.R.drawable.ic_media_pause);
        mTocando = true;
    }

    private void para() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mTocaImageButton.setImageResource(android.R.drawable.ic_media_play);
        mTocando = false;
    }
}
