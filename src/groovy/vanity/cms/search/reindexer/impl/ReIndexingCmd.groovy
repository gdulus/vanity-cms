package vanity.cms.search.reindexer.impl

import vanity.search.Index

public final class ReIndexingCmd {

    final Index target

    final ReIndexingType type

    final List<Long> entitiesIds

    ReIndexingCmd(Index target, ReIndexingType type, List<Long> entitiesIds) {
        this.target = target
        this.type = type
        this.entitiesIds = entitiesIds
    }
}