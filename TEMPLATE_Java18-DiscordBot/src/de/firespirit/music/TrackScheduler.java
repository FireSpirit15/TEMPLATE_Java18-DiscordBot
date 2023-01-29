package de.firespirit.music;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackScheduler extends AudioEventAdapter {
	
	public final AudioPlayer audioPlayer;
	public final BlockingQueue<AudioTrack> queue;
	
	public TrackScheduler(AudioPlayer ap) {
		this.audioPlayer = ap;
		this.queue = new LinkedBlockingQueue<>();		
	}
	
	public void queue(AudioTrack track) {
		if (!this.audioPlayer.startTrack(track, true)) {
			this.queue.offer(track);
		}
	}
	
	public void nextTrack() {
		this.audioPlayer.startTrack(this.queue.poll(), false);
	}
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			nextTrack();
		}
	}

}
