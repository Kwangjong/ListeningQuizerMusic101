import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.lang.InterruptedException;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

/**
 * ListeningQuizer
 * Listensing Quizer for Music 101 UW Madison Fall 2018
 * created by personal test preping
 * @author KJ
 */
public class ListeningQuizer {

  public static ArrayList<WAV> listeningList; // arraylist of wavs that will be quized on 
  public static Clip clip; // music player
  public static final String DIR = "/Users/kwangjong/Library/Mobile Documents/com~apple~CloudDocs/School/MUSIC 101/Listening Quiz/Listening List/"; // directory of the folder with wavs

  /**
   * load wavs from given directory
   */
  public static void loadWAV() {
    // make a arraylist of all the files in the given directory
    listeningList = new ArrayList<WAV>();
    try {
      File folder = new File(DIR);
      File[] list = folder.listFiles(); // get list of all files in DIR
      for(File file : list) {
        if(file.getName().contains(".wav")) // add only wav files to listeningList 
          listeningList.add(new WAV(file));
      }
    } catch(NullPointerException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * play random wav from random position
   * @return string of its file name
   */
  public static WAV playMusic() {
    Random rand = new Random();
    // randomly choose fron the listening list
    WAV wav = listeningList.get(rand.nextInt(listeningList.size()));
    while(wav.played) {
      wav = listeningList.get(rand.nextInt(listeningList.size())); // skiped music already played
    }

    // play music
    try {
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(wav.file());
      clip = AudioSystem.getClip();
      clip.open(audioIn);
      // randomly choose played position from 0 to 90% of the length in unit of 10secs
      long pos = rand.nextInt((int) ((clip.getMicrosecondLength()/1000000)*0.9)/10)*1000000*10;
      clip.setMicrosecondPosition(pos);
      clip.start();
      wav.played = true; // change status to played
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
      ex.printStackTrace();
    }
    return wav;
  }

  /**
   * play quiz
   * quiz will be looped until all of the music is played or user typed quit
   */
  public static void quiz() {
    Scanner scnr = new Scanner(System.in);
    Random rand = new Random();
    boolean exit = false;

    System.out.println("MUSIC 101 LISTENING QUIZER");
    sleep(1500);
    System.out.println("type quit to exit");
    sleep(800);

    // loop
    int num = 1;
    while(!exit) {
      System.out.println();
      System.out.println();
      sleep(800);
      System.out.println("#"+num); // problem number
      System.out.println();
      num++;

      // print choices
      String name;
      WAV[] choices = new WAV[4];
      int ans = rand.nextInt(4); // randomly choose which number the answer will be
      choices[ans] = playMusic();

      // add other choices
      for(int i=0; i<4; i++) {
        if(choices[i] == null) {
          WAV choice = listeningList.get(rand.nextInt(listeningList.size()));
          while(choices[0] == choice || choices[1] == choice || choices[2] == choice || choices[3] == choice) {
            choice = listeningList.get(rand.nextInt(listeningList.size()));
          }
          choices[i] = choice;
        }
      }

      // print choices
      for(int i=0;i<4;i++) {
        name = choices[i].file().getName();
        System.out.println((i+1)+") "+name.substring(3,name.length()-4));
      }

      // get answer
      System.out.println();
      System.out.print("Ans: ");
      String line = scnr.nextLine();

      if(line.equals(""+(ans+1))) {
        System.out.println("Correct");
      } else if(line.equals("quit")) { // quit
        exit = true;
      } else {
        System.out.println("Wrong");
        choices[ans].played = false; // try it again later
      }

      // quit if all of the music is played
      int sum = 0;
      for(WAV wav : listeningList) {
        if(wav.played)
          sum++;
      }
      if(sum == listeningList.size())
        exit = true;

      clip.stop();
    }
  }

  public static void sleep(int millisec) {
    try {
      Thread.sleep(millisec);
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
  }

  /**
   * main method
   * runs ListeningQuizer
   */
  public static void main(String args[]) {
    loadWAV();
    quiz();
  }
}