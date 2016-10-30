modules = {
    base {
        resource url: 'js/jquery-1.12.0.min.js'
        resource url: 'js/bootstrap.min.js'
        resource url: 'js/cms.js'
        resource url: 'css/bootstrap.min.css'
        resource url: 'css/base.css'
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

    articleList {
        dependsOn 'base'
    }

    tagList {
        dependsOn 'base'
    }

    articleForm {
        dependsOn 'base'
        resource url: 'css/articleForm.css'
    }

    tagForm {
        dependsOn 'base'
        resource url: 'css/tagForm.css'
        resource url: 'js/jquery.ba-dotimeout.min.js'
        resource url: 'js/tagForm.js'
    }

    celebrityJobList {
        dependsOn 'base'
    }

    celebrityJobForm {
        dependsOn 'base'
    }

    countryList {
        dependsOn 'base'
    }

    countryForm {
        dependsOn 'base'
    }

}