package com.company;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
public class Main {

    public static final String IN_FILE_TXT = "/Users/yurii.ru/IdeaProjects/MusikAndPictures/src/com/company/input.txt";
    public static final String OUT_FILEMUSIC_TXT = "/Users/yurii.ru/IdeaProjects/MusikAndPictures/src/com/company/mus.txt";
    public static final String PATH_TO_MUSIC = "/Users/yurii.ru/IdeaProjects/MusikAndPictures/src/com/company/music";
    public static final String OUT_FILEPICTURE_TXT = "/Users/yurii.ru/IdeaProjects/MusikAndPictures/src/com/company/Picture.txt";
    public static final String PATH_TO_PICTURE = "/Users/yurii.ru/IdeaProjects/MusikAndPictures/src/com/company/picture";

    public static void main(String[] args) throws MalformedURLException {
        //MUSIC
        String Url;
        URL url = null;
        try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT));
             BufferedWriter outFile = new BufferedWriter(new FileWriter(OUT_FILEMUSIC_TXT))) {
            while ((Url = inFile.readLine()) != null) {
                url = new URL(Url);

                String result;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    result = bufferedReader.lines().collect(Collectors.joining("\n"));
                }
                //МУЗЫКА:

                Pattern email_pattern = Pattern.compile("https:\\/\\/ru.hitmotop.com\\/get\\/music(.+).mp3");
                Matcher matcher = email_pattern.matcher(result);
                int i = 0;
                while (matcher.find() & i < 3) {
                    outFile.write(matcher.group() + "\r\n");
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader musicFile = new BufferedReader(new FileReader(OUT_FILEMUSIC_TXT))) {
            String music;
            int count = 1;
            try {
                while ((music = musicFile.readLine()) != null) {
                    System.out.println("Audio #" + count + " has been installed");
                    downloadUsingNIO(music, PATH_TO_MUSIC + count + ".mp3");
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        new OurPlayer(url).start();

        //PICTURE
        try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT));
             BufferedWriter outFilePicture = new BufferedWriter(new FileWriter(OUT_FILEPICTURE_TXT))) {
            while ((Url = inFile.readLine()) != null) {
                URL url1 = new URL(Url);

                String result;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url1.openStream()))) {
                    result = bufferedReader.lines().collect(Collectors.joining("\n"));
                }


                //КАРТИНКИ:

                Pattern email_pattern = Pattern.compile("\\/covers\\/a\\/(.+).jpg");
                Matcher matcher = email_pattern.matcher(result);
                int t =0;
                while (matcher.find() & t < 3) {
                    outFilePicture.write("https://ru.hitmotop.com" + matcher.group() + "\r\n");
                    t++;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader musicFile = new BufferedReader(new FileReader(OUT_FILEPICTURE_TXT))) {
            String music;
            int count = 1;
            try {
                while ((music = musicFile.readLine()) != null) {
                    System.out.println("Image #" + count + " has been installed");
                    downloadUsingNIO(music, PATH_TO_PICTURE + count + ".jpg");
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream inputStream = new FileInputStream("src/com/company/music1.mp3")) {
            System.out.println("Audio #1 was started");
            try {
                Player player = new Player(inputStream);
                player.play();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void downloadUsingNIO(String strUrl, String file) throws IOException {
        URL url = new URL(strUrl);
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());
        FileOutputStream stream = new FileOutputStream(file);
        stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        stream.close();
        byteChannel.close();

    }
}
