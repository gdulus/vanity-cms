package vanity.cms.image.handler

import vanity.image.gorm.Image

interface ImageResourceHandler {

    public Image save(SavingSpecification specification)

    public boolean delete(DeletingSpecification specification)
}
