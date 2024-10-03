package org.soulasphyxia.webcourseproject.utils;

import lombok.RequiredArgsConstructor;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
@RequiredArgsConstructor
public class ThumbnailTaker {

    public BufferedImage getThumbnailFromVideo(String url) throws FrameGrabber.Exception {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(url);
        frameGrabber.setFormat("mp4");
        frameGrabber.start();

        return frameGrabber.grab().getBufferedImage();
    }

}
