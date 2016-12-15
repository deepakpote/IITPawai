package net.mavericklabs.mitra.utils;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by amoghpalnitkar on 12/15/16.
 */
public class StringUtilsTest {

    @Test
    public void getVideoKeyFromUrl() throws Exception {
        String videoUrl = "http://youtu.be/0zM4nApSvMg";
        String videoUrl1 = "https://youtu.be/0zM4nApSvMg";
        String videoUrl2 = "http://www.youtube.com/watch?v=0zM4nApSvMg";
        String videoUrl3 = "https://www.youtube.com/watch?v=0zM4nApSvMg";
        List<String> videos = new ArrayList<>();
        videos.add(videoUrl);
        videos.add(videoUrl1);
        videos.add(videoUrl2);
        videos.add(videoUrl3);

        for(String url : videos) {
            assertThat(StringUtils.getVideoKeyFromUrl(url),is("0zM4nApSvMg"));
        }
    }

}