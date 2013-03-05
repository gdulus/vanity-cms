class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?" {}
        "/"(controller: 'tag')
		"500"(view:'/error')
	}
}
