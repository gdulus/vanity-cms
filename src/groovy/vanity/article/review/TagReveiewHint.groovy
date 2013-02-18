package vanity.article.review

import vanity.article.Tag

class TagReveiewHint {

    public final Tag reviewedTag

    public final List<Tag> similarTags

    public final List<Tag> parentTags

    TagReveiewHint(Tag reviewedTag, List<Tag> similarTags, List<Tag> parentTags) {
        this.reviewedTag = reviewedTag
        this.similarTags = similarTags
        this.parentTags = parentTags
    }

    public boolean hasSimilarities(){
        return !similarTags.isEmpty()
    }

    public boolean hasParents(){
        return !parentTags.isEmpty()
    }
}
