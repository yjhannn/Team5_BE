package ojosama.talkak.video.util;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ThumbnailExtractor {
    private final static String videoPath = "src/main/resources/videos/";
    private final static String thumbnailPath = "src/main/resources/thumbnails/";

    public static String extract(MultipartFile videoFile, String filename) { // .mp4 없는 filename 전달
        boolean status = false;
        File tempVideoFile = null;
        try {
            String tempVideoPath = videoPath + filename + "_temp.mp4";
            tempVideoFile = new File(tempVideoPath);
            videoFile.transferTo(tempVideoFile);

            FileChannelWrapper ch = NIOUtils.readableChannel(tempVideoFile);
            FrameGrab grab = FrameGrab.createFrameGrab(ch);

            double midpointFrame = grab.getVideoTrack().getMeta().getTotalFrames() / 2;
            grab.seekToFramePrecise((int) midpointFrame);

            Picture picture = grab.getNativeFrame();
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);

            String newThumbnailPath = thumbnailPath + filename + ".jpg";
            File thumbnailFile = new File(newThumbnailPath);
            ImageIO.write(bufferedImage, "jpg", thumbnailFile);

            ch.close();
            status = true;
        } catch (IOException | JCodecException e) {
            e.printStackTrace();
        } finally {
            // 임시 파일 삭제
            if (tempVideoFile != null && tempVideoFile.exists()) {
                tempVideoFile.delete();
            }

            if (status) {
                return thumbnailPath + filename + ".jpg";
            }
            return null;
        }
    }
}
