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
        Tag tag = tagService.create(name)

        if (tag.hasErrors()) {
            return tag
        }

        tag.status = Status.Tag.PUBLISHED
        tag.save()
        List<Tag> parentTags = parentTagIds.collect { Tag.get(it) }
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
