package vanity.cms.image.handler

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component
import vanity.image.gorm.Image

import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import javax.imageio.stream.FileImageOutputStream
import javax.imageio.stream.ImageOutputStream
import java.awt.image.BufferedImage

@Slf4j
@Component
class BaseImageResourceHandler implements ImageResourceHandler {

    @Override
    public Image save(final SavingSpecification specification) {
        if (!validateParams(specification)) {
            throw new ImageHandlingException('Invalid list of parameters')
        }

        BufferedImage bufferedImage = ImageIO.read(specification.uploadedImage.getInputStream())

        if (!validateDimension(bufferedImage, specification)) {
            throw new ImageHandlingException('Invalid image dimension')
        }

        if (!writeImage(bufferedImage, specification.path)) {
            throw new ImageHandlingException('File saving error')
        }

        return new Image(name: specification.fileName, width: bufferedImage.width, height: bufferedImage.height)
    }

    private validateParams(final SavingSpecification specification) {
        return (specification && specification.validate() && !specification.uploadedImage.isEmpty())
    }

    private validateDimension(final BufferedImage image, final SavingSpecification specification) {
        return (image.width <= specification.maxWidth && image.height <= specification.maxHeight)
    }

    private boolean writeImage(final BufferedImage bufferedImage, final String fullPath) {
        try {
            log.info('Try to save image into {}', fullPath)
            ImageOutputStream output = new FileImageOutputStream(new File(fullPath))
            IIOImage image = new IIOImage(bufferedImage, null, null);
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next()
            ImageWriteParam param = writer.getDefaultWriteParam()
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(1.0F);
            writer.setOutput(output)
            writer.write(null, image, param);
            writer.dispose()
            output.flush()
            return true
        } catch (Exception exp) {
            log.info('Exception during saving image', exp)
            return false
        }
    }

    @Override
    boolean delete(final DeletingSpecification specification) {
        if (!validateParams(specification)) {
            throw new ImageHandlingException('Invalid list of parameters')
        }

        File file = new File(specification.path)

        if (!file.exists()) {
            throw new ImageHandlingException("No image found in path ${file}")
        }

        return file.delete()
    }

    private validateParams(final DeletingSpecification specification) {
        return (specification && specification.validate())
    }
}
