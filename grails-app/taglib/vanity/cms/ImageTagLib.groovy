package vanity.cms

import vanity.celebrity.CelebrityImage
import vanity.media.ImageService

class ImageTagLib {

    private static final Integer IMG_SIZE = 300

    static namespace = 'image'

    ImageService imageService

    def celebrity = { attrs, body ->
        CelebrityImage image = attrs.remove('src')
        String path = imageService.getPath(image, IMG_SIZE)
        out << """<img src="${path}"    />"""
    }
}
