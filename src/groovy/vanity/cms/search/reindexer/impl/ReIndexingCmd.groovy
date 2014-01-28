package vanity.cms.search.reindexer.impl

import vanity.search.Index

public final class ReIndexingCmd {

    final Index target

    final Date startFrom

    ReIndexingCmd(Index target) {
        this(target, null)
    }

    ReIndexingCmd(Index target, Date startFrom) {
        this.target = target
        this.startFrom = startFrom
    }
}