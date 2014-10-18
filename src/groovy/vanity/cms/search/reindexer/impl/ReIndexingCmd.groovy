package vanity.cms.search.reindexer.impl

import vanity.search.Index

public final class ReIndexingCmd {

    final Index target

    final List<Long> entitiesIds

    ReIndexingCmd(final Index target, final List<Long> entitiesIds) {
        this.target = target
        this.entitiesIds = entitiesIds
    }
}