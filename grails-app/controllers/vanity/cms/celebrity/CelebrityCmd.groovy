package vanity.cms.celebrity

import grails.validation.Validateable
import org.springframework.web.multipart.MultipartFile
import vanity.article.Tag
import vanity.image.utils.ImageWrapper

@Validateable
class CelebrityCmd {

    private static final int IMAGE_MAX_WIDTH = 100

    private static final int IMAGE_MAX_HEIGHT = 100

    Long id

    String firstName

    String lastName

    String description

    MultipartFile avatar

    Tag tag

    static constraints = {
        id(nullable: true)
        firstName(nullable:true, blank:true)
        lastName(nullable:true, blank:true)
        description(nullable:true, blank:true)
        tag(nullable: false, unique: true, validator:{
            return it?.root
        })
        avatar(nullable:true, empty:false, validator:{ val, obj ->
            def wrapper = obj.provideImageWrapper()
            def dimension = wrapper.dimension
            return dimension.width > IMAGE_MAX_WIDTH || dimension.height > IMAGE_MAX_HEIGHT
        })
    }

    public ImageWrapper provideImageWrapper(){
        return new ImageWrapper(avatar)
    }
}
