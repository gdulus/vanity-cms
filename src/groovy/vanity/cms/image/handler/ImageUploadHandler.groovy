package vanity.cms.image.handler

import vanity.image.gorm.Image

interface ImageUploadHandler {

    public Image handle(HandlingSpecification specification)
}
