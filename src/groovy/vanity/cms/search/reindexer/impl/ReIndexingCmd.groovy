package vanity.cms.search.reindexer.impl

import vanity.search.Index

public final class ReIndexingCmd {

    final Index target

    final ReIndexingType type

    final Closure dataProvider

    ReIndexingCmd(Index target, ReIndexingType type, Closure dataProvider) {
        this.target = target
        this.type = type
        this.dataProvider = dataProvider
    }
}