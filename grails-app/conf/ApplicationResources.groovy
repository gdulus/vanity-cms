modules = {
    bootstrap {
        resource url:'http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js'
        resource url:'js/bootstrap.min.js'
        resource url:'css/bootstrap.min.css'
    }

    review {
        dependsOn 'bootstrap'
        resource url: 'css/review.css'
        resource url: 'js/review.js'
    }
}