package vanity.cms.image.handler

import grails.validation.Validateable
import org.springframework.web.multipart.MultipartFile

@Validateable
final class HandlingSpecification {

    MultipartFile uploadedImage

    String location

    String prefix

    Long uniqueness

    Integer maxWidth

    Integer maxHeight

    static constraints = {
        uploadedImage(nullable: false)
        location(nullable: false, blank: false)
        prefix(nullable: false, blank: false)
        uniqueness(nullable: false)
        maxWidth(nullable: false)
        maxHeight(nullable: false)
    }

    public String getPath() {
        return "${location}/${fileName}"
    }

    public String getFileName() {
        return "${prefix}_${uniqueness}.${fileExtension}"
    }

    public String getFileExtension() {
        if (!uploadedImage) {
            throw new IllegalStateException('No image to analyze')
        }

        return uploadedImage.originalFilename.tokenize('.').pop()
    }
}
