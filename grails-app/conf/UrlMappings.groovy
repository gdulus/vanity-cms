class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?" {}
        "/"(controller: 'tag', action: 'review')
        "500"(view:'/error')
	}
}
