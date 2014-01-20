package vanity.cms.article

import org.springframework.transaction.annotation.Transactional
import vanity.article.*

class TagAdminService {

    ArticleService articleService

    TagService tagService

    @Transactional
    void delete(final Long id) {
        Tag tag = Tag.get(id)

        if (!tag) {
            return
        }

        articleService.getByTag(tag)*.removeFromTags(tag)
        tag.delete()
    }

    @Transactional
    Tag save(final String name, final List<Long> parentTagIds) {
        Tag tag = new Tag(
            name: name,
            status: Status.Tag.PUBLISHED,
            root: false
        )

        if (!tag.save()) {
            return tag
        }

        List<Tag> parentTags = parentTagIds.collect { Tag.load(it) }
        parentTags*.addToChildTags(tag)
        return tag
    }

    @Transactional
    Tag update(final Long id, final String name, final List<Long> parentTagIds) {
        Tag tag = Tag.get(id)

        if (!tag) {
            return null
        }

        tag.name = name

        if (!tag.save()) {
            return tag
        }

        List<Tag> parentTags = parentTagIds.collect { Tag.load(it) }
        parentTags*.addToChildTags(tag)
        return tag
    }

    @Transactional
    Article update(final Long id, final Closure binder) {
        Article article = Article.get(id)

        if (!article) {
            return null
        }

        binder.call(article)
        return article.save()
    }
}
