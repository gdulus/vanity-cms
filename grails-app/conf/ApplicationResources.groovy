modules = {
    base {
        resource url:'http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js'
        resource url:'js/bootstrap.min.js'
        resource url:'js/cms.js'
        resource url:'css/bootstrap.min.css'
        resource url:'css/base.css'
    }

    reviewTag {
        dependsOn 'base'
        resource url: 'css/reviewTag.css'
        resource url: 'js/reviewTag.js'
    }

    promoteTag {
        dependsOn 'base'
        resource url: 'css/promoteTag.css'
        resource url: 'js/promoteTag.js'
    }

    celebrityList {
        dependsOn 'base'
    }

    celebrityForm {
        dependsOn 'base'
        resource url: 'css/celebrityForm.css'
    }
}