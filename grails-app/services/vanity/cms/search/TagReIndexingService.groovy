package vanity.cms.search

import org.springframework.transaction.annotation.Transactional
import vanity.article.Tag
import vanity.article.TagStatus

class TagReIndexingService {

    @Transactional(readOnly = true)
    public List<Long> findAllValidForReIndexing() {
        return Tag.executeQuery("""
                    select
                        id
                    from
                        Tag t
                    where
                        status in (:statuses)

                """,
            [
                statuses: (TagStatus.OPEN_STATUSES + [TagStatus.SPAM])
            ]
        ) as List<Long>
    }

    @Transactional(readOnly = true)
    public List<Long> findAllValidForReIndexing(final Date point) {
        return Tag.executeQuery("""
                    select
                        id
                    from
                        Tag t
                    where
                        (dateCreated >= :point or lastUpdated >= :point)
                        and status in (:statuses)

                """,
            [
                point: point,
                statuses: (TagStatus.OPEN_STATUSES + [TagStatus.SPAM])
            ]
        ) as List<Long>
    }

}
