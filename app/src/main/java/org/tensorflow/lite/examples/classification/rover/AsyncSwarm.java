package org.tensorflow.lite.examples.classification.rover;

import android.os.AsyncTask;

public class AsyncSwarm extends AsyncTask<Void, Void, String> {
    Swarm swarm;

    public AsyncSwarm(Swarm swarm){
        this.swarm = swarm;
    }

    @Override
    protected String doInBackground(Void... params) {
        swarm.startSwarm();
        return null;
    }
}
